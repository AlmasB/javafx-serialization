package com.almasb.javafx.serialization.std

import com.almasb.javafx.serialization.mapper
import com.almasb.javafx.serialization.toColor
import com.almasb.javafx.serialization.toDouble
import com.almasb.javafx.serialization.toInt
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.exc.InvalidDefinitionException
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.treeToValue
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle

/**
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class RectangleSerializer : StdSerializer<Rectangle>(Rectangle::class.java) {

    override fun serialize(value: Rectangle, gen: JsonGenerator, provider: SerializerProvider) {
        gen.writeStartObject()

//        gen.writeStringField("@class", value.javaClass.canonicalName)
//        gen.writeNumberField("width", value.width)
//        gen.writeNumberField("height", value.height)
//
//        gen.writeObjectField("fill", value.fill)

        val methods = value.javaClass.methods
        val methodNames = methods.map { it.name }

        methods.filter { it.name.startsWith("get") }
                .filter { "EventDispatcher" !in it.name }
                .filter {
                    val setterName = it.name.replaceFirstChar { 's' }

                    setterName in methodNames && !setterName.startsWith("setOn")
                }
                .forEach {
                    val result = it.invoke(value)

                    val fieldName = it.name.removePrefix("get").replaceFirstChar { it.uppercaseChar() }

                    println("Writing $fieldName with $result")

                    try {

                        when (result) {
                            is Double -> gen.writeNumberField(fieldName, result)
                            is Int -> gen.writeNumberField(fieldName, result)
                            is String -> gen.writeStringField(fieldName, result)
                            else -> {
                                gen.writeObjectField(fieldName, result)
                            }
                        }
                    } catch (e: Exception) {
                        println("Do not know how to serialize the above: $e")
                    }
                }

        gen.writeEndObject()
    }
}

class RectangleDeserializer : StdDeserializer<Rectangle>(Rectangle::class.java) {

    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): Rectangle {
        val node = p.codec.readTree<JsonNode>(p)

//        val width = node.get("width").toDouble()
//        val height = node.get("height").toDouble()
//
//        val fill = node.get("fill").toColor()

        val rect = Rectangle()


        val methods = rect.javaClass.methods
        val methodNames = methods.map { it.name }

        methods.filter { it.name.startsWith("set") }
                .filter {
                    val getterName = it.name.replaceFirstChar { 'g' }

                    getterName in methodNames && !getterName.startsWith("getOn")
                }
                .forEach {
                    val paramType = it.parameterTypes[0]

                    val serializedValue = node.get(it.name.removePrefix("set").replaceFirstChar { it.uppercaseChar() })

                    println("Calling ${it.name} with $serializedValue")


                    if (paramType == Double::class.java) {

                        // TODO: cast value to actual object
                        it.invoke(rect, serializedValue.toDouble())
                    } else if (paramType == Int::class.java) {

                        it.invoke(rect, serializedValue.toInt())
                    } else if (paramType.isEnum) {

                        val result = paramType.enumConstants.find { it.toString() == serializedValue.textValue() }

                        it.invoke(rect, result)
                    } else {
                        try {
                            // read embedded class param info
                            val classInfo = serializedValue.get("@class")

                            classInfo?.let { info ->
                                val actualParamType = info.asText()

                                val clazz = Class.forName(actualParamType)

                                val actualValue = mapper.convertValue(serializedValue, clazz)

                                it.invoke(rect, actualValue)
                            }


                            // otherwise no class info

                        } catch (e: Exception) {
                            println("Failed to setXXXX: $e")
                        }
                    }
                }

        return rect
    }
}
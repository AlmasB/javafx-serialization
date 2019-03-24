package com.almasb.javafx.serialization.std

import com.almasb.javafx.serialization.toColor
import com.almasb.javafx.serialization.toDouble
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import javafx.scene.shape.Rectangle

/**
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class RectangleSerializer : StdSerializer<Rectangle>(Rectangle::class.java) {

    override fun serialize(value: Rectangle, gen: JsonGenerator, provider: SerializerProvider) {
        gen.writeStartObject()

        gen.writeStringField("@class", value.javaClass.canonicalName)
        gen.writeNumberField("width", value.width)
        gen.writeNumberField("height", value.height)

        gen.writeObjectField("fill", value.fill)

        gen.writeEndObject()
    }
}

class RectangleDeserializer : StdDeserializer<Rectangle>(Rectangle::class.java) {

    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): Rectangle {
        val node = p.codec.readTree<JsonNode>(p)

        val width = node.get("width").toDouble()
        val height = node.get("height").toDouble()

        val fill = node.get("fill").toColor()

        return Rectangle(width, height, fill)
    }
}
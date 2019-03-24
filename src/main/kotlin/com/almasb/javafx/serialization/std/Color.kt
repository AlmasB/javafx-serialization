package com.almasb.javafx.serialization.std

import com.almasb.javafx.serialization.toDouble
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import javafx.scene.paint.Color

/**
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class ColorSerializer : StdSerializer<Color>(Color::class.java) {

    override fun serialize(value: Color, gen: JsonGenerator, provider: SerializerProvider) {
        gen.writeStartObject()

        gen.writeStringField("@class", value.javaClass.canonicalName)
        gen.writeNumberField("red", value.red)
        gen.writeNumberField("green", value.green)
        gen.writeNumberField("blue", value.blue)
        gen.writeNumberField("opacity", value.opacity)

        gen.writeEndObject()
    }
}

class ColorDeserializer : StdDeserializer<Color>(Color::class.java) {
    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): Color {

        val node = p.codec.readTree<JsonNode>(p)

        val red = node.get("red").toDouble()
        val green = node.get("green").toDouble()
        val blue = node.get("blue").toDouble()
        val opacity = node.get("opacity").toDouble()

        return Color.color(red, green, blue, opacity)
    }
}
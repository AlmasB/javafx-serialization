package com.almasb.javafx.serialization.std

import com.almasb.javafx.serialization.toNode
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.node.DoubleNode
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import javafx.scene.Node
import javafx.scene.layout.Pane

/**
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class PaneSerializer : StdSerializer<Pane>(Pane::class.java) {

    override fun serialize(value: Pane, gen: JsonGenerator, provider: SerializerProvider) {
        gen.writeStartObject()

        // TODO: find a way to remove duplicate code and generalize
        gen.writeStringField("@class", value.javaClass.canonicalName)
        gen.writeNumberField("width", value.width)
        gen.writeStringField("id", value.id)

        gen.writeArrayFieldStart("children")

        value.children.forEach {
            gen.writeObject(it)
        }

        gen.writeEndArray()

        gen.writeEndObject()
    }
}

class PaneDeserializer : StdDeserializer<Pane>(Pane::class.java) {
    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): Pane {

        val node = p.codec.readTree<JsonNode>(p)

        val id = node.get("id").asText()
        val width = (node.get("width") as DoubleNode).numberValue() as Double

        val children: List<Node> = node.get("children").map { it.toNode() }

        return Pane(*children.toTypedArray())
    }
}
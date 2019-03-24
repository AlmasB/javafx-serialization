@file:JvmName("FXSerializer")

package com.almasb.javafx.serialization

import com.almasb.javafx.serialization.std.*
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.node.DoubleNode
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import javafx.scene.Node
import javafx.scene.layout.Pane
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle

/**
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */

val mapper = jacksonObjectMapper().apply {
    val module = SimpleModule()
    module.addSerializer(Pane::class.java, PaneSerializer())
    module.addSerializer(Rectangle::class.java, RectangleSerializer())
    module.addSerializer(Color::class.java, ColorSerializer())

    module.addDeserializer(Pane::class.java, PaneDeserializer())
    module.addDeserializer(Rectangle::class.java, RectangleDeserializer())
    module.addDeserializer(Color::class.java, ColorDeserializer())

    registerModule(module)
}

fun JsonNode.toColor(): Color {
    return mapper.convertValue(this)
}

fun JsonNode.toRectangle(): Rectangle {
    return mapper.convertValue(this)
}

fun JsonNode.toNode(): Node {
    return when(this.get("@class").asText()) {
        Rectangle::class.java.canonicalName -> this.toRectangle()
        else -> TODO()
    }
}

fun JsonNode.toDouble(): Double {
    return (this as DoubleNode).numberValue() as Double
}
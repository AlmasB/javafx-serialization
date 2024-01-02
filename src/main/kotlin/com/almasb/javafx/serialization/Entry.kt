package com.almasb.javafx.serialization

import com.fasterxml.jackson.module.kotlin.readValue
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle
import javafx.scene.shape.StrokeType

fun main() {
    serial()
    //test2()
}

private fun serial() {
    val rect = Rectangle(200.0, 300.0, 1000.0, 150.0)
    rect.fill = Color.RED

    val str = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(rect)

    println(str)

    val rect2 = mapper.readValue<Rectangle>(str)

    println(rect2)
}

private fun test2() {
    val clazz = StrokeType::class.java

    val str = "INSIDE"

    val value: StrokeType = clazz.enumConstants.find { it.name == str }!!

    println(value)
}
package com.almasb.javafx.serialization

import com.fasterxml.jackson.module.kotlin.readValue
import javafx.scene.layout.Pane
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.*
import org.junit.jupiter.api.Test

/**
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
class SerializationTest {

    @Test
    fun `Color`() {
        val color1 = Color.color(0.44, 0.55, 0.123, 0.234)
        val value = mapper.writeValueAsString(color1)

        val color2 = mapper.readValue<Color>(value)

        assertThat(color1, `is`(color2))
    }

    @Test
    fun `Pane with Rectangle children`() {
        val pane1 = Pane(Rectangle(22.0, 13.0, Color.BLUE), Rectangle(20.0, 30.0, Color.RED))
        val value = mapper.writeValueAsString(pane1)

        val pane2 = mapper.readValue<Pane>(value)

        assertThat((pane1.children[0] as Rectangle).width, `is`((pane2.children[0] as Rectangle).width))
        assertThat((pane1.children[0] as Rectangle).height, `is`((pane2.children[0] as Rectangle).height))
        assertThat((pane1.children[0] as Rectangle).fill, `is`((pane2.children[0] as Rectangle).fill))

        assertThat((pane1.children[1] as Rectangle).width, `is`((pane2.children[1] as Rectangle).width))
        assertThat((pane1.children[1] as Rectangle).height, `is`((pane2.children[1] as Rectangle).height))
        assertThat((pane1.children[1] as Rectangle).fill, `is`((pane2.children[1] as Rectangle).fill))
    }
}
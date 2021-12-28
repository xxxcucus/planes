package com.planes.test
import com.google.common.truth.Truth
import com.planes.single_player_engine.Coordinate2D
import org.junit.Test

class Coordinate2DTest {
    @Test
    fun Coordinate2D_x() {
        val c1 = Coordinate2D(1, 1)
        Truth.assertThat(c1.x() == 1).isTrue()
    }

    @Test
    fun Coordinate2D_y() {
        val c2 = Coordinate2D(1, 1)
        Truth.assertThat(c2.y() == 1).isTrue()
    }

    @Test
    fun Coordinate2D_add() {
        val c1 = Coordinate2D(1, 0)
        val c2 = Coordinate2D(2, 4)
        val c3 = Coordinate2D(3, 4)
        Truth.assertThat(c1.add(c2).equals(c3)).isTrue()
    }

    @Test
    fun Coordinate2D_clone() {
        val c1 = Coordinate2D(1, 0)
        val c2 = c1.clone() as Coordinate2D
        Truth.assertThat(c1.equals(c2)).isTrue()
    }
}
package com.planes.test
import com.google.common.truth.Truth
import com.planes.single_player_engine.Coordinate2D
import com.planes.single_player_engine.PlaneIntersectingPointIterator
import org.junit.Test

class PlaneIntersectingPointIteratorTest {
    @Test
    fun PlaneIntersectingPointIterator_Values() {
        val point = Coordinate2D(0, 0)
        val pipi = PlaneIntersectingPointIterator(point)
        var count = 0
        while (pipi.hasNext()) {
            val pl = pipi.next()
            Truth.assertThat(pl.containsPoint(point)).isTrue()
            count++
        }
        Truth.assertThat(count > 0).isTrue()
    }
}
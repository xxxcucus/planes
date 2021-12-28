package com.planes.test
import com.planes.single_player_engine.GuessPoint
import com.planes.single_player_engine.HeadData
import com.planes.single_player_engine.Type
import org.junit.Test

class HeadDataTest {
    @Test
    fun HeadData_Init() {
        val hd = HeadData(10, 10, 5, 5)
        hd.update(GuessPoint(5, 5, Type.Dead))
    }
}
import com.google.common.truth.Truth
import com.planes.single_player_engine.ComputerLogic
import org.junit.Test

class ComputerLogicTest {
    @Test
    fun ComputerLogic_MapIndexToPlane() {
        val cl = ComputerLogic(10, 10, 3)
        Truth.assertThat(cl.mapPlaneToIndex(cl.mapIndexToPlane(50))).isEqualTo(50)
    }
}
import com.google.common.truth.Truth
import com.planes_multiplayer.single_player_engine.PlaneOrientationData
import org.junit.Test

class PlaneOrientationDataTest {
    @Test
    fun PlaneOrientationData_AreAllPointsTested() {
        val pod = PlaneOrientationData()
        Truth.assertThat(pod.areAllPointsChecked()).isTrue()
    }
}
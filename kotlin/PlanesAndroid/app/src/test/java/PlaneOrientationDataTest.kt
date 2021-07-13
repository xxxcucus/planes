import com.google.common.truth.Truth
import com.planes.common.PlaneOrientationData
import org.junit.Test

class PlaneOrientationDataTest {
    @Test
    fun PlaneOrientationData_AreAllPointsTested() {
        val pod = PlaneOrientationData()
        Truth.assertThat(pod.areAllPointsChecked()).isTrue()
    }
}
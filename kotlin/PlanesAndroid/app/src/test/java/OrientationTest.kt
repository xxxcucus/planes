import com.google.common.truth.Truth
import com.planes.common.Orientation
import org.junit.Test

class OrientationTest {
    @Test
    fun Orientation_Values() {
        Truth.assertThat(Orientation.values()[0] === Orientation.NorthSouth).isTrue()
        Truth.assertThat(Orientation.values()[1] === Orientation.SouthNorth).isTrue()
        Truth.assertThat(Orientation.values()[2] === Orientation.WestEast).isTrue()
        Truth.assertThat(Orientation.values()[3] === Orientation.EastWest).isTrue()
    }
}
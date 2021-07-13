import com.planes.common.GuessPoint
import com.planes.common.HeadData
import com.planes.common.Type
import org.junit.Test

class HeadDataTest {
    @Test
    fun HeadData_Init() {
        val hd = HeadData(10, 10, 5, 5)
        hd.update(GuessPoint(5, 5, Type.Dead))
    }
}
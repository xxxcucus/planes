import com.google.common.truth.Truth
import com.planes.single_player_engine.*
import org.junit.Test
import java.util.*

class PointInfluenceIteratorTest {
    @Test
    fun PointInfluenceIterator_Values() {
        val itValues = Vector<Coordinate2D?>()
        val pi = PointInfluenceIterator(Coordinate2D(0, 0))
        while (pi.hasNext()) {
            itValues.add(pi.next())
        }
        for (i in 0..3) {
            val pl = Plane(0, 0, Orientation.values()[i])
            val ppi = PlanePointIterator(pl)
            while (ppi.hasNext()) {
                Truth.assertThat(itValues.contains(ppi.next())).isTrue()
            }
        }
    }
}
import com.google.common.truth.Truth
import com.planes_multiplayer.single_player_engine.VectorIterator
import org.junit.Test
import java.util.*

class VectorIteratorTest {
    @Test
    fun VectorIterator_Iterate() {
        val testVectorIterator = VectorIterator<Int>()
        val testVector = Vector<Int>()
        testVector.add(1)
        testVector.add(2)
        testVector.add(3)
        testVectorIterator.setInternalList(testVector)
        var count = 0
        while (testVectorIterator.hasNext()) {
            count++
            Truth.assertThat(testVectorIterator.next() == count).isTrue()
        }
    }
}
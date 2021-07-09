import com.google.common.truth.Truth;
import com.planes.common.VectorIterator;

import org.junit.Test;

import java.util.Vector;

import static com.google.common.truth.Truth.assertThat;

public class VectorIteratorTest {
    @Test
    public void VectorIterator_Iterate() {
        VectorIterator<Integer> testVectorIterator = new VectorIterator<Integer>();
        Vector<Integer> testVector = new Vector<Integer>();
        testVector.add(1);
        testVector.add(2);
        testVector.add(3);
        testVectorIterator.setInternalList(testVector);

        int count = 0;
        while (testVectorIterator.hasNext()) {
            count++;
            assertThat(testVectorIterator.next() == count).isTrue();
        }
    }
}

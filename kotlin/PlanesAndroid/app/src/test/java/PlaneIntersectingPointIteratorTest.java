import org.junit.Test;
import com.google.common.truth.Truth;
import com.planes.common.Coordinate2D;
import com.planes.common.Plane;
import com.planes.common.PlaneIntersectingPointIterator;

import static com.google.common.truth.Truth.assertThat;

public class PlaneIntersectingPointIteratorTest {

    @Test
    public void PlaneIntersectingPointIterator_Values() {
        Coordinate2D point = new Coordinate2D(0, 0);
        PlaneIntersectingPointIterator pipi = new PlaneIntersectingPointIterator(point);

        int count = 0;

        while (pipi.hasNext()) {
            Plane pl = pipi.next();
            assertThat(pl.containsPoint(point)).isTrue();
            count++;
        }

        assertThat(count > 0).isTrue();
    }
}

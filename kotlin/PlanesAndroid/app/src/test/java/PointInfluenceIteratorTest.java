import com.google.common.truth.Truth;
import com.planes.common.Coordinate2D;
import com.planes.common.Orientation;
import com.planes.common.Plane;
import com.planes.common.PlanePointIterator;
import com.planes.common.PointInfluenceIterator;

import org.junit.Test;

import java.util.Vector;

import static com.google.common.truth.Truth.assertThat;

public class PointInfluenceIteratorTest {

    @Test
    public void PointInfluenceIterator_Values() {

        Vector<Coordinate2D> itValues = new Vector<Coordinate2D>();
        PointInfluenceIterator pi = new PointInfluenceIterator(new Coordinate2D(0, 0));
        while (pi.hasNext()) {
            itValues.add(pi.next());
        }

        for (int i = 0; i < 4; i++) {
            Plane pl = new Plane(0, 0, Orientation.values()[i]);
            PlanePointIterator ppi = new PlanePointIterator(pl);

            while (ppi.hasNext()) {
                assertThat(itValues.contains(ppi.next())).isTrue();
            }
        }

    }
}

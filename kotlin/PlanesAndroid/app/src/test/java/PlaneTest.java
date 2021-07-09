import com.google.common.truth.Truth;
import com.planes.common.Coordinate2D;
import com.planes.common.Orientation;
import com.planes.common.Plane;
import com.planes.common.PlanePointIterator;

import org.junit.Test;
import static com.google.common.truth.Truth.assertThat;

public class PlaneTest {

    @Test
    public void Plane_add() {
        Plane pl = new Plane(0, 0, Orientation.EastWest);
        Plane pl_copy = (Plane)pl.clone();
        Plane pl_add = pl.add(new Coordinate2D(1,2));
        assertThat(pl_add.head().x() == 1).isTrue();
        assertThat(pl_add.head().y() == 2).isTrue();
        assertThat(pl.orientation() == pl_copy.orientation()).isTrue();
    }

    @Test
    public void Plane_rotate() {
        Plane pl = new Plane(0, 0, Orientation.EastWest);
        Plane pl_copy = (Plane)pl.clone();
        for (int i = 0; i < 4; i++) {
            pl.rotate();
            assertThat(pl.head().equals(pl_copy.head())).isTrue();
        }
        assertThat(pl.equals(pl_copy));
    }

    @Test
    public void Plane_translateWhenHeadPosValid() {
        int rows = 10;
        int cols = 10;

        Plane pl = new Plane(rows / 2, 0, Orientation.NorthSouth);
        pl.translateWhenHeadPosValid(-1, -1, rows, cols);

        assertThat(pl.head()).isEqualTo(new Coordinate2D(rows / 2, 0));
    }

    @Test
    public void Plane_containsPoint() {
        Plane pl = new Plane(0, 0, Orientation.EastWest);
        PlanePointIterator ppi = new PlanePointIterator(pl);

        while (ppi.hasNext()) {
            Coordinate2D point = ppi.next();
            assertThat(pl.containsPoint(point)).isTrue();
        }

        Plane pl1 = new Plane(0,0, Orientation.WestEast);
        PlanePointIterator ppi1 = new PlanePointIterator(pl1);

        while (ppi1.hasNext()) {
            Coordinate2D point = ppi1.next();
            if (point.equals(pl1.head()))
                continue;
            assertThat(pl.containsPoint(point)).isFalse();
        }
    }

    @Test
    public void Plane_isPositionValid() {
        int rows = 10;
        int cols = 10;

        Plane pl = new Plane(rows / 2, cols / 2, Orientation.WestEast);
        assertThat(pl.isPositionValid(rows, cols)).isTrue();

        Plane pl1 = new Plane(1, cols / 2, Orientation.NorthSouth);
        assertThat(pl1.isPositionValid(rows, cols)).isFalse();
    }

}

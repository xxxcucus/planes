import com.google.common.truth.Truth;
import com.planes.common.Coordinate2D;

import org.junit.Test;
import static com.google.common.truth.Truth.assertThat;

public class Coordinate2DTest {
    @Test
    public void Coordinate2D_x() {
        Coordinate2D c1 = new Coordinate2D(1, 1);
        assertThat(c1.x() == 1).isTrue();
    }

    @Test
    public void Coordinate2D_y() {
        Coordinate2D c2 = new Coordinate2D(1, 1);
        assertThat(c2.y() == 1).isTrue();
    }

    @Test
    public void Coordinate2D_add() {
        Coordinate2D c1 = new Coordinate2D(1, 0);
        Coordinate2D c2 = new Coordinate2D(2, 4);
        Coordinate2D c3 = new Coordinate2D(3, 4);
        assertThat(c1.add(c2).equals(c3)).isTrue();
    }

    @Test
    public void Coordinate2D_clone() {
        Coordinate2D c1 = new Coordinate2D(1, 0);
        Coordinate2D c2 = (Coordinate2D)c1.clone();
        assertThat(c1.equals(c2)).isTrue();
    }
}

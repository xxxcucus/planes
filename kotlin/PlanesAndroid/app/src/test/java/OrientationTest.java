import com.google.common.truth.Truth;
import com.planes.common.Orientation;

import org.junit.Test;
import static com.google.common.truth.Truth.assertThat;

public class OrientationTest {

    @Test
    public void Orientation_Values() {
        assertThat(Orientation.values()[0] == Orientation.NorthSouth).isTrue();
        assertThat(Orientation.values()[1] == Orientation.SouthNorth).isTrue();
        assertThat(Orientation.values()[2] == Orientation.WestEast).isTrue();
        assertThat(Orientation.values()[3] == Orientation.EastWest).isTrue();
    }
}

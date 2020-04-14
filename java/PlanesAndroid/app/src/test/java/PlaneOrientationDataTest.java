import com.google.common.truth.Truth;
import com.planes.common.PlaneOrientationData;

import org.junit.Test;
import static com.google.common.truth.Truth.assertThat;

public class PlaneOrientationDataTest {

    @Test
    public void PlaneOrientationData_AreAllPointsTested() {
        PlaneOrientationData pod = new PlaneOrientationData();
        assertThat(pod.areAllPointsChecked()).isTrue();
    }
}

import com.google.common.truth.Truth;
import com.planes.common.ComputerLogic;

import org.junit.Test;
import static com.google.common.truth.Truth.assertThat;

public class ComputerLogicTest {

    @Test
    public void ComputerLogic_MapIndexToPlane() {
        ComputerLogic cl = new ComputerLogic(10, 10, 3);
        assertThat(cl.mapPlaneToIndex(cl.mapIndexToPlane(50))).isEqualTo(50);
    }
}

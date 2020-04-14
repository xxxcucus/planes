import com.google.common.truth.Truth;
import com.planes.common.GuessPoint;
import com.planes.common.HeadData;
import com.planes.common.Type;

import org.junit.Test;
import static com.google.common.truth.Truth.assertThat;

public class HeadDataTest {

    @Test
    public void HeadData_Init() {
        HeadData hd = new HeadData(10, 10, 5, 5);
        hd.update(new GuessPoint(5,5, Type.Dead));
    }
}

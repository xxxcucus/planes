import com.google.common.truth.Truth;
import org.junit.Test;
import static com.google.common.truth.Truth.assertThat;

import com.planes.common.GuessPoint;
import com.planes.common.Type;


public class GuessPointTest {
    @Test
    public void GuessPoint_SetType() {
        GuessPoint gp1 = new GuessPoint(1, 1);
        GuessPoint gp2 = new GuessPoint(1, 1, Type.Dead);
        gp1.setType(Type.Dead);
        assertThat(gp1.equals(gp2)).isTrue();
    }

    @Test
    public void GuessPoint_Clone() {
        GuessPoint gp1 = new GuessPoint(1, 1, Type.Dead);
        GuessPoint gp2 = (GuessPoint)gp1.clone();
        assertThat(gp1.equals(gp2)).isTrue();
    }
}

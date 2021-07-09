import com.google.common.truth.Truth;
import com.planes.common.Type;

import org.junit.Test;
import static com.google.common.truth.Truth.assertThat;

public class TypeTest {
    @Test
    public void Type_Values() {
        assertThat(Type.values()[0] == Type.Miss).isTrue();
        assertThat(Type.values()[1] == Type.Hit).isTrue();
        assertThat(Type.values()[2] == Type.Dead).isTrue();
    }

}
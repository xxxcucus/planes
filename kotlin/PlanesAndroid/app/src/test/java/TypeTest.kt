import com.google.common.truth.Truth
import com.planes.single_player_engine.Type
import org.junit.Test

class TypeTest {
    @Test
    fun Type_Values() {
        Truth.assertThat(Type.values()[0] === Type.Miss).isTrue()
        Truth.assertThat(Type.values()[1] === Type.Hit).isTrue()
        Truth.assertThat(Type.values()[2] === Type.Dead).isTrue()
    }
}
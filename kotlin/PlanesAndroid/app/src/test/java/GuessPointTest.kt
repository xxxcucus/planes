import com.google.common.truth.Truth
import com.planes.single_player_engine.GuessPoint
import com.planes.single_player_engine.Type
import org.junit.Test

class GuessPointTest {
    @Test
    fun GuessPoint_SetType() {
        val gp1 = GuessPoint(1, 1)
        val gp2 = GuessPoint(1, 1, Type.Dead)
        gp1.setType(Type.Dead)
        Truth.assertThat(gp1.equals(gp2)).isTrue()
    }

    @Test
    fun GuessPoint_Clone() {
        val gp1 = GuessPoint(1, 1, Type.Dead)
        val gp2 = gp1.clone() as GuessPoint
        Truth.assertThat(gp1.equals(gp2)).isTrue()
    }
}
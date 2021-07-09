import com.google.common.truth.Truth;
import com.planes.common.PlaneRound;

import org.junit.Test;
import static com.google.common.truth.Truth.assertThat;

public class PlaneRoundTest {

    @Test
    public void PlaneRound_playerGuessAlreadyMade() {
        PlaneRound pr = new PlaneRound(10, 10, 3);
        assertThat(pr.playerGuessAlreadyMade(0, 0)).isEqualTo(0);
    }
}

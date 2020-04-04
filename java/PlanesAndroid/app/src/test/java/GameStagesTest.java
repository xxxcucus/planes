import com.google.common.truth.Truth;
import com.planes.common.GameStages;

import org.junit.Test;
import static com.google.common.truth.Truth.assertThat;

public class GameStagesTest {

    @Test
    public void GameStages_Values() {
        assertThat(GameStages.values()[0] == GameStages.GameNotStarted).isTrue();
        assertThat(GameStages.values()[1] == GameStages.BoardEditing).isTrue();
        assertThat(GameStages.values()[2] == GameStages.Game).isTrue();
    }
}

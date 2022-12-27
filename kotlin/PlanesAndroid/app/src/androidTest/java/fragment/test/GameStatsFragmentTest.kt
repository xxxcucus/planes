package fragment.test

import androidx.fragment.app.testing.launchFragment
import androidx.lifecycle.Lifecycle
import androidx.test.runner.AndroidJUnit4
import com.google.common.truth.Truth
import com.planes.android.gamestats.GameStatsFragment
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class GameStatsFragmentTest {

    @Test
    fun testOnAttach() {
        var fragmentScenario = launchFragment<GameStatsFragment>(
            initialState = Lifecycle.State.INITIALIZED
        )
        fragmentScenario.onFragment { fragment ->
            Truth.assertThat(fragment.m_Context).isNotNull()
            Truth.assertThat(fragment.m_MultiplayerRound).isNotNull()
        }

        fragmentScenario.close()
    }
}
package fragment.test

import androidx.fragment.app.testing.launchFragment
import androidx.lifecycle.Lifecycle
import androidx.test.runner.AndroidJUnit4
import com.google.common.truth.Truth
import com.planes.android.game.multiplayer.GameFragmentMultiplayer
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class GameFragmentMultiplayerTest {

    @Test
    fun testOnAttach() {
        var fragmentScenario = launchFragment<GameFragmentMultiplayer>(
            initialState = Lifecycle.State.INITIALIZED
        )
        fragmentScenario.onFragment { fragment ->
            Truth.assertThat(fragment.m_PlaneRound).isNotNull()
            Truth.assertThat(fragment.m_GameControls).isNotNull()
            Truth.assertThat(fragment.m_Context).isNotNull()
        }

        fragmentScenario.close()
    }
}
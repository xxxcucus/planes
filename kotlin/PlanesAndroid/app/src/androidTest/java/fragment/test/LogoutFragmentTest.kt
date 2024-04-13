package fragment.test

import androidx.fragment.app.testing.launchFragment
import androidx.lifecycle.Lifecycle
import androidx.test.runner.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.planes.android.creategame.CreateGameStates
import com.planes.android.logout.LogoutFragment
import org.junit.Test
import org.junit.runner.RunWith
import com.planes.android.R
import com.planes.multiplayer_engine.responses.GameStatusResponse
import com.planes.single_player_engine.GameStages


@RunWith(AndroidJUnit4::class)
class LogoutFragmentTest {

    @Test
    fun testOnAttach() {
        var fragmentScenario = launchFragment<LogoutFragment>(
            initialState = Lifecycle.State.INITIALIZED
        )
        fragmentScenario.onFragment { fragment ->
            assertThat(fragment.m_Context).isNotNull()
            assertThat(fragment.m_MultiplayerRound).isNotNull()
            assertThat(fragment.m_CreateGameSettingsService).isNotNull()
        }

        fragmentScenario.close()
    }

    @Test
    fun testPerformLogout() {
        var fragmentScenario = launchFragment<LogoutFragment>(
            initialState = Lifecycle.State.RESUMED
        )
        fragmentScenario.onFragment { fragment ->
            fragment.m_MultiplayerRound.setUserData("test", "test", "test")
            fragment.m_MultiplayerRound.setGameStage(GameStages.BoardEditing)
            fragment.m_MultiplayerRound.setGameData(GameStatusResponse(true, "1", "test", "test", "test",
                "1", "1", "1"))
            fragment.finalizeLogoutSuccessful()
            assertThat(fragment.m_CreateGameSettingsService.createGameState == CreateGameStates.NotSubmitted).isTrue()
            assertThat(fragment.m_CreateGameSettingsService.gameName == "").isTrue()
            assertThat(fragment.m_MultiplayerRound.getUsername() == "").isTrue()
            assertThat(fragment.m_MultiplayerRound.getGameId() == 0L).isTrue()
            assertThat(fragment.m_MultiplayerRound.getGameStage() == 1).isTrue()
            assertThat(fragment.binding.logout.isEnabled == false).isTrue()
            assertThat(fragment.binding.settingsData!!.m_LoginStatus == fragment.m_Context.resources.getString(R.string.nouser)).isTrue()
            assertThat(fragment.binding.settingsData!!.m_Username == "").isTrue()
        }

        fragmentScenario.close()
    }

}

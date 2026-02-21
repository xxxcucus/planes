package fragment.test

import androidx.fragment.app.testing.launchFragment
import androidx.lifecycle.Lifecycle
import androidx.test.runner.AndroidJUnit4
import com.google.common.truth.Truth
import com.planes.android.R
import com.planes.android.creategame.CreateGameStates
import com.planes.android.deleteuser.DeleteUserFragment
import com.planes.multiplayer_engine.responses.GameStatusResponse
import com.planes.single_player_engine.GameStages
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DeleteUserFragmentTest {

    @Test
    fun testOnAttach() {
        var fragmentScenario = launchFragment<DeleteUserFragment>(
            initialState = Lifecycle.State.CREATED
        )
        fragmentScenario.onFragment { fragment ->
            Truth.assertThat(fragment.m_Context).isNotNull()
            Truth.assertThat(fragment.m_MultiplayerRound).isNotNull()
            Truth.assertThat(fragment.m_CreateGameSettingsService).isNotNull()
        }

        fragmentScenario.close()
    }

    @Test
    fun testPerformLogout() {
        var fragmentScenario = launchFragment<DeleteUserFragment>(
            initialState = Lifecycle.State.RESUMED
        )
        fragmentScenario.onFragment { fragment ->
            fragment.m_MultiplayerRound.setUserData("test", "test", "test")
            fragment.m_MultiplayerRound.setGameStage(GameStages.BoardEditing)
            fragment.m_MultiplayerRound.setGameData(
                GameStatusResponse(true, "1", "test", "test", "test",
                "1", "1", "1")
            )
            fragment.finalizeDeleteUserSuccessful()
            Truth.assertThat(fragment.m_CreateGameSettingsService.createGameState == CreateGameStates.NotSubmitted)
                .isTrue()
            Truth.assertThat(fragment.m_CreateGameSettingsService.gameName == "").isTrue()
            Truth.assertThat(fragment.m_MultiplayerRound.getUsername() == "").isTrue()
            Truth.assertThat(fragment.m_MultiplayerRound.getGameId() == 0L).isTrue()
            Truth.assertThat(fragment.m_MultiplayerRound.getGameStage() == 1).isTrue()
            Truth.assertThat(fragment.binding.deleteuser.isEnabled == false).isTrue()
            Truth.assertThat(
                fragment.binding.settingsData!!.m_LoginStatus == fragment.m_Context.resources.getString(
                    R.string.nouser
                )
            ).isTrue()
            Truth.assertThat(fragment.binding.settingsData!!.m_Username == "").isTrue()
        }

        fragmentScenario.close()
    }

}

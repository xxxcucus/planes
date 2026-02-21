package fragment.test

import androidx.fragment.app.testing.launchFragment
import androidx.lifecycle.Lifecycle
import androidx.test.runner.AndroidJUnit4
import com.google.common.truth.Truth
import com.google.gson.Gson
import com.planes.android.creategame.CreateGameFragment
import com.planes.multiplayer_engine.responses.ConnectToGameResponse
import com.planes.multiplayer_engine.responses.CreateGameResponse
import com.planes.multiplayer_engine.responses.ErrorResponse
import com.planes.multiplayer_engine.responses.GameStatusResponse
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class CreateGameFragmentTest {

    @Test
    fun testOnAttach() {
        var fragmentScenario = launchFragment<CreateGameFragment>(
            initialState = Lifecycle.State.CREATED
        )
        fragmentScenario.onFragment { fragment ->
            Truth.assertThat(fragment.m_CreateGameSettingsService).isNotNull()
            Truth.assertThat(fragment.m_MultiplayerRound).isNotNull()
            Truth.assertThat(fragment.m_Context).isNotNull()
        }

        fragmentScenario.close()
    }

    @Test
    fun testValidateUsernamePassword() {
        var fragmentScenario = launchFragment<CreateGameFragment>(
            initialState = Lifecycle.State.INITIALIZED
        )
        fragmentScenario.onFragment { fragment ->
            Truth.assertThat(fragment.validationGameName("")).isFalse()
            Truth.assertThat(fragment.validationGameName("testGameNametestGameNametestGameName")).isFalse()
            Truth.assertThat(fragment.validationGameName("testGameName")).isTrue()
        }

        fragmentScenario.close()
    }

    @Test
    fun testReactToGameStatusInPolling() {
        var fragmentScenario = launchFragment<CreateGameFragment>(
            initialState = Lifecycle.State.RESUMED
        )
        fragmentScenario.onFragment { fragment ->
            fragment.m_MultiplayerRound.setUserId(2L)
            fragment.m_MultiplayerRound.setUserData("testFirstPlayerName", "testPassword", "testAuthToken")
            var response = GameStatusResponse(true, "1", "testGameName", "testFirstPlayerName",
                   "testSecondPlayerName", "2", "3", "4" )
            fragment.reactToGameStatusInPolling(response)
            Truth.assertThat(fragment.m_MultiplayerRound.getGameId() == 1L).isTrue()
            Truth.assertThat(fragment.m_MultiplayerRound.getGameData().gameName == "testGameName").isTrue()
            Truth.assertThat(fragment.m_MultiplayerRound.getUsername() == "testFirstPlayerName").isTrue()
            Truth.assertThat(fragment.m_MultiplayerRound.getOpponentName() == "testSecondPlayerName").isTrue()
            Truth.assertThat(fragment.m_MultiplayerRound.getUserId() == 2L).isTrue()
            Truth.assertThat(fragment.m_MultiplayerRound.getOpponentId() == 3L).isTrue()
            Truth.assertThat(fragment.m_MultiplayerRound.getRoundId() == 4L).isTrue()
            Truth.assertThat(fragment.m_GameName == "testGameName").isTrue()
        }

        fragmentScenario.close()
    }

    @Test
    fun testReactToGameCreation() {
        var fragmentScenario = launchFragment<CreateGameFragment>(
            initialState = Lifecycle.State.RESUMED
        )
        fragmentScenario.onFragment { fragment ->
            fragment.m_MultiplayerRound.setUserData("testFirstPlayerName", "testPassword", "testAuthToken")
            var response = CreateGameResponse(true, "1", "testGameName", "testFirstPlayerName",
                "testSecondPlayerName", "2", "3", "4" )
            fragment.reactToGameCreation(null, response)
            Truth.assertThat(fragment.m_MultiplayerRound.getGameId() == 1L).isTrue()
            Truth.assertThat(fragment.m_MultiplayerRound.getGameData().gameName == "testGameName").isTrue()
            Truth.assertThat(fragment.m_MultiplayerRound.getGameData().username == "testSecondPlayerName").isTrue()
            Truth.assertThat(fragment.m_MultiplayerRound.getOpponentName() == "testFirstPlayerName").isTrue()
            Truth.assertThat(fragment.m_MultiplayerRound.getUserId() == 3L).isTrue()
            Truth.assertThat(fragment.m_MultiplayerRound.getOpponentId() == 2L).isTrue()
            Truth.assertThat(fragment.m_MultiplayerRound.getRoundId() == 4L).isTrue()
            Truth.assertThat(fragment.m_GameName == "testGameName").isTrue()
        }

        fragmentScenario.close()
    }

    @Test
    fun testReactToGameCreationError() {
        var fragmentScenario = launchFragment<CreateGameFragment>(
            initialState = Lifecycle.State.RESUMED
        )
        fragmentScenario.onFragment { fragment ->
            val gson = Gson()
            var errorResponse = ErrorResponse("", 0, "TestError", "TestMessage", "")
            val errorResponseString = gson.toJson(errorResponse).toString()
            fragment.reactToGameCreation(errorResponseString, null)
            Truth.assertThat(fragment.m_CreateGameError).isTrue()
            Truth.assertThat(fragment.m_CreateGameErrorString.contains("TestMessage"));
        }

        fragmentScenario.close()
    }

    @Test
    fun testReactToConnectToGame() {
        var fragmentScenario = launchFragment<CreateGameFragment>(
            initialState = Lifecycle.State.RESUMED
        )
        fragmentScenario.onFragment { fragment ->
            fragment.m_MultiplayerRound.setUserData("testFirstPlayerName", "testPassword", "testAuthToken")
            var response = ConnectToGameResponse(true, "1", "testGameName", "testFirstPlayerName",
                "testSecondPlayerName", "2", "3", "4" )
            fragment.reactToConnectToGame(null, response)
            Truth.assertThat(fragment.m_MultiplayerRound.getGameId() == 1L).isTrue()
            Truth.assertThat(fragment.m_MultiplayerRound.getGameData().gameName == "testGameName").isTrue()
            Truth.assertThat(fragment.m_MultiplayerRound.getGameData().username == "testSecondPlayerName").isTrue()
            Truth.assertThat(fragment.m_MultiplayerRound.getOpponentName() == "testFirstPlayerName").isTrue()
            Truth.assertThat(fragment.m_MultiplayerRound.getUserId() == 3L).isTrue()
            Truth.assertThat(fragment.m_MultiplayerRound.getOpponentId() == 2L).isTrue()
            Truth.assertThat(fragment.m_MultiplayerRound.getRoundId() == 4L).isTrue()
            Truth.assertThat(fragment.m_GameName == "testGameName").isTrue()
        }

        fragmentScenario.close()
    }

    @Test
    fun testReactToConnectToGameError() {
        var fragmentScenario = launchFragment<CreateGameFragment>(
            initialState = Lifecycle.State.RESUMED
        )
        fragmentScenario.onFragment { fragment ->
            val gson = Gson()
            var errorResponse = ErrorResponse("", 0, "TestError", "TestMessage", "")
            val errorResponseString = gson.toJson(errorResponse).toString()
            fragment.reactToConnectToGame(errorResponseString, null)
            Truth.assertThat(fragment.m_CreateGameError).isTrue()
            Truth.assertThat(fragment.m_CreateGameErrorString.contains("TestMessage"));
        }

        fragmentScenario.close()
    }

    @Test
    fun testReactToGameStatusExists() {
        var fragmentScenario = launchFragment<CreateGameFragment>(
            initialState = Lifecycle.State.RESUMED
        )
        fragmentScenario.onFragment { fragment ->
            var response = GameStatusResponse(true, "1", "testGameName", "testFirstPlayerName",
                "testFirstPlayerName", "2", "3", "4" )
            fragment.reactToGameStatus(null, response)
            Truth.assertThat(fragment.m_CreateGameError).isFalse()
        }

        fragmentScenario.close()
    }

    @Test
    fun testReactToGameStatusNotExist() {
        var fragmentScenario = launchFragment<CreateGameFragment>(
            initialState = Lifecycle.State.RESUMED
        )
        fragmentScenario.onFragment { fragment ->
            var response = GameStatusResponse(false, "1", "testGameName", "testFirstPlayerName",
                "testSecondPlayerName", "2", "3", "4" )
            fragment.reactToGameStatus(null, response)
            Truth.assertThat(fragment.m_CreateGameError).isFalse()
        }

        fragmentScenario.close()
    }

    @Test
    fun testReactToGameStatusGameCreationNotPossible() {
        var fragmentScenario = launchFragment<CreateGameFragment>(
            initialState = Lifecycle.State.RESUMED
        )
        fragmentScenario.onFragment { fragment ->
            var response = GameStatusResponse(false, "1", "testGameName", "testFirstPlayerName",
                "testSecondPlayerName", "2", "3", "4" )
            fragment.reactToGameStatus(null, response)
            Truth.assertThat(fragment.m_CreateGameError).isFalse()
        }

        fragmentScenario.close()
    }

    @Test
    fun testReactToGameStatusError() {
        var fragmentScenario = launchFragment<CreateGameFragment>(
            initialState = Lifecycle.State.RESUMED
        )
        fragmentScenario.onFragment { fragment ->
            val gson = Gson()
            var errorResponse = ErrorResponse("", 0, "TestError", "TestMessage", "")
            val errorResponseString = gson.toJson(errorResponse).toString()
            fragment.reactToGameStatus(errorResponseString, null)
            Truth.assertThat(fragment.m_CreateGameError).isTrue()
            Truth.assertThat(fragment.m_CreateGameErrorString.contains("TestMessage"));
        }

        fragmentScenario.close()
    }
}
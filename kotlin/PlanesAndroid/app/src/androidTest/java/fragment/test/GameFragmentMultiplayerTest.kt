package fragment.test

import androidx.fragment.app.testing.launchFragment
import androidx.lifecycle.Lifecycle
import androidx.test.runner.AndroidJUnit4
import com.google.common.truth.Truth
import com.planes.android.game.multiplayer.GameFragmentMultiplayer
import com.planes.multiplayer_engine.requests.SingleMoveRequest
import com.planes.multiplayer_engine.responses.AcquireOpponentPositionsResponse
import com.planes.multiplayer_engine.responses.SendNotSentMovesResponse
import com.planes.multiplayer_engine.responses.SendPlanePositionsResponse
import com.planes.single_player_engine.GameStages
import com.planes.single_player_engine.Orientation
import com.planes.single_player_engine.Plane
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

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

    @Test
    fun testReceivedOpponentPlanePositionsCancelled() {
        var fragmentScenario = launchFragment<GameFragmentMultiplayer>(
            initialState = Lifecycle.State.RESUMED
        )
        fragmentScenario.onFragment { fragment ->
            var response = SendPlanePositionsResponse(true, false, 1, 1,1,1,
                        1, 1,1, 1,1)
            fragment.receivedOpponentPlanePositions(response)
            Truth.assertThat(fragment.m_PlaneRound.getGameStage() == GameStages.GameNotStarted.value).isTrue()
        }

        fragmentScenario.close()
    }

    @Test
    fun testReceivedOpponentPlanePositionsOtherExist() {
        var fragmentScenario = launchFragment<GameFragmentMultiplayer>(
            initialState = Lifecycle.State.RESUMED
        )
        fragmentScenario.onFragment { fragment ->
            var response = SendPlanePositionsResponse(false, true, 1, 2,0,3,
                4, 1,5, 6,2)
            fragment.receivedOpponentPlanePositions(response)
            var plane1 = fragment.m_PlaneRound.getComputerPlaneNo(0)
            var plane2 = fragment.m_PlaneRound.getComputerPlaneNo(1)
            var plane3 = fragment.m_PlaneRound.getComputerPlaneNo(2)
            Truth.assertThat(plane1.row == 1).isTrue()
            Truth.assertThat(plane1.col == 2).isTrue()
            Truth.assertThat(plane1.orientation() == Orientation.NorthSouth).isTrue()
            Truth.assertThat(plane2.row == 3).isTrue()
            Truth.assertThat(plane2.col == 4).isTrue()
            Truth.assertThat(plane2.orientation() == Orientation.SouthNorth).isTrue()
            Truth.assertThat(plane3.row == 5).isTrue()
            Truth.assertThat(plane3.col == 6).isTrue()
            Truth.assertThat(plane3.orientation() == Orientation.WestEast).isTrue()
        }

        fragmentScenario.close()
    }

    @Test
    fun testReceivedOpponentPlanePositionsOtherDoNotExist() {
        var fragmentScenario = launchFragment<GameFragmentMultiplayer>(
            initialState = Lifecycle.State.RESUMED
        )
        fragmentScenario.onFragment { fragment ->
            var response = SendPlanePositionsResponse(false, false, 1, 2,0,3,
                4, 1,5, 6,2)
            fragment.receivedOpponentPlanePositions(response)
            Truth.assertThat(fragment.m_PlaneRound.getGameStage() == GameStages.WaitForOpponentPlanesPositions.value).isTrue()
        }

        fragmentScenario.close()
    }

    @Test
    fun testReactToOpponentPlanePositionsInPollingCancelled() {
        var fragmentScenario = launchFragment<GameFragmentMultiplayer>(
            initialState = Lifecycle.State.RESUMED
        )
        fragmentScenario.onFragment { fragment ->
            var response = AcquireOpponentPositionsResponse(true, false, 1, 1,1,1,
                1, 1,1, 1,1)
            fragment.reactToOpponentPlanePositionsInPolling(response)
            Truth.assertThat(fragment.m_PlaneRound.getGameStage() == GameStages.GameNotStarted.value).isTrue()
        }

        fragmentScenario.close()
    }

    @Test
    fun testReactToOpponentPlanePositionsInPollingOtherExist() {
        var fragmentScenario = launchFragment<GameFragmentMultiplayer>(
            initialState = Lifecycle.State.RESUMED
        )
        fragmentScenario.onFragment { fragment ->
            var response = AcquireOpponentPositionsResponse(false, true, 1, 2,0,3,
                4, 1,5, 6,2)
            fragment.reactToOpponentPlanePositionsInPolling(response)
            var plane1 = fragment.m_PlaneRound.getComputerPlaneNo(0)
            var plane2 = fragment.m_PlaneRound.getComputerPlaneNo(1)
            var plane3 = fragment.m_PlaneRound.getComputerPlaneNo(2)
            Truth.assertThat(plane1.row == 1).isTrue()
            Truth.assertThat(plane1.col == 2).isTrue()
            Truth.assertThat(plane1.orientation() == Orientation.NorthSouth).isTrue()
            Truth.assertThat(plane2.row == 3).isTrue()
            Truth.assertThat(plane2.col == 4).isTrue()
            Truth.assertThat(plane2.orientation() == Orientation.SouthNorth).isTrue()
            Truth.assertThat(plane3.row == 5).isTrue()
            Truth.assertThat(plane3.col == 6).isTrue()
            Truth.assertThat(plane3.orientation() == Orientation.WestEast).isTrue()
        }

        fragmentScenario.close()
    }

    @Test
    fun testFinalizeCancelRound() {
        var fragmentScenario = launchFragment<GameFragmentMultiplayer>(
            initialState = Lifecycle.State.RESUMED
        )
        fragmentScenario.onFragment { fragment ->
            fragment.finalizeCancelRound()
            Truth.assertThat(fragment.m_PlaneRound.getGameStage() == GameStages.GameNotStarted.value).isTrue()
        }

        fragmentScenario.close()
    }


    @Test
    fun testFinalizeSendWinner() {
        var fragmentScenario = launchFragment<GameFragmentMultiplayer>(
            initialState = Lifecycle.State.RESUMED
        )
        fragmentScenario.onFragment { fragment ->
            fragment.finalizeSendWinner()
            Truth.assertThat(fragment.m_PlaneRound.getGameStage() == GameStages.GameNotStarted.value).isTrue()
        }

        fragmentScenario.close()
    }

    @Test
    fun testReceivedSendMoveResponseCancelled() {
        var fragmentScenario = launchFragment<GameFragmentMultiplayer>(
            initialState = Lifecycle.State.RESUMED
        )
        fragmentScenario.onFragment { fragment ->
            var response = SendNotSentMovesResponse("1", "2", 0, true, Vector<SingleMoveRequest>())
            fragment.receivedSendMoveResponse(response)
            Truth.assertThat(fragment.m_PlaneRound.getGameStage() == GameStages.GameNotStarted.value).isTrue()
        }

        fragmentScenario.close()
    }

    @Test
    fun testReceivedSendMoveResponseNotCancelled() {
        var fragmentScenario = launchFragment<GameFragmentMultiplayer>(
            initialState = Lifecycle.State.RESUMED
        )
        fragmentScenario.onFragment { fragment ->
            var movesVector = Vector<SingleMoveRequest>()
            movesVector.addAll(listOf(SingleMoveRequest(3, 4, 5)))
            var response = SendNotSentMovesResponse("1", "2", 0, false, movesVector)
            fragment.m_PlaneRound.addToNotSentMoves(0)
            fragment.m_PlaneRound.saveNotSentMoves()
            fragment.m_PlaneRound.setGameStage(GameStages.Game)
            fragment.receivedSendMoveResponse(response)
            Truth.assertThat(fragment.m_PlaneRound.getNotSentMoveCount() == 0).isTrue()
            Truth.assertThat(fragment.m_PlaneRound.getReceivedMovesCount() == 1).isTrue()
        }

        fragmentScenario.close()
    }

    @Test
    fun testReactToOpponentMovesInPollingCancelled() {
        var fragmentScenario = launchFragment<GameFragmentMultiplayer>(
            initialState = Lifecycle.State.RESUMED
        )
        fragmentScenario.onFragment { fragment ->
            var response = SendNotSentMovesResponse("1", "2", 0, true, Vector<SingleMoveRequest>())
            fragment.reactToOpponentMovesInPolling(response)
            Truth.assertThat(fragment.m_PlaneRound.getGameStage() == GameStages.GameNotStarted.value).isTrue()
        }

        fragmentScenario.close()
    }

    @Test
    fun testReactToOpponentMovesInPollingNotCancelled() {
        var fragmentScenario = launchFragment<GameFragmentMultiplayer>(
            initialState = Lifecycle.State.RESUMED
        )
        fragmentScenario.onFragment { fragment ->
            var movesVector = Vector<SingleMoveRequest>()
            movesVector.addAll(listOf(SingleMoveRequest(3, 4, 5)))
            var response = SendNotSentMovesResponse("1", "2", 0, false, movesVector)
            fragment.m_PlaneRound.addToNotSentMoves(0)
            fragment.m_PlaneRound.saveNotSentMoves()
            fragment.m_PlaneRound.setGameStage(GameStages.Game)
            fragment.reactToOpponentMovesInPolling(response)
            Truth.assertThat(fragment.m_PlaneRound.getNotSentMoveCount() == 0).isTrue()
            Truth.assertThat(fragment.m_PlaneRound.getReceivedMovesCount() == 1).isTrue()
        }

        fragmentScenario.close()
    }

    @Test
    fun testFinalizeStartNewRound() {
        var fragmentScenario = launchFragment<GameFragmentMultiplayer>(
            initialState = Lifecycle.State.RESUMED
        )
        fragmentScenario.onFragment { fragment ->
            fragment.finalizeStartNewRound()
            Truth.assertThat(fragment.m_PlaneRound.getGameStage() == GameStages.BoardEditing.value).isTrue()
        }

        fragmentScenario.close()
    }
}
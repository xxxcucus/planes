package com.planes.android.game.multiplayer

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.planes.android.*
import com.planes.android.customviews.*
import com.planes.android.game.singleplayer.*
import com.planes.multiplayer_engine.MultiplayerRoundJava
import com.planes.multiplayer_engine.requests.AcquireOpponentPositionsRequest
import com.planes.multiplayer_engine.requests.SendPlanePositionsRequest
import com.planes.multiplayer_engine.responses.AcquireOpponentPositionsResponse
import com.planes.multiplayer_engine.responses.GameStatusResponse
import com.planes.multiplayer_engine.responses.SendPlanePositionsResponse
import com.planes.single_player_engine.GameStages
import com.planes.single_player_engine.Orientation
import com.planes.single_player_engine.Plane
import com.planes.single_player_engine.PlanesRoundJava
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.Headers
import java.util.concurrent.TimeUnit

class GameFragmentMultiplayer : Fragment() {

    private lateinit var m_PlaneRound: MultiplayerRoundInterface
    private lateinit var m_GameBoards: GameBoardsAdapterMultiplayer
    private lateinit var m_GameControls: GameControlsAdapterMultiplayer
    private lateinit var m_PlanesLayout: PlanesVerticalLayoutMultiplayer
    private lateinit var m_DonePositioningSubscription: Disposable
    private lateinit var m_PollOpponentPositionsSubscription: Disposable
    private lateinit var m_Context: Context

    private var m_SendPlanePositionsError: Boolean = false
    private var m_SendPlanePositionsErrorString: String = ""

    private var m_ReceiveOpponentPlanePositionsError: Boolean = false
    private var m_ReceiveOpponentPlanePositionsErrorString: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        m_PlaneRound = MultiplayerRoundJava()
        (m_PlaneRound as MultiplayerRoundJava).createPlanesRound()

        m_GameControls = GameControlsAdapterMultiplayer(context)
        m_Context = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var rootView = inflater.inflate(R.layout.fragment_game_multiplayer, container, false)

        m_PlanesLayout = rootView.findViewById<View>(R.id.planes_layout) as PlanesVerticalLayoutMultiplayer

        var isTablet = false
        var isHorizontal = false
        val linearLayout = rootView.findViewById<View>(R.id.rootView) as LinearLayout
        if (linearLayout.tag.toString().contains("tablet")) {
            isTablet = true
        }
        if (linearLayout.tag.toString().contains("horizontal")) {
            isHorizontal = true
        }
        m_GameBoards = if (showTwoBoards(isTablet)) {
            val playerBoard = rootView.findViewById<View>(R.id.player_board) as GameBoardMultiplayer
            playerBoard.setGameSettings(m_PlaneRound, true)
            playerBoard.setPlayerBoard()
            val computerBoard = rootView.findViewById<View>(R.id.computer_board) as GameBoardMultiplayer
            computerBoard.setGameSettings(m_PlaneRound, true)
            computerBoard.setComputerBoard()
            GameBoardsAdapterMultiplayer(playerBoard, computerBoard)
        } else {
            val gameBoard = rootView.findViewById<View>(R.id.game_boards) as GameBoardMultiplayer
            gameBoard.setGameSettings(m_PlaneRound, false)
            GameBoardsAdapterMultiplayer(gameBoard)
        }

        //Board Editing Buttons
        val doneButton = rootView.findViewById<View>(R.id.done_button) as Button
        val rotateButton = rootView.findViewById<View>(R.id.rotate_button) as Button
        val cancelBoardEditingButton = rootView.findViewById<View>(R.id.cancel_boardediting) as Button
        val progressBarBoardEditing = rootView.findViewById<View>(R.id.ProgressBarBoardEditing) as ProgressBar

        //Game Stage
        val statsTitle = rootView.findViewById<View>(R.id.stats_title_label) as TwoLineTextButton
        val viewOpponentBoardButton1 = rootView.findViewById<View>(R.id.view_opponent_board1) as TwoLineTextButtonWithState
        val cancelGameButton = rootView.findViewById<View>(R.id.cancel_game) as TextButton
        val progressBarGameButton = rootView.findViewById<View>(R.id.ProgressBarGame) as ProgressBar

        //Start New Game Stage
        val viewComputerBoardButton2 = rootView.findViewById<View>(R.id.view_opponent_board2) as TwoLineTextButtonWithState
        val startNewGameButton = rootView.findViewById<View>(R.id.start_new_game) as TwoLineTextButton
        val computerWinsLabel = rootView.findViewById<View>(R.id.opponent_wins_label) as ColouredSurfaceWithText
        val computerWinsCount = rootView.findViewById<View>(R.id.opponent_wins_count) as ColouredSurfaceWithText
        val playerWinsLabel = rootView.findViewById<View>(R.id.player_wins_label) as ColouredSurfaceWithText
        val playerWinsCount = rootView.findViewById<View>(R.id.player_wins_count) as ColouredSurfaceWithText
        val winnerText = rootView.findViewById<View>(R.id.winner_textview) as ColouredSurfaceWithText
        val drawsLabel = rootView.findViewById<View>(R.id.draws_label) as ColouredSurfaceWithText
        val drawsCount = rootView.findViewById<View>(R.id.draws_count) as ColouredSurfaceWithText

        m_GameControls.setBoardEditingControls(doneButton, rotateButton, cancelBoardEditingButton, progressBarBoardEditing, ::doneClicked)
        if (!showTwoBoards(isTablet)) m_GameControls.setGameControls(statsTitle, viewOpponentBoardButton1, cancelBoardEditingButton, progressBarGameButton)
        m_GameControls.setStartNewGameControls(viewComputerBoardButton2, startNewGameButton, computerWinsLabel, computerWinsCount, playerWinsLabel, playerWinsCount, drawsLabel, drawsCount, winnerText)
        m_GameControls.setGameSettings(m_PlaneRound, isTablet)
        m_GameControls.setGameBoards(m_GameBoards)
        m_GameControls.setPlanesLayout(m_PlanesLayout)
        m_GameBoards.setGameControls(m_GameControls)

        reinitializeFromState()

        (activity as MainActivity).setActionBarTitle(getString(R.string.game))
        (activity as MainActivity).setCurrentFragmentId(ApplicationScreens.Game)
        return rootView
    }

    fun reinitializeFromState() {
        when ((m_PlaneRound as MultiplayerRoundJava).getGameStage()) {
            0 -> {
                m_GameBoards.setNewRoundStage()
                m_GameControls.setNewRoundStage()
                m_PlanesLayout.setComputerBoard()
                m_PlanesLayout.setNewRoundStage()
            }
            1 -> {
                m_GameBoards.setBoardEditingStage()
                m_GameControls.setBoardEditingStage(false)
                m_PlanesLayout.setBoardEditingStage()
            }
            2 -> {
                m_GameBoards.setGameStage()
                m_GameControls.setGameStage()
                m_PlanesLayout.setGameStage()
            }
            3 -> {
                m_GameBoards.setBoardEditingStage()
                m_GameControls.setBoardEditingStage(true)
                m_PlanesLayout.setBoardEditingStage()
                pollForOpponentPlanesPositions()
            }
        }
    }

    override fun onDetach () {
        super.onDetach()
        hideLoading()
        if (this::m_PollOpponentPositionsSubscription.isInitialized)
            m_PollOpponentPositionsSubscription.dispose()
    }

    override fun onPause() {
        super.onPause()
        if (this::m_PollOpponentPositionsSubscription.isInitialized)
            m_PollOpponentPositionsSubscription.dispose()
    }

    override fun onResume() {
        super.onResume()
        reinitializeFromState()
    }

    fun showTwoBoards(isTablet: Boolean): Boolean {
        return false
    }

    //region BoardEditing
    fun doneClicked() {
        m_SendPlanePositionsError = false
        m_SendPlanePositionsErrorString = ""

        if (!userLoggedIn()) {
            finalizeSendPlanePositions()
            return
        }

        if (!connectedToGame()) {
            finalizeSendPlanePositions()
            return
        }

        var plane1 = m_PlaneRound.getPlayerPlaneNo(0)
        var plane2 = m_PlaneRound.getPlayerPlaneNo(1)
        var plane3 = m_PlaneRound.getPlayerPlaneNo(2)

        var sendPlanePositionsRequest = buildPlanePositionsRequest(m_PlaneRound.getGameId(), m_PlaneRound.getRoundId(), m_PlaneRound.getUserId(),
                    m_PlaneRound.getOpponentId(), plane1, plane2, plane3)

        var sendPlanePositions = m_PlaneRound.sendPlanePositions(sendPlanePositionsRequest)
        m_DonePositioningSubscription = sendPlanePositions
            .delay (1500, TimeUnit.MILLISECONDS ) //TODO: to remove this
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { _ -> showLoading() }
            .doOnTerminate { hideLoading() }
            .doOnComplete { hideLoading() }
            .subscribe({data -> receivedOpponentPlanePositions(data.code(), data.errorBody()?.string(), data.headers(), data.body())}
                , {error -> setSendPlanePositionsError(error.localizedMessage.toString())});

    }

    private fun buildPlanePositionsRequest(
        gameId: Long, roundId: Long, userId: Long, opponentId: Long, plane1: Plane, plane2: Plane, plane3: Plane): SendPlanePositionsRequest {

        return SendPlanePositionsRequest(gameId.toString(), roundId.toString(), userId.toString(), opponentId.toString(),
            plane1.row(), plane1.col(), plane1.orientation().value, plane2.row(), plane2.col(), plane2.orientation().value,
            plane3.row(), plane3.col(), plane3.orientation().value)

    }

    //TODO: to make work for all error variables and move to MultiplayerRound
    fun userLoggedIn(): Boolean {
        if (!m_PlaneRound.isUserLoggedIn()) {
            m_SendPlanePositionsError = true
            m_SendPlanePositionsErrorString = getString(R.string.validation_user_not_loggedin)
            return false
        }
        return true
    }

    fun connectedToGame(): Boolean {
        if (!m_PlaneRound.isUserConnectedToGame()) {
            m_SendPlanePositionsError = true
            m_SendPlanePositionsErrorString = getString(R.string.validation_not_connected_to_game)
            return false
        }
        return true
    }

    fun finalizeSendPlanePositions() {
        if (m_SendPlanePositionsError) {
            (activity as MainActivity).onWarning(m_SendPlanePositionsErrorString)
        } else {
            if (m_PlaneRound.getGameStage() == GameStages.Game.value) {  //plane positions where received
                m_GameBoards.setGameStage()
                m_GameControls.setGameStage()
                m_PlanesLayout.setGameStage()
            } else {
                pollForOpponentPlanesPositions()
            }
        }
    }

    fun setSendPlanePositionsError(errorMsg: String) {
        m_SendPlanePositionsError = true
        m_SendPlanePositionsErrorString = errorMsg
        finalizeSendPlanePositions()
    }

    fun receivedOpponentPlanePositions(code: Int, jsonErrorString: String?, headrs: Headers, body: SendPlanePositionsResponse?) {
        if (body != null)  {
            var otherPositionsExist = body!!.m_OtherExist

            if (otherPositionsExist) {

                var plane1_x = body!!.m_Plane1X
                var plane1_y = body!!.m_Plane1Y
                var plane2_x = body!!.m_Plane2X
                var plane2_y = body!!.m_Plane2Y
                var plane3_x = body!!.m_Plane3X
                var plane3_y = body!!.m_Plane3Y
                var plane1_orient = Orientation.EastWest
                var plane2_orient = Orientation.NorthSouth
                var plane3_orient = Orientation.NorthSouth

                try {
                    plane1_orient = Orientation.fromInt(body!!.m_Plane1Orient)
                    plane2_orient = Orientation.fromInt(body!!.m_Plane2Orient)
                    plane3_orient = Orientation.fromInt(body!!.m_Plane3Orient)
                } catch(e: NoSuchElementException) {
                    m_SendPlanePositionsError = true
                    m_SendPlanePositionsErrorString = getString(R.string.invalid_plane_orientation)
                }

                var setOk = m_PlaneRound.setComputerPlanes(plane1_x, plane1_y, plane1_orient, plane2_x, plane2_y, plane2_orient,
                        plane3_x, plane3_y, plane3_orient)

                if (!setOk) {
                    m_SendPlanePositionsError = true
                    m_SendPlanePositionsErrorString = getString(R.string.error_init_opponent_board)
                } else {
                    Tools.displayToast(getString(R.string.plane_positions_received), m_Context)
                }
            } else {
                m_PlaneRound.setGameStage(GameStages.WaitForOpponentPlanesPositions)
                m_GameControls.setBoardEditingStage(true)
                pollForOpponentPlanesPositions()
            }
        } else {
            m_SendPlanePositionsErrorString = Tools.parseJsonError(jsonErrorString, getString(R.string.sendplanepositions_error),
                getString(R.string.unknownerror))
            m_SendPlanePositionsError = true
        }
        finalizeSendPlanePositions()
    }

    fun pollForOpponentPlanesPositions() {
        m_ReceiveOpponentPlanePositionsError = false
        m_ReceiveOpponentPlanePositionsErrorString = ""

        Tools.displayToast(getString(R.string.waiting_for_planes_positions), m_Context)
        var acquireOpponentPlanePositionsRequest = buildAcquireOpponentPlanePositionsRequest(m_PlaneRound.getGameId(), m_PlaneRound.getRoundId(), m_PlaneRound.getUserId(),
            m_PlaneRound.getOpponentId())

        if (!this::m_PollOpponentPositionsSubscription.isInitialized) {
            m_PollOpponentPositionsSubscription =
                Observable.interval(5, TimeUnit.SECONDS, Schedulers.io())
                    .flatMap { _ -> m_PlaneRound.acquireOpponentPlanePositions(acquireOpponentPlanePositionsRequest) }
                    .doOnError { setReceiveOpponentPlanePositionsError(getString(R.string.error_plane_positions)) }
                    .retry()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ data -> reactToOpponentPlanePositionsInPolling(data.body()) },
                        { error -> setReceiveOpponentPlanePositionsError(error.localizedMessage.toString()) })
        }
    }

    fun setReceiveOpponentPlanePositionsError(errorMsg: String) {
        m_ReceiveOpponentPlanePositionsError = true
        m_ReceiveOpponentPlanePositionsErrorString = errorMsg
        finalizeReceiveOpponentPlanePositions()
    }

    fun finalizeReceiveOpponentPlanePositions() {
        if (m_ReceiveOpponentPlanePositionsError) {
            (activity as MainActivity).onWarning(m_ReceiveOpponentPlanePositionsErrorString)
        } else {
            if (m_PlaneRound.getGameStage() == GameStages.Game.value) {  //plane positions where received
                m_GameBoards.setGameStage()
                m_GameControls.setGameStage()
                m_PlanesLayout.setGameStage()
                if (this::m_PollOpponentPositionsSubscription.isInitialized)
                    m_PollOpponentPositionsSubscription.dispose()
            } else {
                //when position of planes were not received
            }
        }
    }

    fun buildAcquireOpponentPlanePositionsRequest(gameId: Long, roundId: Long, userId: Long, opponentId: Long): AcquireOpponentPositionsRequest {
        return AcquireOpponentPositionsRequest(gameId.toString(), roundId.toString(), userId.toString(), opponentId.toString())
    }

    fun reactToOpponentPlanePositionsInPolling(body: AcquireOpponentPositionsResponse?) {
        if (body != null) {
            var otherPositionsExist = body!!.m_OtherExist

            if (otherPositionsExist) {

                var plane1_x = body!!.m_Plane1X
                var plane1_y = body!!.m_Plane1Y
                var plane2_x = body!!.m_Plane2X
                var plane2_y = body!!.m_Plane2Y
                var plane3_x = body!!.m_Plane3X
                var plane3_y = body!!.m_Plane3Y
                var plane1_orient = Orientation.EastWest
                var plane2_orient = Orientation.NorthSouth
                var plane3_orient = Orientation.NorthSouth

                try {
                    plane1_orient = Orientation.fromInt(body!!.m_Plane1Orient)
                    plane2_orient = Orientation.fromInt(body!!.m_Plane2Orient)
                    plane3_orient = Orientation.fromInt(body!!.m_Plane3Orient)
                } catch (e: NoSuchElementException) {
                    m_ReceiveOpponentPlanePositionsError = true
                    m_ReceiveOpponentPlanePositionsErrorString =
                        getString(R.string.invalid_plane_orientation)
                }

                var setOk = m_PlaneRound.setComputerPlanes(
                    plane1_x, plane1_y, plane1_orient, plane2_x, plane2_y, plane2_orient,
                    plane3_x, plane3_y, plane3_orient
                )

                if (!setOk) {
                    m_ReceiveOpponentPlanePositionsError = true
                    m_ReceiveOpponentPlanePositionsErrorString =
                        getString(R.string.error_init_opponent_board)
                } else {
                    Tools.displayToast(getString(R.string.plane_positions_received), m_Context)
                }
            }
        }
        finalizeReceiveOpponentPlanePositions()
    }
    //endregion BoardEditing

    fun showLoading() {
        (activity as MainActivity).startProgressDialog()
    }

    fun hideLoading() {
        (activity as MainActivity).stopProgressDialog()
    }
}
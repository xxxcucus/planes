package com.planes.android.game.multiplayer

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import com.planes.android.*
import com.planes.android.customviews.*
import com.planes.multiplayer_engine.MultiplayerRoundJava
import com.planes.multiplayer_engine.commobj.LoginWithoutLoadingCommObj
import com.planes.multiplayer_engine.commobj.SimpleRequestCommObj
import com.planes.multiplayer_engine.commobj.SimpleRequestWithoutLoadingCommObj
import com.planes.multiplayer_engine.requests.AcquireOpponentPositionsRequest
import com.planes.multiplayer_engine.requests.SendNotSentMovesRequest
import com.planes.multiplayer_engine.requests.SendPlanePositionsRequest
import com.planes.multiplayer_engine.responses.*
import com.planes.single_player_engine.*
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Response
import java.util.concurrent.TimeUnit

class GameFragmentMultiplayer : Fragment(), IGameFragmentMultiplayer {

    lateinit var m_PlaneRound: MultiplayerRoundInterface
    private lateinit var m_GameBoards: GameBoardsAdapterMultiplayer
    lateinit var m_GameControls: GameControlsAdapterMultiplayer
    private lateinit var m_PlanesLayout: PlanesVerticalLayoutMultiplayer

    private lateinit var m_DonePositioningCommObj: SimpleRequestCommObj<SendPlanePositionsResponse>
    private lateinit var m_CancelRoundCommObj: SimpleRequestCommObj<CancelRoundResponse>
    private lateinit var m_SendMoveCommObj: SimpleRequestWithoutLoadingCommObj<SendNotSentMovesResponse>
    private lateinit var m_SendWinnerCommObj: SimpleRequestCommObj<SendWinnerResponse>
    private lateinit var m_StartNewRoundCommObj: SimpleRequestCommObj<StartNewRoundResponse>
    private lateinit var m_LoginWhenTokenExpiredCommObj: LoginWithoutLoadingCommObj

    private lateinit var m_PollOpponentPositionsSubscription: Disposable
    private lateinit var m_PollOpponentMovesSubscription: Disposable

    lateinit var m_Context: Context

    private var m_ReceiveOpponentPlanePositionsError: Boolean = false
    private var m_ReceiveOpponentPlanePositionsErrorString: String = ""

    private var m_ReceiveOpponentMovesError: Boolean = false
    private var m_ReceiveOpponentMovesErrorString: String = ""

    private var m_Draw: Boolean = false
    private var m_WinnerId: Long = 0L

    override fun onAttach(context: Context) {
        super.onAttach(context)

        m_PlaneRound = MultiplayerRoundJava()
        (m_PlaneRound as MultiplayerRoundJava).createPlanesRound()
        m_PlaneRound.setGameFragment(this)

        m_GameControls = GameControlsAdapterMultiplayer(context)
        m_Context = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_game_multiplayer, container, false)

        m_PlanesLayout = rootView.findViewById<View>(R.id.planes_layout) as PlanesVerticalLayoutMultiplayer

        var isTablet = false
        val linearLayout = rootView.findViewById<View>(R.id.rootView) as LinearLayout
        if (linearLayout.tag.toString().contains("tablet")) {
            isTablet = true
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

        m_GameControls.setBoardEditingControls(doneButton, rotateButton, cancelBoardEditingButton, progressBarBoardEditing, ::doneClicked, ::cancelRound)
        if (!showTwoBoards(isTablet)) m_GameControls.setGameControls(statsTitle, viewOpponentBoardButton1, cancelGameButton, progressBarGameButton, ::cancelRound, ::showGameStats)
        m_GameControls.setStartNewGameControls(viewComputerBoardButton2, startNewGameButton, computerWinsLabel, computerWinsCount, playerWinsLabel,
            playerWinsCount, drawsLabel, drawsCount, winnerText, ::startNewGame)
        m_GameControls.setGameSettings(m_PlaneRound, isTablet)
        m_GameControls.setGameBoards(m_GameBoards)
        m_GameControls.setPlanesLayout(m_PlanesLayout)
        m_GameBoards.setGameControls(m_GameControls)

        performLoginWhenTokenExpired()
        reinitializeFromState()

        if (activity is MainActivity) {
            (activity as MainActivity).setActionBarTitle(getString(R.string.game))
            (activity as MainActivity).setCurrentFragmentId(ApplicationScreens.Game)
        }
        return rootView
    }

    private fun reinitializeFromState() {
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
                m_GameControls.setGameStage(false)
                m_PlanesLayout.setGameStage()
            }
            3 -> {
                m_GameBoards.setBoardEditingStage()
                m_GameControls.setBoardEditingStage(true)
                m_PlanesLayout.setBoardEditingStage()
                pollForOpponentPlanesPositions()
            }
            4 -> {
                m_GameBoards.setGameStage()
                m_GameControls.setGameStage(true)
                m_PlanesLayout.setGameStage()
                pollForOpponentMoves(true)
            }
            5 -> {
                m_GameBoards.setGameStage()
                m_GameControls.setGameStage(true)
                m_PlanesLayout.setGameStage()
                pollForOpponentMoves(false)
            }

        }
    }

    override fun onDetach () {
        super.onDetach()
        hideLoading()
        disposeAllSubscriptions()
    }

    private fun disposeAllSubscriptions() {
        if (this::m_DonePositioningCommObj.isInitialized)
            m_DonePositioningCommObj.disposeSubscription()
        if (this::m_SendWinnerCommObj.isInitialized)
            m_SendWinnerCommObj.disposeSubscription()
        if (this::m_SendMoveCommObj.isInitialized)
            m_SendMoveCommObj.disposeSubscription()
        if (this::m_CancelRoundCommObj.isInitialized)
            m_CancelRoundCommObj.disposeSubscription()
        if (this::m_StartNewRoundCommObj.isInitialized) {
            m_StartNewRoundCommObj.disposeSubscription()
        }
        if (this::m_LoginWhenTokenExpiredCommObj.isInitialized) {
            m_LoginWhenTokenExpiredCommObj.disposeSubscription()
        }

        disposeAllPollingSubscriptions()
        hideLoading()
    }

    private fun disposeAllPollingSubscriptions() {
        if (this::m_PollOpponentPositionsSubscription.isInitialized)
            m_PollOpponentPositionsSubscription.dispose()
        if (this::m_PollOpponentMovesSubscription.isInitialized)
            m_PollOpponentMovesSubscription.dispose()
    }

    override fun onPause() {
        super.onPause()
        disposeAllPollingSubscriptions()
    }

    override fun onResume() {
        super.onResume()
        reinitializeFromState()
    }

    private fun showTwoBoards(isTablet: Boolean): Boolean {
        return false
    }

    private fun saveCredentialsTokenExpired(username: String, password: String, authorizationHeader: String) {
        m_PlaneRound.setUserData(username, password, authorizationHeader)
    }

    private fun createObservableTokenExpired() : Observable<Response<LoginResponse>> {
        return m_PlaneRound.login(m_PlaneRound.getUsername(), m_PlaneRound.getPassword())
    }

    private fun performLoginWhenTokenExpired() {
        if (m_PlaneRound.authTokenExpired()) {
            m_LoginWhenTokenExpiredCommObj = LoginWithoutLoadingCommObj(
                ::createObservableTokenExpired,
                getString(R.string.loginerror),
                getString(R.string.unknownerror),
                m_PlaneRound.getUsername(),
                m_PlaneRound.getPassword(),
                ::saveCredentialsTokenExpired,
                requireActivity()
            )
            m_LoginWhenTokenExpiredCommObj.makeRequest()
        }
    }

    //region BoardEditing

    //region DoneClicked
    private fun createObservableDoneClicked(): Observable<Response<SendPlanePositionsResponse>> {
        val plane1 = m_PlaneRound.getPlayerPlaneNo(0)
        val plane2 = m_PlaneRound.getPlayerPlaneNo(1)
        val plane3 = m_PlaneRound.getPlayerPlaneNo(2)

        val sendPlanePositionsRequest = buildPlanePositionsRequest(m_PlaneRound.getGameId(), m_PlaneRound.getRoundId(), m_PlaneRound.getUserId(),
            m_PlaneRound.getOpponentId(), plane1, plane2, plane3)

        return m_PlaneRound.sendPlanePositions(sendPlanePositionsRequest)
    }

    private fun buildPlanePositionsRequest(
        gameId: Long, roundId: Long, userId: Long, opponentId: Long, plane1: Plane, plane2: Plane, plane3: Plane): SendPlanePositionsRequest {

        return SendPlanePositionsRequest(gameId.toString(), roundId.toString(), userId.toString(), opponentId.toString(),
            plane1.row(), plane1.col(), plane1.orientation().value, plane2.row(), plane2.col(), plane2.orientation().value,
            plane3.row(), plane3.col(), plane3.orientation().value)

    }

    private fun doneClicked() {

        m_DonePositioningCommObj = SimpleRequestCommObj(::createObservableDoneClicked,
            getString(R.string.sendplanepositions_error), getString(R.string.unknownerror), getString(R.string.validation_user_not_loggedin),
                getString(R.string.validation_not_connected_to_game), ::receivedOpponentPlanePositions, ::finalizeSendPlanePositions, requireActivity())

        m_DonePositioningCommObj.makeRequest()
    }

    private fun finalizeSendPlanePositions() {
        if (m_PlaneRound.getGameStage() == GameStages.Game.value) {  //plane positions where received
            disposeAllPollingSubscriptions()
            reinitializeFromState()
        } else if (m_PlaneRound.getGameStage() == GameStages.WaitForOpponentPlanesPositions.value) {
            m_GameControls.setBoardEditingStage(true)
            pollForOpponentPlanesPositions()
        } else if (m_PlaneRound.getGameStage() == GameStages.GameNotStarted.value) {
            disposeAllSubscriptions()
            reinitializeFromState()
        }
    }

    fun receivedOpponentPlanePositions(body: SendPlanePositionsResponse): String {
        var errorString = ""

        if (body.m_Cancelled) {
            m_PlaneRound.cancelRound()
            m_GameControls.roundEnds(isComputerWinner = false, isDraw = false, isCancelled = true)
            Tools.displayToast(getString(R.string.roundcancelled_opponent), m_Context)
            finalizeSendPlanePositions()
            return errorString
        }

        val otherPositionsExist = body.m_OtherExist

        if (otherPositionsExist) {

            val plane1_x = body.m_Plane1X
            val plane1_y = body.m_Plane1Y
            val plane2_x = body.m_Plane2X
            val plane2_y = body.m_Plane2Y
            val plane3_x = body.m_Plane3X
            val plane3_y = body.m_Plane3Y
            var plane1_orient = Orientation.EastWest
            var plane2_orient = Orientation.NorthSouth
            var plane3_orient = Orientation.NorthSouth

            try {
                plane1_orient = Orientation.fromInt(body.m_Plane1Orient)
                plane2_orient = Orientation.fromInt(body.m_Plane2Orient)
                plane3_orient = Orientation.fromInt(body.m_Plane3Orient)
            } catch(e: NoSuchElementException) {
                errorString = getString(R.string.invalid_plane_orientation)
            }

            val setOk = m_PlaneRound.setComputerPlanes(plane1_x, plane1_y, plane1_orient, plane2_x, plane2_y, plane2_orient,
                    plane3_x, plane3_y, plane3_orient)

            if (!setOk) {
                errorString = getString(R.string.error_init_opponent_board)
            } else {
                Tools.displayToast(getString(R.string.plane_positions_received), m_Context)
            }
        } else {
            m_PlaneRound.setGameStage(GameStages.WaitForOpponentPlanesPositions)
        }
        return errorString
    }

    //endregion DoneClicked

    //region PollOpponentPlanes
    private fun pollForOpponentPlanesPositions() {
        m_ReceiveOpponentPlanePositionsError = false
        m_ReceiveOpponentPlanePositionsErrorString = ""

        disposeAllPollingSubscriptions()

        Tools.displayToast(getString(R.string.waiting_for_planes_positions), m_Context)
        val acquireOpponentPlanePositionsRequest = buildAcquireOpponentPlanePositionsRequest(m_PlaneRound.getGameId(), m_PlaneRound.getRoundId(), m_PlaneRound.getUserId(),
            m_PlaneRound.getOpponentId())


        m_PollOpponentPositionsSubscription =
            Observable.interval(5, TimeUnit.SECONDS, Schedulers.io())
                .flatMap { m_PlaneRound.acquireOpponentPlanePositions(acquireOpponentPlanePositionsRequest) }
                .doOnError { setReceiveOpponentPlanePositionsError(getString(R.string.error_plane_positions)) }
                .retry()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ data -> reactToOpponentPlanePositionsInPolling(data.body()) }
                ) { error ->
                    error.localizedMessage?.let {
                        setReceiveOpponentPlanePositionsError(
                            it
                        )
                    }
                }

    }

    private fun setReceiveOpponentPlanePositionsError(errorMsg: String) {
        m_ReceiveOpponentPlanePositionsError = true
        m_ReceiveOpponentPlanePositionsErrorString = errorMsg
        finalizeReceiveOpponentPlanePositions()
    }

    private fun finalizeReceiveOpponentPlanePositions() {
        if (m_ReceiveOpponentPlanePositionsError) {
            if (activity is MainActivity)
                (activity as MainActivity).onWarning(m_ReceiveOpponentPlanePositionsErrorString)
        } else {
            if (m_PlaneRound.getGameStage() == GameStages.Game.value) {  //plane positions where received
                reinitializeFromState()
                disposeAllPollingSubscriptions()
            } else if (m_PlaneRound.getGameStage() == GameStages.GameNotStarted.value) {
                disposeAllSubscriptions()
                reinitializeFromState()
            } else {
                //when position of planes were not received
            }
        }
    }

    private fun buildAcquireOpponentPlanePositionsRequest(gameId: Long, roundId: Long, userId: Long, opponentId: Long): AcquireOpponentPositionsRequest {
        return AcquireOpponentPositionsRequest(gameId.toString(), roundId.toString(), userId.toString(), opponentId.toString())
    }

    fun reactToOpponentPlanePositionsInPolling(body: AcquireOpponentPositionsResponse?) {
        if (body != null) {

            if (body.m_Cancelled) {
                m_PlaneRound.cancelRound()
                m_GameControls.roundEnds(
                    isComputerWinner = false,
                    isDraw = false,
                    isCancelled = true
                )
                Tools.displayToast(getString(R.string.roundcancelled_opponent), m_Context)
                finalizeReceiveOpponentPlanePositions()
                return
            }
            val otherPositionsExist = body.m_OtherExist

            if (otherPositionsExist) {

                val plane1_x = body.m_Plane1X
                val plane1_y = body.m_Plane1Y
                val plane2_x = body.m_Plane2X
                val plane2_y = body.m_Plane2Y
                val plane3_x = body.m_Plane3X
                val plane3_y = body.m_Plane3Y
                var plane1_orient = Orientation.EastWest
                var plane2_orient = Orientation.NorthSouth
                var plane3_orient = Orientation.NorthSouth

                try {
                    plane1_orient = Orientation.fromInt(body.m_Plane1Orient)
                    plane2_orient = Orientation.fromInt(body.m_Plane2Orient)
                    plane3_orient = Orientation.fromInt(body.m_Plane3Orient)
                } catch (e: NoSuchElementException) {
                    m_ReceiveOpponentPlanePositionsError = true
                    m_ReceiveOpponentPlanePositionsErrorString =
                        getString(R.string.invalid_plane_orientation)
                }

                val setOk = m_PlaneRound.setComputerPlanes(
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
    //region PollOpponentPlanes

    //endregion BoardEditing

    //region CancelRound

    private fun createObservableCancelRound() : Observable<Response<CancelRoundResponse>> {
        return m_PlaneRound.cancelRound(m_PlaneRound.getGameId(), m_PlaneRound.getRoundId())
    }
    private fun cancelRound() {
        m_CancelRoundCommObj = SimpleRequestCommObj(::createObservableCancelRound,
            getString(R.string.error_cancelround), getString(R.string.unknownerror), getString(R.string.validation_user_not_loggedin),
            getString(R.string.validation_not_connected_to_game), { "" }, ::finalizeCancelRound, requireActivity())

        m_CancelRoundCommObj.makeRequest()
    }

    fun finalizeCancelRound() {
        m_PlaneRound.cancelRound()
        m_GameControls.roundEnds(isComputerWinner = false, isDraw = false, isCancelled = true)
        disposeAllSubscriptions()
        reinitializeFromState()
        //TODO dispose polling for opponent moves
    }
    //endregion CancelRound

    //region Game

    //region SendWinner

    private fun createObservableSendWinner(): Observable<Response<SendWinnerResponse>> {
        return m_PlaneRound.sendWinner(m_Draw, m_WinnerId)
    }

    override fun sendWinner(draw: Boolean, winnerId: Long) {
        if (this::m_PollOpponentMovesSubscription.isInitialized) {
            m_PollOpponentMovesSubscription.dispose()
        }

        m_Draw = draw
        m_WinnerId = winnerId

        m_SendWinnerCommObj = SimpleRequestCommObj(::createObservableSendWinner,
            getString(R.string.sendwinner_error), getString(R.string.unknownerror), getString(R.string.validation_user_not_loggedin),
            getString(R.string.validation_not_connected_to_game), { "" }, ::finalizeSendWinner, requireActivity())

        m_SendWinnerCommObj.makeRequest()

    }

    fun finalizeSendWinner() {
        m_GameControls.roundEnds(!m_PlaneRound.playerGuess_IsPlayerWinner(), m_PlaneRound.playerGuess_IsDraw())
        m_PlaneRound.roundEnds(!m_PlaneRound.playerGuess_IsPlayerWinner(), m_PlaneRound.playerGuess_IsDraw())
        reinitializeFromState()
    }

    //endregion SendWinner

    //region SendMove

    private fun createObservableSendMove() : Observable<Response<SendNotSentMovesResponse>> {
        val sendMoveRequest = buildSendMoveRequest(m_PlaneRound.getGameId(), m_PlaneRound.getRoundId(), m_PlaneRound.getUserId(),
            m_PlaneRound.getOpponentId())

        return m_PlaneRound.sendMove(sendMoveRequest)
    }

    private fun buildSendMoveRequest(gameId: Long, roundId: Long, userId: Long, opponentId: Long): SendNotSentMovesRequest {
        val receivedMovesData = m_PlaneRound.computeNotReceivedMoves()
        val notSentMoves = m_PlaneRound.prepareNotSentMoves()

        return SendNotSentMovesRequest(gameId.toString(), roundId.toString(), userId.toString(), opponentId.toString(), receivedMovesData.second,
            notSentMoves, receivedMovesData.first)
    }

    override fun sendMove(gp: GuessPoint, playerMoveIndex: Int) {

        m_PlaneRound.addToNotSentMoves(playerMoveIndex)
        if (this::m_SendMoveCommObj.isInitialized) {
            if (m_SendMoveCommObj.isActive()) {
                return  //currently sending another move
            }
        }
        m_PlaneRound.saveNotSentMoves()

        m_SendMoveCommObj = SimpleRequestWithoutLoadingCommObj(::createObservableSendMove,
            getString(R.string.sendmove_error), getString(R.string.unknownerror), getString(R.string.validation_user_not_loggedin),
            getString(R.string.validation_not_connected_to_game), ::receivedSendMoveResponse, ::finalizeSendMove, requireActivity())

        m_SendMoveCommObj.makeRequest()
    }

    private fun finalizeSendMove() {
         if (m_PlaneRound.getGameStage() == GameStages.GameNotStarted.value) {
             disposeAllSubscriptions()
             reinitializeFromState()
         }
    }

    fun receivedSendMoveResponse(body: SendNotSentMovesResponse) : String{
        if (body.m_Cancelled) {
            m_PlaneRound.cancelRound()
            m_GameControls.roundEnds(isComputerWinner = false, isDraw = false, isCancelled = true)
            Tools.displayToast(getString(R.string.roundcancelled_opponent), m_Context)
        } else {
            if (m_PlaneRound.getGameStage() == GameStages.GameNotStarted.value) { //TODO: what is this for ?
                reinitializeFromState()
            } else {
                m_PlaneRound.deleteFromNotSentList()
                for (move in body.m_ListMoves) {
                    val gp = GuessPoint(move.m_MoveX, move.m_MoveY)
                    val moveIdx = move.m_MoveIndex
                    if (!m_PlaneRound.moveAlreadyReceived(moveIdx)) {
                        m_PlaneRound.addOpponentMove(gp, moveIdx)
                    }
                }
            }
        }
        return ""
    }

    //endregion SendMove

    //region PollOpponentMoves
    override fun pollForOpponentMoves(playerFinished: Boolean) {
        m_ReceiveOpponentMovesError = false
        m_ReceiveOpponentMovesErrorString = ""

        disposeAllPollingSubscriptions()

        if (playerFinished) {
            Tools.displayToast(getString(R.string.waiting_for_moves), m_Context)
            m_PlaneRound.setGameStage(GameStages.WaitForOpponentMoves)
        } else {
            Tools.displayToast(getString(R.string.sending_remaining_moves), m_Context)
            m_PlaneRound.setGameStage(GameStages.SendRemainingMoves)
        }
        m_GameControls.setGameStage(true)

        m_PollOpponentMovesSubscription =
            Observable.interval(5, TimeUnit.SECONDS, Schedulers.io())
                .flatMap { buildSendMoveRequestInPolling() }
                .doOnError { setReceiveOpponentMovesError(getString(R.string.error_moves)) }
                .retry()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ data -> reactToOpponentMovesInPolling(data.body()) }
                ) { error -> error.localizedMessage?.let { setReceiveOpponentMovesError(it) } }
    }


    private fun buildSendMoveRequestInPolling(): Observable<Response<SendNotSentMovesResponse>> {
        m_PlaneRound.saveNotSentMoves()
        val sendMoveRequest = buildSendMoveRequest(m_PlaneRound.getGameId(), m_PlaneRound.getRoundId(), m_PlaneRound.getUserId(),
            m_PlaneRound.getOpponentId())
        return m_PlaneRound.sendMove(sendMoveRequest)
    }

    private fun setReceiveOpponentMovesError(errorMsg: String) {
        m_ReceiveOpponentMovesError = true
        m_ReceiveOpponentMovesErrorString = errorMsg
        finalizeReceiveOpponentMoves()
    }

    private fun finalizeReceiveOpponentMoves() {
        if (m_ReceiveOpponentMovesError) {
            if (activity is MainActivity)
                (activity as MainActivity).onWarning(m_ReceiveOpponentPlanePositionsErrorString)
        } else if (m_PlaneRound.getGameStage() == GameStages.GameNotStarted.value) {
            disposeAllSubscriptions()
            reinitializeFromState()
        }
    }

    fun reactToOpponentMovesInPolling(body: SendNotSentMovesResponse?) {
        if (body != null) {

            if (body.m_Cancelled) {
                m_PlaneRound.cancelRound()
                m_GameControls.roundEnds(
                    isComputerWinner = false,
                    isDraw = false,
                    isCancelled = true
                )
                Tools.displayToast(getString(R.string.roundcancelled_opponent), m_Context)
                finalizeReceiveOpponentMoves()
                return
            }

            if (m_PlaneRound.getGameStage() == GameStages.GameNotStarted.value) { //TODO: what is this for ?
                reinitializeFromState()
            } else {
                m_PlaneRound.deleteFromNotSentList()

                if (body.m_ListMoves.isEmpty() && m_PlaneRound.getGameStage() == GameStages.SendRemainingMoves.value) {
                    m_PlaneRound.checkWinnerSent()
                }

                for (move in body.m_ListMoves) {
                    val gp = GuessPoint(move.m_MoveX, move.m_MoveY)
                    val moveIdx = move.m_MoveIndex
                    if (!m_PlaneRound.moveAlreadyReceived(moveIdx)) {
                        m_PlaneRound.addOpponentMove(gp, moveIdx)
                    }
                }
            }
        }
        finalizeReceiveOpponentMoves()
    }

    //endregion PollOpponentMoves

    //endregion Game

    //region StartNewGame

    private fun createObservableStartNewGame() : Observable<Response<StartNewRoundResponse>> {
        return m_PlaneRound.startNewRound(m_PlaneRound.getGameId(), m_PlaneRound.getUserId(), m_PlaneRound.getOpponentId())
    }

    private fun startNewGame() {
        m_StartNewRoundCommObj = SimpleRequestCommObj(::createObservableStartNewGame,
            getString(R.string.error_startnewround), getString(R.string.unknownerror), getString(R.string.validation_user_not_loggedin),
            getString(R.string.validation_not_connected_to_game), ::receivedStartNewRoundResponse, ::finalizeStartNewRound, requireActivity())

        m_StartNewRoundCommObj.makeRequest()
    }

    fun finalizeStartNewRound() {
        m_PlaneRound.initRound()
        disposeAllSubscriptions()
        reinitializeFromState()
    }

    private fun receivedStartNewRoundResponse(body: StartNewRoundResponse): String {
        m_PlaneRound.setRoundId(body.m_RoundId.toLong())
        return ""
    }

    //endregion StartNewGame

    private fun hideLoading() {
        if (activity is MainActivity)
            (activity as MainActivity).stopProgressDialog()
    }

    private fun showGameStats() {
        if (activity is MainActivity)
            (activity as MainActivity).startGameStatsFragment()
    }

}
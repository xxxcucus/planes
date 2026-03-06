package com.planes.android.screens.multiplayergame

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.planes.android.repository.PlanesGameRepository
import com.planes.android.screens.createmultiplayergame.CreateGameStates
import com.planes.android.screens.createmultiplayergame.GameStatus
import com.planes.android.screens.singleplayergame.PlaneGridViewModel
import com.planes.multiplayer_engine.requests.GameStatusRequest
import com.planes.multiplayer_engine.requests.SendNotSentMovesRequest
import com.planes.multiplayer_engine.requests.SendWinnerRequest
import com.planes.multiplayer_engine.requests.SingleMoveRequest
import com.planes.multiplayer_engine.requests.StartNewRoundRequest
import com.planes.multiplayerengine.MultiPlayerRoundInterface
import com.planes.singleplayerengine.GuessPoint
import com.planes.singleplayerengine.Plane
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Vector
import javax.inject.Inject
import kotlin.text.get
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class ComputerGridViewModelMultiPlayer @Inject constructor(planeRound: MultiPlayerRoundInterface,
                                                         private val repository: PlanesGameRepository
): PlaneGridViewModel(planeRound,true) {

    private var m_Error = mutableStateOf<String?>("");

    private var m_Authorization = mutableStateOf<String?>(null)
    private var m_GameName = mutableStateOf<String?>(null)
    private var m_GameId = mutableStateOf<String?>(null)
    private var m_RoundId = mutableStateOf<String?>(null)
    private var m_UserName = mutableStateOf<String?>(null)
    private var m_UserId = mutableStateOf<String?>(null)
    private var m_OpponentId = mutableStateOf<String?>(null)

    private var m_SentMoves = mutableStateListOf<Int>()
    private var m_ReceivedMoves = mutableStateListOf<Int>()
    private var m_SendMovesCancelled = mutableStateOf(false)

    private var m_SendingMoveState = mutableStateOf(false)

    private var m_PollingForComputerMoves = mutableStateOf(false)

    private var m_PreparedForGame = mutableStateOf(false)

    private var m_RoundEnds = mutableStateOf(false)

    private var m_StartNewRound = mutableStateOf(false)

    private var m_PlaneRoundMultiplayer = planeRound


    fun updateRoundEnds() {
        val pgr = m_PlaneRoundMultiplayer.checkRoundEndAsync()
        m_RoundEnds.value = pgr.m_RoundEnds
    }

    fun getRoundEnds() : Boolean {
        return m_RoundEnds.value
    }

    fun getStartNewRound() : Boolean {
        return m_StartNewRound.value
    }
    fun setCredentials(authorization: MutableState<String?>, gameName: MutableState<String?>,
                       gameId : MutableState<String?>, roundId: MutableState<String?>,
                       username: MutableState<String?>, userid: MutableState<String?>,
                       opponnentid: MutableState<String?>) {
        m_Authorization = authorization
        m_GameName = gameName
        m_GameId = gameId
        m_RoundId = roundId
        m_UserName = username
        m_UserId = userid
        m_OpponentId = opponnentid
    }

    fun getGameName() : String? {
        return m_GameName.value
    }

    fun getRoundId() : String? {
        return m_RoundId.value
    }

    fun savePlanes(planes: List<Plane>) {
        planes.forEach {

            //Log.d("Planes", "Plane  ${it.row()},${it.col} with ${it.orientation().value}")
            savePlane(it)
        }
    }

    fun resetState() {
        m_SentMoves.clear()
        m_ReceivedMoves.clear()
        m_SendMovesCancelled.value = false
        m_SendingMoveState.value = false
        m_PollingForComputerMoves.value = false
        m_PreparedForGame.value = false
        m_RoundEnds.value = false
        m_StartNewRound.value = false
    }

    fun prepareForGame(planes: List<Plane>) {
        if (m_PreparedForGame.value == true)
            return

        m_PreparedForGame.value = true

        resetGrid()
        savePlanes(planes)

        computePlanePointsList()
        updatePlanesToPlaneRound()
    }

    fun sendMoves() {

        Log.d("Planes", "Send player moves")
        //set state sending move
        if (m_SendingMoveState.value == true || m_SendMovesCancelled.value == true) {
            Log.d("Planes", "Send moves not possible ${m_SendingMoveState.value} ${m_SendMovesCancelled.value} ")
            return;
        }

        updateRoundEnds()

        viewModelScope.launch {

            m_SendingMoveState.value = true

            val guessCount = getGuessesCount()

            //compute list of not sent moves
            var notSentMoves = Vector<Int>()

            for (i in 0..guessCount - 1) {
                if (!m_SentMoves.contains(i))
                    notSentMoves.add(i)
            }

            if (notSentMoves.isEmpty()) {
                m_SendingMoveState.value = false
                Log.d("Planes", "Not sent moves is empty")
                return@launch
            }

            //compute moves not received moves
            var notReceivedMoves = Vector<Int>()

            if (m_ReceivedMoves.isNotEmpty()) {
                for (i in 0..m_ReceivedMoves.get(m_ReceivedMoves.size - 1)) {
                    if (!m_ReceivedMoves.contains(i))
                        notReceivedMoves.add(i)
                }
            }

            //build request

            val request = createSendNotSentMovesRequest(notSentMoves, notReceivedMoves)

            //call repository method
            val result = withContext(Dispatchers.IO) {
                repository.sendOwnMove(
                    m_Authorization.value!!, request
                )
            }

            //check it was sent
            if (result.data != null) {
                m_SendMovesCancelled.value = result.data!!.m_Cancelled

                if (result.data!!.m_Cancelled)
                    return@launch

                notSentMoves.forEach {
                    m_SentMoves.add(it)
                }

                if (result.data!!.m_ListMoves.isNotEmpty()) {
                    for (move in result.data!!.m_ListMoves) {
                        //val gp = GuessPoint(move.m_MoveX, move.m_MoveY)
                        //val moveIdx = move.m_MoveIndex
                        Log.d(
                            "Planes",
                            "Received move ${move.m_MoveIndex} ${move.m_MoveX} ${move.m_MoveY}"
                        )

                        //TODO: and if not game ended
                        if (!m_PlaneRound.computerGuessAlreadyMade(move.m_MoveX, move.m_MoveY)) {
                            m_PlaneRound.addComputerMove(move.m_MoveX, move.m_MoveY)
                            m_ReceivedMoves.add(move.m_MoveIndex)
                            updateRoundEnds()
                        }
                    }

                    m_ReceivedMoves.sort()
                }
            }

            m_SendingMoveState.value = false
        }
    }

    fun pollForComputerMoves() {

        val winners = m_PlaneRound.checkIfRoundEnds()

        if (winners.first == false)
            return

        if (m_PollingForComputerMoves.value == true)
            return;

        m_PollingForComputerMoves.value = true

        viewModelScope.launch {
            withContext(Dispatchers.IO) {

                do {
                    delay(5.seconds)

                    val notSentMoves = Vector<Int>()

                    //compute moves not received moves
                    val notReceivedMoves = Vector<Int>()

                    if (m_ReceivedMoves.isNotEmpty()) {
                        for (i in 0..m_ReceivedMoves.get(m_ReceivedMoves.size - 1)) {
                            if (!m_ReceivedMoves.contains(i))
                                notReceivedMoves.add(i)
                        }
                    }

                    val request = createSendNotSentMovesRequest(notSentMoves, notReceivedMoves)

                    val result = repository.sendOwnMove(
                        m_Authorization.value!!, request
                    )

                    if (result.data != null) {
                        m_SendMovesCancelled.value = result.data!!.m_Cancelled
                        if (result.data!!.m_ListMoves.isNotEmpty()) {
                            for (move in result.data!!.m_ListMoves) {
                                //val gp = GuessPoint(move.m_MoveX, move.m_MoveY)
                                //val moveIdx = move.m_MoveIndex
                                Log.d(
                                    "Planes",
                                    "Received move ${move.m_MoveIndex} ${move.m_MoveX} ${move.m_MoveY}"
                                )

                                if (!m_PlaneRound.computerGuessAlreadyMade(move.m_MoveX, move.m_MoveY)) {
                                    m_PlaneRound.addComputerMove(move.m_MoveX, move.m_MoveY)
                                    m_ReceivedMoves.add(move.m_MoveIndex)
                                    updateRoundEnds()
                                }
                            }
                            m_ReceivedMoves.sort()
                        }
                    }

                    val pgr = m_PlaneRoundMultiplayer.checkRoundEndAsync()

                } while (m_SendMovesCancelled.value == false && !pgr.m_RoundEnds)

                m_PollingForComputerMoves.value = false
            }
        }

    }

    fun sendWinner(isPlayerWinner: Boolean, isDraw: Boolean) {

        viewModelScope.launch {
            val request = createSendWinnerRequest(isPlayerWinner, isDraw)

            val result = withContext(Dispatchers.IO) {
                repository.sendWinner(
                    m_Authorization.value!!, request
                )
            }
        }
    }

    fun startNewRound() {
        viewModelScope.launch {
            val request = createStartNewRoundRequest()

            val result = withContext(Dispatchers.IO) {
                repository.startNewRound(
                    m_Authorization.value!!, request
                )
            }

            if (result.data != null) {
                if (result.data!!.m_NewRoundCreated) {
                    m_RoundId.value = result.data!!.m_RoundId
                    m_StartNewRound.value = true
                } else {
                    Log.d("Planes", "New round was not created")
                }
            }
        }
    }

    fun createSendNotSentMovesRequest(notSentMoves: Vector<Int>, notReceivedMoves: Vector<Int>): SendNotSentMovesRequest {

        val notSentMovesWithPositions = Vector<SingleMoveRequest>()

        notSentMoves.forEach {
            val guess = getGuessAtIndex(it)
            if (guess != null) {
                notSentMovesWithPositions.add(SingleMoveRequest(it, guess.row, guess.col))
                Log.d("Planes", "Not sent $it ${guess.row} ${guess.col}")
            }
        }

        val opponentMoveIndex = if (m_ReceivedMoves.isNotEmpty()) m_ReceivedMoves.get(m_ReceivedMoves.size - 1) else 0

        return SendNotSentMovesRequest(m_GameId.value!!, m_RoundId.value!!, m_OpponentId.value!!,
            opponentMoveIndex, notSentMovesWithPositions,
            notReceivedMoves,
            m_UserId.value!!, m_UserName.value!!
        )
    }

    fun createSendWinnerRequest(isPlayerWiner: Boolean, isDraw: Boolean) : SendWinnerRequest {
        val winnerId = if (isPlayerWiner) m_UserId.value!! else m_OpponentId.value!!
        return SendWinnerRequest(m_GameId.value!!, m_RoundId.value!!,
            winnerId, isDraw, m_UserId.value!!, m_UserName.value!!)
    }

    fun createStartNewRoundRequest(): StartNewRoundRequest {
        return StartNewRoundRequest(m_GameId.value!!, m_OpponentId.value!!, m_UserId.value!!, m_UserName.value!!)
    }
}
package com.planes.android.screens.multiplayergame

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.planes.android.repository.PlanesGameRepository
import com.planes.android.screens.singleplayergame.PlaneGridViewModel
import com.planes.multiplayer_engine.requests.SendNotSentMovesRequest
import com.planes.multiplayer_engine.requests.SingleMoveRequest
import com.planes.multiplayerengine.MultiPlayerRoundInterface
import com.planes.singleplayerengine.GuessPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Vector
import javax.inject.Inject
import kotlin.text.get

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
    private var m_LastSentMoves = mutableStateListOf<Int>()
    private var m_ReceivedMoves = mutableStateListOf<Int>()
    private var m_SendMovesCancelled = mutableStateOf(false)

    private var m_SendingMoveState = mutableStateOf(false)

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

        //TODO: reset states
    }


    fun sendMoves() {

        Log.d("Planes", "Send player moves")
        //TODO: set state sending move
        if (m_SendingMoveState.value == true || m_SendMovesCancelled.value == true) {
            Log.d("Planes", "Send moves not possible ${m_SendingMoveState.value} ${m_SendMovesCancelled.value} ")
            return;
        }

        viewModelScope.launch {

            m_SendingMoveState.value = true

            val guessCount = getGuessesCount()

            //TODO: compute list of not sent moves
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

            //TODO: compute moves not received moves
            var notReceivedMoves = Vector<Int>()

            if (m_ReceivedMoves.isNotEmpty()) {
                for (i in 0..m_ReceivedMoves.get(m_ReceivedMoves.size - 1)) {
                    if (!m_ReceivedMoves.contains(i))
                        notReceivedMoves.add(i)
                }
            }

            //TODO: build request

            val request = createSendNotSentMovesRequest(notSentMoves, notReceivedMoves)

            //TODO: call repository method
            val result = withContext(Dispatchers.IO) {
                repository.sendOwnMove(
                    m_Authorization.value!!, request
                )
            }

            //TODO: check it was sent
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
                        }
                    }

                    m_ReceivedMoves.sort()
                }
            }

            m_SendingMoveState.value = false
        }
    }

    fun createSendNotSentMovesRequest(notSentMoves: Vector<Int>, notReceivedMoves: Vector<Int>): SendNotSentMovesRequest {

        var notSentMovesWithPositions = Vector<SingleMoveRequest>()

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
}
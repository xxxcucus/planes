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
import com.planes.multiplayer_engine.requests.SendPlanePositionsRequest
import com.planes.multiplayerengine.MultiPlayerRoundInterface
import com.planes.singleplayerengine.Orientation
import com.planes.singleplayerengine.Plane
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class PlayerGridViewModelMultiPlayer @Inject constructor(planeRound: MultiPlayerRoundInterface,
                                                         private val repository: PlanesGameRepository
): PlaneGridViewModel(planeRound,false) {

    private var m_BoardEditingState = mutableStateOf<BoardEditingStates>(BoardEditingStates.EditPlanePositions)

    private var m_Error = mutableStateOf<String?>("");

    //TODO: transform these into mutablestate
    private var m_Authorization = mutableStateOf<String?>(null)
    private var m_GameName = mutableStateOf<String?>(null)
    private var m_GameId = mutableStateOf<String?>(null)
    private var m_RoundId = mutableStateOf<String?>(null)
    private var m_UserName = mutableStateOf<String?>(null)
    private var m_UserId = mutableStateOf<String?>(null)
    private var m_OpponentId = mutableStateOf<String?>(null)

    private var m_SendPositionsCancelled = mutableStateOf<Boolean?>(null)
    private var m_SendPositionsOtherExists = mutableStateOf<Boolean?>(null)
    private var m_SendPositionsPlaneList = mutableStateListOf<Plane>()

    fun getBoardEditingState(): BoardEditingStates {
        return m_BoardEditingState.value
    }

    fun setBoardEditingState(value: BoardEditingStates) {
        m_BoardEditingState.value = value
    }

    fun getReceivedPlaneList(): List<Plane> {
        return m_SendPositionsPlaneList.toList()
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

        //TODO: reset states
    }

    fun createSendPlanePositionsRequest() : SendPlanePositionsRequest {

        val firstPlane = getPlane(0).second
        val secondPlane = getPlane(1).second
        val thirdPlane = getPlane(2).second

        return SendPlanePositionsRequest(m_GameId.value!!, m_RoundId.value!!, m_OpponentId.value!!,
            firstPlane.row(), firstPlane.col(), firstPlane.orientation().value,
            secondPlane.row(), secondPlane.col(), secondPlane.orientation().value,
            thirdPlane.row(), thirdPlane.col(), thirdPlane.orientation().value,
            m_UserId.value!!, m_UserName.value!!
            )
    }

    override fun cancelRound() {
        super.cancelRound()

        m_BoardEditingState.value = BoardEditingStates.Cancel
    }

    override fun doneEditing() {
        super.doneEditing()

        if (m_Authorization.value == null)
            return

        viewModelScope.launch {

            m_BoardEditingState.value = BoardEditingStates.SendOwnPlanePositions
            m_Error.value = null

            val result = withContext(Dispatchers.IO) {
                repository.sendPlanePositions(
                    m_Authorization.value!!,
                    createSendPlanePositionsRequest()
                )
            }

            if (result.data == null) {
                Log.d("PlanesCompose", "Sending own plane positions error ${result.e}")
                m_Error.value = result.e
            } else {

                m_SendPositionsCancelled.value = result.data!!.m_Cancelled

                if (m_SendPositionsCancelled.value == false) {
                    m_SendPositionsOtherExists.value = result.data!!.m_OtherExist

                    if (m_SendPositionsOtherExists.value == true) {

                        m_SendPositionsPlaneList.clear()
                        val firstPlane = Plane(result.data!!.m_Plane1X, result.data!!.m_Plane1Y, Orientation.fromInt(result.data!!.m_Plane1Orient))
                        m_SendPositionsPlaneList.add(firstPlane)
                        val secondPlane = Plane(result.data!!.m_Plane2X, result.data!!.m_Plane2Y, Orientation.fromInt(result.data!!.m_Plane2Orient))
                        m_SendPositionsPlaneList.add(secondPlane)
                        val thirdPlane = Plane(result.data!!.m_Plane3X, result.data!!.m_Plane3Y, Orientation.fromInt(result.data!!.m_Plane3Orient))
                        m_SendPositionsPlaneList.add(thirdPlane)

                        m_BoardEditingState.value = BoardEditingStates.OpponentPlanePositionsReceived
                    } else {
                        m_BoardEditingState.value = BoardEditingStates.WaitForOpponentPlanePositions
                    }
                } else if (m_SendPositionsCancelled.value == true) {
                    m_BoardEditingState.value = BoardEditingStates.Cancel
                }
            }

            if (m_BoardEditingState.value != BoardEditingStates.WaitForOpponentPlanePositions)
                return@launch

            withContext(Dispatchers.IO) {
                do {
                    delay(5.seconds)

                    if (m_BoardEditingState.value != BoardEditingStates.WaitForOpponentPlanePositions)
                        break

                    val resultPolling = repository.sendPlanePositions(
                        m_Authorization.value!!,
                        createSendPlanePositionsRequest()
                    )

                    if (resultPolling.data == null) {
                        Log.d("PlanesCompose", "Error when polling for opponent plane positions ${result.e}")
                        m_Error.value = result.e
                        break;
                    }

                    m_SendPositionsCancelled.value = resultPolling.data!!.m_Cancelled

                    if (m_SendPositionsCancelled.value == false) {
                        m_SendPositionsOtherExists.value = resultPolling.data!!.m_OtherExist

                        if (m_SendPositionsOtherExists.value == true) {

                            m_SendPositionsPlaneList.clear()
                            val firstPlane = Plane(resultPolling.data!!.m_Plane1X, resultPolling.data!!.m_Plane1Y, Orientation.fromInt(resultPolling.data!!.m_Plane1Orient))
                            m_SendPositionsPlaneList.add(firstPlane)
                            val secondPlane = Plane(resultPolling.data!!.m_Plane2X, resultPolling.data!!.m_Plane2Y, Orientation.fromInt(resultPolling.data!!.m_Plane2Orient))
                            m_SendPositionsPlaneList.add(secondPlane)
                            val thirdPlane = Plane(resultPolling.data!!.m_Plane3X, resultPolling.data!!.m_Plane3Y, Orientation.fromInt(resultPolling.data!!.m_Plane3Orient))
                            m_SendPositionsPlaneList.add(thirdPlane)

                            m_BoardEditingState.value = BoardEditingStates.OpponentPlanePositionsReceived
                        }
                    } else if (m_SendPositionsCancelled.value == true) {
                        m_BoardEditingState.value = BoardEditingStates.Cancel
                    }
                    Log.d("PlaneCompose", "Polling for planes positions ${m_SendPositionsOtherExists.value}")
                } while (m_BoardEditingState.value == BoardEditingStates.WaitForOpponentPlanePositions)
            }
        }
    }

}
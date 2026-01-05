package com.planes.android.screens.createmultiplayergame

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.planes.android.repository.PlanesGameRepository
import com.planes.multiplayer_engine.requests.ConnectToGameRequest
import com.planes.multiplayer_engine.requests.GameStatusRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CreateViewModel @Inject constructor(private val repository: PlanesGameRepository): ViewModel() {

    private var m_GameName = mutableStateOf("")
    private var m_Loading = mutableStateOf(false)
    private var m_Error = mutableStateOf<String?>(null)

    private var m_CreateState = mutableStateOf<CreateGameStates>(CreateGameStates.StatusNotRequested)

    private var m_StatusGameId = mutableStateOf<String?>(null)
    private var m_StatusGameName = mutableStateOf<String?>(null)
    private var m_StatusExists = mutableStateOf<Boolean?>(null)
    private var m_StatusFirstPlayerName = mutableStateOf<String?>(null)
    private var m_StatusFirstPlayerId = mutableStateOf<String?>(null)
    private var m_StatusSecondPlayerName = mutableStateOf<String?>(null)
    private var m_StatusSecondPlayerId = mutableStateOf<String?>(null)
    private var m_StatusCurrentRoundId = mutableStateOf<String?>(null)

    private var m_ConnectedGameId = mutableStateOf<String?>(null)
    private var m_ConnectedGameName = mutableStateOf<String?>(null)
    private var m_ConnectedExists = mutableStateOf<Boolean?>(null)
    private var m_ConnectedFirstPlayerName = mutableStateOf<String?>(null)
    private var m_ConnectedFirstPlayerId = mutableStateOf<String?>(null)
    private var m_ConnectedSecondPlayerName = mutableStateOf<String?>(null)
    private var m_ConnectedSecondPlayerId = mutableStateOf<String?>(null)
    private var m_ConnectedCurrentRoundId = mutableStateOf<String?>(null)


    fun getGameName(): String {
        return m_GameName.value
    }

    fun setGameName(value: String) {
        m_GameName.value = value
    }

    fun getLoading(): Boolean {
        return m_Loading.value
    }

    fun setLoading(value: Boolean) {
        m_Loading.value = value
    }

    fun getCreateState(): CreateGameStates {
        return m_CreateState.value
    }

    fun setCreateState(value: CreateGameStates) {
        m_CreateState.value = value
    }

    fun getStatusExists(): Boolean? {
        return m_StatusExists.value
    }

    fun setStatusExists(value: Boolean) {
        m_StatusExists.value = value
    }

    fun getStatusGameId(): String? {
        return m_StatusGameId.value
    }

    fun setStatusGameId(value: String?) {
        m_StatusGameId.value = value
    }

    fun getStatusGameName(): String? {
        return m_StatusGameName.value
    }

    fun setStatusGameName(value: String?) {
        m_StatusGameName.value = value
    }

    fun getStatusFirstPlayerName(): String? {
        return m_StatusFirstPlayerName.value
    }

    fun setStatusFirstPlayerName(value: String?) {
        m_StatusFirstPlayerName.value = value
    }

    fun getStatusFirstPlayerId(): String? {
        return m_StatusFirstPlayerId.value
    }

    fun setStatusFirstPlayerId(value: String?) {
        m_StatusFirstPlayerId.value = value
    }

    fun getStatusSecondPlayerName(): String? {
        return m_StatusSecondPlayerName.value
    }

    fun setStatusSecondPlayerName(value: String?) {
        m_StatusSecondPlayerName.value = value
    }

    fun getStatusSecondPlayerId(): String? {
        return m_StatusSecondPlayerId.value
    }

    fun setStatusSecondPlayerId(value: String?) {
        m_StatusSecondPlayerId.value = value
    }

    fun getStatusCurrentRoundId(): String? {
        return m_StatusCurrentRoundId.value
    }

    fun setStatusCurrentRoundId(value: String?) {
        m_StatusCurrentRoundId.value = value
    }

    fun getConnectedExists(): Boolean? {
        return m_ConnectedExists.value
    }

    fun setConnectedExists(value: Boolean) {
        m_ConnectedExists.value = value
    }

    fun getConnectedGameId(): String? {
        return m_ConnectedGameId.value
    }

    fun setConnectedGameId(value: String?) {
        m_ConnectedGameId.value = value
    }

    fun getConnectedGameName(): String? {
        return m_ConnectedGameName.value
    }

    fun setConnectedGameName(value: String?) {
        m_ConnectedGameName.value = value
    }

    fun getConnectedFirstPlayerName(): String? {
        return m_ConnectedFirstPlayerName.value
    }

    fun setConnectedFirstPlayerName(value: String?) {
        m_ConnectedFirstPlayerName.value = value
    }

    fun getConnectedFirstPlayerId(): String? {
        return m_ConnectedFirstPlayerId.value
    }

    fun setConnectedFirstPlayerId(value: String?) {
        m_ConnectedFirstPlayerId.value = value
    }

    fun getConnectedSecondPlayerName(): String? {
        return m_ConnectedSecondPlayerName.value
    }

    fun setConnectedSecondPlayerName(value: String?) {
        m_ConnectedSecondPlayerName.value = value
    }

    fun getConnectedSecondPlayerId(): String? {
        return m_ConnectedSecondPlayerId.value
    }

    fun setConnectedSecondPlayerId(value: String?) {
        m_ConnectedSecondPlayerId.value = value
    }

    fun getConnectedCurrentRoundId(): String? {
        return m_ConnectedCurrentRoundId.value
    }

    fun setConnectedCurrentRoundId(value: String?) {
        m_ConnectedCurrentRoundId.value = value
    }


    fun getError(): String? {
        return m_Error.value
    }

    fun setError(value: String?) {
        m_Error.value = value
    }

    fun gameStatus(authorization: String, userid: String, username: String) {
        viewModelScope.launch {
            m_Loading.value = true
            m_Error.value = null
            val result = withContext(Dispatchers.IO) {
                repository.gameStatus(authorization, GameStatusRequest(getGameName(), username, userid, ""))
            }

            m_CreateState.value = CreateGameStates.StatusRequested

            if (result.data == null) {
                Log.d("PlanesCompose", "Game Status error ${result.e}")
                m_Error.value = result.e
            } else {
                m_StatusExists.value = result.data?.m_Exists
                m_StatusGameId.value = result.data?.m_GameId
                m_StatusGameName.value = result.data?.m_GameName
                m_StatusFirstPlayerName.value = result.data?.m_FirstPlayerName
                m_StatusFirstPlayerId.value = result.data?.m_FirstPlayerId
                m_StatusSecondPlayerName.value = result.data?.m_SecondPlayerName
                m_StatusSecondPlayerId.value = result.data?.m_SecondPlayerId
                m_StatusCurrentRoundId.value = result.data?.m_CurrentRoundId

                m_CreateState.value = CreateGameStates.StatusReceived

                if (m_StatusExists.value == false)
                    Log.d("PlanesCompose", "Game ${m_GameName.value} does not exist")
                else if (m_StatusExists.value == true)
                    Log.d("PlanesCompose", "Game ${m_GameName.value} exists with id ${m_StatusGameId.value}")
                else
                    Log.d("PlanesCompose", "Game data not available")
            }
            m_Loading.value = result.loading!!
        }
    }

    fun connectToGame(authorization: String, userid: String, username: String) {
        viewModelScope.launch {
            m_Loading.value = true
            m_Error.value = null
            val result = withContext(Dispatchers.IO) {
                repository.connectToGame(authorization, ConnectToGameRequest(getStatusGameName()!!, username, userid, getStatusGameId()!!))
            }

            m_CreateState.value = CreateGameStates.ConnectedToGameRequested

            if (result.data == null) {
                Log.d("PlanesCompose", "Game Connect error ${result.e}")
                m_Error.value = result.e
            } else {
                m_ConnectedExists.value = result.data?.m_Exists
                m_ConnectedGameId.value = result.data?.m_GameId
                m_ConnectedGameName.value = result.data?.m_GameName
                m_ConnectedFirstPlayerName.value = result.data?.m_FirstPlayerName
                m_ConnectedFirstPlayerId.value = result.data?.m_FirstPlayerId
                m_ConnectedSecondPlayerName.value = result.data?.m_SecondPlayerName
                m_ConnectedSecondPlayerId.value = result.data?.m_SecondPlayerId
                m_ConnectedCurrentRoundId.value = result.data?.m_CurrentRoundId

                m_CreateState.value = CreateGameStates.ConnectedComplete

                if (m_ConnectedExists.value == false)
                    Log.d("PlanesCompose", "Game ${m_GameName.value} does not exist")
                else if (m_ConnectedExists.value == true)
                    Log.d("PlanesCompose", "Connected to Game ${m_GameName.value}")
                else
                    Log.d("PlanesCompose", "Game data not available")
            }
            m_Loading.value = result.loading!!
        }
    }

    fun dataAvailable(): Boolean {
        if (m_StatusExists.value == null)
            return false

        if (m_StatusExists.value == false)
            return true

        if (m_StatusGameId.value == null || m_StatusGameName.value == null)
            return false

        if (m_StatusFirstPlayerId.value == null || m_StatusFirstPlayerName.value == null)
            return false

        if (m_StatusSecondPlayerId.value == null || m_StatusSecondPlayerName.value == null)
            return false

        if (m_StatusCurrentRoundId.value == null)
            return false

        return true
    }

}
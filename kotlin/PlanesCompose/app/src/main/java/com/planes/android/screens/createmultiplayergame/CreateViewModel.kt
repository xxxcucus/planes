package com.planes.android.screens.createmultiplayergame

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.planes.android.repository.PlanesGameRepository
import com.planes.multiplayer_engine.requests.ConnectToGameRequest
import com.planes.multiplayer_engine.requests.CreateGameRequest
import com.planes.multiplayer_engine.requests.GameStatusRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class CreateViewModel @Inject constructor(private val repository: PlanesGameRepository): ViewModel() {

    private var m_GameName = mutableStateOf("")
    private var m_Loading = mutableStateOf(false)
    private var m_Error = mutableStateOf<String?>(null)

    private var m_CreateState = mutableStateOf<CreateGameStates>(CreateGameStates.StatusNotRequested)

    private var m_GameStatusMap = HashMap<String, GameStatus>()


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

    fun getExists(key: String): Boolean? {
        return m_GameStatusMap[key]?.getExists()
    }

    fun setExists(key: String, value: Boolean?) {
        m_GameStatusMap[key]?.setExists(value)
    }

    fun getGameId(key: String): String? {
        return m_GameStatusMap[key]?.getGameId()
    }

    fun getGameIdState(key: String): MutableState<String?> {
        if (m_GameStatusMap.containsKey(key))
            return m_GameStatusMap[key]?.getGameIdState()!!
        else
            return mutableStateOf<String?>(null)
    }

    fun getGameIdState(): MutableState<String?> {
        if (dataAvailable("Create"))
            return getGameIdState("Create");
        else if (dataAvailable("Connect"))
            return getGameIdState("Connect")
        else
            return mutableStateOf<String?>(null);
    }

    fun setGameId(key: String, value: String?) {
        m_GameStatusMap[key]?.setGameId(value)
    }

    fun getGameName(key: String): String? {
        return m_GameStatusMap[key]?.getGameName()
    }

    fun getGameNameState(key: String): MutableState<String?> {
        if (m_GameStatusMap.containsKey(key))
            return m_GameStatusMap[key]?.getGameNameState()!!
        else
            return mutableStateOf<String?>(null)
    }

    fun getGameNameState(): MutableState<String?> {
        if (dataAvailable("Create"))
            return getGameNameState("Create");
        else if (dataAvailable("Connect"))
            return getGameNameState("Connect")
        else
            return mutableStateOf<String?>(null);
    }

    fun setGameName(key: String, value: String?) {
        m_GameStatusMap[key]?.setGameName(value)
    }

    fun getFirstPlayerName(key: String): String? {
        return m_GameStatusMap[key]?.getFirstPlayerName()
    }

    fun getFirstPlayerNameState(): MutableState<String?> {
        if (dataAvailable("Create"))
            return getFirstPlayerNameState("Create");
        else if (dataAvailable("Connect"))
            return getFirstPlayerNameState("Connect")
        else
            return mutableStateOf<String?>(null);
    }

    fun getFirstPlayerNameState(key: String): MutableState<String?> {
        if (m_GameStatusMap.containsKey(key))
            return m_GameStatusMap[key]?.getFirstPlayerNameState()!!
        else
            return mutableStateOf<String?>(null)
    }

    fun setFirstPlayerName(key: String, value: String?) {
        m_GameStatusMap[key]?.setFirstPlayerName(value)
    }

    fun getFirstPlayerId(key: String): String? {
        return m_GameStatusMap[key]?.getFirstPlayerId()
    }

    fun getFirstPlayerIdState(): MutableState<String?> {
        if (dataAvailable("Create"))
            return getFirstPlayerIdState("Create");
        else if (dataAvailable("Connect"))
            return getFirstPlayerIdState("Connect")
        else
            return mutableStateOf<String?>(null);
    }

    fun getFirstPlayerIdState(key: String): MutableState<String?> {
        if (m_GameStatusMap.containsKey(key))
            return m_GameStatusMap[key]?.getFirstPlayerIdState()!!
        else
            return mutableStateOf<String?>(null)
    }

    fun setFirstPlayerId(key: String, value: String?) {
        m_GameStatusMap[key]?.setFirstPlayerId(value)
    }

    fun getSecondPlayerName(key: String): String? {
        return m_GameStatusMap[key]?.getSecondPlayerName()
    }

    fun getSecondPlayerNameState(): MutableState<String?> {
        if (dataAvailable("Create"))
            return getSecondPlayerNameState("Create");
        else if (dataAvailable("Connect"))
            return getSecondPlayerNameState("Connect")
        else
            return mutableStateOf<String?>(null);
    }

    fun getSecondPlayerNameState(key: String): MutableState<String?> {
        if (m_GameStatusMap.containsKey(key))
            return m_GameStatusMap[key]?.getSecondPlayerNameState()!!
        else
            return mutableStateOf<String?>(null)
    }

    fun setSecondPlayerName(key: String, value: String?) {
        m_GameStatusMap[key]?.setSecondPlayerName(value)
    }

    fun getSecondPlayerId(key: String): String? {
        return m_GameStatusMap[key]?.getSecondPlayerId()
    }

    fun getSecondPlayerIdState(): MutableState<String?> {
        if (dataAvailable("Create"))
            return getSecondPlayerIdState("Create");
        else if (dataAvailable("Connect"))
            return getSecondPlayerIdState("Connect")
        else
            return mutableStateOf<String?>(null);
    }

    fun getSecondPlayerIdState(key: String): MutableState<String?> {
        if (m_GameStatusMap.containsKey(key))
            return m_GameStatusMap[key]?.getSecondPlayerIdState()!!
        else
            return mutableStateOf<String?>(null)
    }

    fun setSecondPlayerId(key: String, value: String?) {
        m_GameStatusMap[key]?.setSecondPlayerId(value)
    }

    fun getCurrentRoundId(key: String): String? {
        return m_GameStatusMap[key]?.getCurrentRoundId()
    }

    fun getCurrentRoundIdState(): MutableState<String?> {
        if (dataAvailable("Create"))
            return getCurrentRoundIdState("Create");
        else if (dataAvailable("Connect"))
            return getCurrentRoundIdState("Connect")
        else
            return mutableStateOf<String?>(null);
    }

    fun getCurrentRoundIdState(key: String): MutableState<String?> {
        if (m_GameStatusMap.containsKey(key))
            return m_GameStatusMap[key]?.getCurrentRoundIdState()!!
        else
            return mutableStateOf<String?>(null)
    }

    fun setCurrentRoundId(key: String, value: String?) {
        m_GameStatusMap[key]?.setCurrentRoundId(value)
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

            m_CreateState.value = CreateGameStates.StatusRequested

            //TODO: reset m_GameStatus

            val result = withContext(Dispatchers.IO) {
                repository.gameStatus(authorization, GameStatusRequest(getGameName(), username, userid, ""))
            }

            if (result.data == null) {
                Log.d("PlanesCompose", "Game Status error ${result.e}")
                m_Error.value = result.e
            } else {
                val key = "Status"

                if (!m_GameStatusMap.containsKey(key))
                    m_GameStatusMap.put(key, GameStatus())

                Log.d("PlanesCompose", "Game Status exists ${result.data?.m_Exists}")

                setExists(key, result.data?.m_Exists)
                setGameId(key, result.data?.m_GameId)
                setGameName(key, result.data?.m_GameName)
                setFirstPlayerName(key, result.data?.m_FirstPlayerName)
                setFirstPlayerId(key, result.data?.m_FirstPlayerId)
                setSecondPlayerName(key, result.data?.m_SecondPlayerName)
                setSecondPlayerId(key, result.data?.m_SecondPlayerId)
                setCurrentRoundId(key, result.data?.m_CurrentRoundId)

                m_CreateState.value = CreateGameStates.StatusReceived

                if (getExists(key) == false)
                    Log.d("PlanesCompose", "Game ${m_GameName.value} does not exist")
                else if (getExists(key) == true)
                    Log.d("PlanesCompose", "Game ${m_GameName.value} exists with id ${getGameId("Status")}")
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

            m_CreateState.value = CreateGameStates.ConnectedToGameRequested

            val result = withContext(Dispatchers.IO) {
                repository.connectToGame(authorization, ConnectToGameRequest(getGameName("Status")!!, username, userid, getGameId("Status")!!))
            }

            if (result.data == null) {
                Log.d("PlanesCompose", "Game Connect error ${result.e}")
                m_Error.value = result.e
            } else {
                val key = "Connect"

                if (!m_GameStatusMap.containsKey(key))
                    m_GameStatusMap.put(key, GameStatus())

                setExists(key, result.data?.m_Exists)
                setGameId(key, result.data?.m_GameId)
                setGameName(key, result.data?.m_GameName)
                setFirstPlayerName(key, result.data?.m_FirstPlayerName)
                setFirstPlayerId(key, result.data?.m_FirstPlayerId)
                setSecondPlayerName(key, result.data?.m_SecondPlayerName)
                setSecondPlayerId(key, result.data?.m_SecondPlayerId)
                setCurrentRoundId(key, result.data?.m_CurrentRoundId)

                m_CreateState.value = CreateGameStates.ConnectedComplete

                if (getExists(key) == false)
                    Log.d("PlanesCompose", "Game ${m_GameName.value} does not exist")
                else if (getExists(key) == true)
                    Log.d("PlanesCompose", "Connected to Game ${m_GameName.value}")
                else
                    Log.d("PlanesCompose", "Game data not available")
            }
            m_Loading.value = result.loading!!
        }
    }

    //TODO: set maximum time for running coroutine
    fun createGame(authorization: String, userid: String, username: String) {
        viewModelScope.launch {
            m_Loading.value = true
            m_Error.value = null

            m_CreateState.value = CreateGameStates.GameCreationRequested

            Log.d(
                "PlanesCompose",
                "Create game ${getGameName()} $username $userid ${getGameId("Status")!!}"
            )

            val result = withContext(Dispatchers.IO) {
                repository.createGame(
                    authorization,
                    CreateGameRequest(getGameName(), username, userid, "0")
                )
            }

            if (result.data == null) {
                Log.d("PlanesCompose", "Game Creation error ${result.e}")
                m_Error.value = result.e
                return@launch
            } else {
                val key = "Create"

                if (!m_GameStatusMap.containsKey(key))
                    m_GameStatusMap.put(key, GameStatus())

                setExists(key, result.data?.m_Exists)
                setGameId(key, result.data?.m_GameId)
                setGameName(key, result.data?.m_GameName)
                setFirstPlayerName(key, result.data?.m_FirstPlayerName)
                setFirstPlayerId(key, result.data?.m_FirstPlayerId)
                setSecondPlayerName(key, result.data?.m_SecondPlayerName)
                setSecondPlayerId(key, result.data?.m_SecondPlayerId)
                setCurrentRoundId(key, result.data?.m_CurrentRoundId)

                m_CreateState.value = CreateGameStates.GameCreationComplete

                if (getExists(key) == false)
                    Log.d("PlanesCompose", "Game ${m_GameName.value} does not exist")
                else if (getExists(key) == true)
                    Log.d("PlanesCompose", "Game created ${m_GameName.value}")
                else
                    Log.d("PlanesCompose", "Game data not available")
            }
            m_Loading.value = result.loading!!

            m_CreateState.value = CreateGameStates.PollingForConnectionStarted
            m_Loading.value = true

            //TODO: stop this when pausing the app
            //TODO: stop this when the state is different
            //TODO: stop this when logging out or logging in again
            //TODO: stop this when run for too long
            withContext(Dispatchers.IO) {
                var firstPlayerName = getFirstPlayerName("Create")
                val secondPlayerName = getFirstPlayerName("Create")

                do {
                    delay(5.seconds)

                    if (m_CreateState.value != CreateGameStates.PollingForConnectionStarted)
                        break

                    val resultPolling = repository.gameStatus(
                        authorization,
                        GameStatusRequest(getGameName(), username, userid, "")
                    )

                    if (resultPolling.data == null) {
                        m_Error.value = result.e
                        break;
                    }

                    val key = "Create"

                    if (!m_GameStatusMap.containsKey(key))
                        m_GameStatusMap.put(key, GameStatus())

                    setExists(key, resultPolling.data?.m_Exists)
                    setGameId(key, resultPolling.data?.m_GameId)
                    setGameName(key, resultPolling.data?.m_GameName)
                    setFirstPlayerName(key, resultPolling.data?.m_FirstPlayerName)
                    setFirstPlayerId(key, resultPolling.data?.m_FirstPlayerId)
                    setSecondPlayerName(key, resultPolling.data?.m_SecondPlayerName)
                    setSecondPlayerId(key, resultPolling.data?.m_SecondPlayerId)
                    setCurrentRoundId(key, resultPolling.data?.m_CurrentRoundId)

                    Log.d("PlaneCompose", "Polling ${getFirstPlayerName("Create")} and ${getSecondPlayerName("Create")}")
                } while (getFirstPlayerName("Create") == getSecondPlayerName("Create"))
            }

            m_CreateState.value = CreateGameStates.PollingForConnectionEnded

            m_Loading.value = false
        }
    }



    fun dataAvailable(key: String): Boolean {
        if (getExists(key) == null)
            return false

        if (getExists(key) == false)
            return true

        if (getGameId(key) == null || getGameName(key) == null)
            return false

        if (getFirstPlayerId(key) == null || getFirstPlayerName(key) == null)
            return false

        if (getSecondPlayerId(key) == null || getSecondPlayerName(key) == null)
            return false

        if (getCurrentRoundId(key) == null)
            return false

        return true
    }

    fun gameConnectionExists(): Boolean {
        return dataAvailable("Create") || dataAvailable("Connect")
    }

}
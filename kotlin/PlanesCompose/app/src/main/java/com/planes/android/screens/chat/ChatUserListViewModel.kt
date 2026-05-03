package com.planes.android.screens.chat

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.planes.android.repository.PlanesGameRepository
import com.planes.android.repository.PlanesUserRepository
import com.planes.android.screens.createmultiplayergame.CreateGameStates
import com.planes.android.screens.createmultiplayergame.GameStatus
import com.planes.multiplayer_engine.requests.GameStatusRequest
import com.planes.multiplayer_engine.requests.PlayersListRequest
import com.planes.multiplayer_engine.responses.PlayersListResponse
import com.planes.multiplayer_engine.responses.UserWithLastLoginResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class ChatUserListViewModel @Inject constructor(private val repository: PlanesUserRepository): ViewModel() {


    private val _playersListState = MutableStateFlow<List<UserWithLastLoginResponse>?>(null)
    val m_PlayersListState: StateFlow<List<UserWithLastLoginResponse>?> = _playersListState

    val m_PollingStartedState = mutableStateOf(false)
    val m_StopPollingState = mutableStateOf(false)

    fun getPlayersList(): StateFlow<List<UserWithLastLoginResponse>?> {
        return m_PlayersListState
    }

    fun setPollingStop(value: Boolean) {
        m_StopPollingState.value = value
    }

    //TODO: start on login
    //TODO: stop on logout
    fun pollForPlayersList(authorization: String, userid: String, username: String) {

        if (m_PollingStartedState.value)
            return

        m_PollingStartedState.value = true
        m_StopPollingState.value = false

        viewModelScope.launch {
            do {
                delay(5.seconds)

                val resultPolling = repository.getPlayersList(
                    authorization,
                    PlayersListRequest(userid, username, 90)
                )

                if (resultPolling.data != null) {
                    _playersListState.update {
                        resultPolling.data!!.m_Usernames
                    }
                }

            } while (!m_StopPollingState.value)

            m_PollingStartedState.value = false
        }

    }




}
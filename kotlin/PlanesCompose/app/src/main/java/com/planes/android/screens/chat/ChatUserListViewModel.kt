package com.planes.android.screens.chat

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.planes.android.data.ChatMessage
import com.planes.android.data.NewMessagesFlag
import com.planes.android.repository.ChatDbRepository
import com.planes.android.repository.NewMessagesDbRepository
import com.planes.android.repository.PlanesUserRepository
import com.planes.android.utils.DateTimeUtils
import com.planes.multiplayer_engine.requests.PlayersListRequest
import com.planes.multiplayer_engine.requests.ReceiveChatMessagesRequest
import com.planes.multiplayer_engine.responses.UserWithLastLoginResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.Instant
import java.util.Date
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class ChatUserListViewModel @Inject constructor(private val userRepository: PlanesUserRepository,
                                                private val chatRepository: ChatDbRepository,
                                                private val newMessagesRepository: NewMessagesDbRepository): ViewModel() {


    private val _playersListState = MutableStateFlow<List<UserWithLastLoginResponse>?>(null)
    val m_PlayersListState: StateFlow<List<UserWithLastLoginResponse>?> = _playersListState

    val m_PollingStartedState = mutableStateOf(false)
    val m_StopPollingState = mutableStateOf(false)

    private val m_PollForChatMessages = mutableStateOf(false)

    private val _MessagesFlagsList = MutableStateFlow<List<NewMessagesFlag>>(emptyList())
    private val m_MessagesFlagsList : StateFlow<List<NewMessagesFlag>> = _MessagesFlagsList

    private val m_PollForMessagesFlags = mutableStateOf(false)

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

                val resultPolling = withContext(Dispatchers.IO)  {
                    userRepository.getPlayersList(
                        authorization,
                        PlayersListRequest(userid, username, 90)
                    )
                }

                if (resultPolling.data != null) {
                    _playersListState.update {
                        resultPolling.data!!.m_Usernames
                    }
                }

                delay(30.seconds)

            } while (!m_StopPollingState.value)

            m_PollingStartedState.value = false
        }
    }


//TODO: delete old chat messages

    fun pollForChatMessages(authorization: String, userid: String, username: String) {
        if (m_PollForChatMessages.value == true)
            return

        m_PollForChatMessages.value = true
        m_StopPollingState.value = false

        val receiveChatMessagesRequest = ReceiveChatMessagesRequest(userid, username)

        viewModelScope.launch {
            do {
                delay(15.seconds)

                val resultPolling = withContext(Dispatchers.IO) {
                    userRepository.getChatMessages(
                        authorization,
                        receiveChatMessagesRequest
                    )
                }

                if (resultPolling.data != null) {
                    val receiveChatMessagesResponse = resultPolling.data!!

                    for (message in receiveChatMessagesResponse.m_Messages) {

                        var msgDate = DateTimeUtils.parseDate(message.m_CreatedAt)
                        if (msgDate == null)
                            msgDate = Date.from(Instant.now())

                        chatRepository.addChatMessage(
                            message.m_SenderId.toLong() , message.m_SenderName,
                            message.m_Message, msgDate,
                            userid.toLong(), username,
                            userid.toLong() , username,
                        )
                        newMessagesRepository.updateNewMessagesFlags(message.m_SenderName, message.m_SenderId.toLong(),
                            username, userid.toLong(), true)
                    }
                }

            } while (!m_StopPollingState.value)
        }
    }


    fun setMessagesUnread(senderName: String, senderId: Long, receiverName: String, receiverId: Long) {
        viewModelScope.launch {
            newMessagesRepository.updateNewMessagesFlags(senderName, senderId, receiverName, receiverId, false)
        }
    }

    fun pollForNewMessagesFlags()  {

        if (m_PollForMessagesFlags.value == true)
            return

        m_PollForMessagesFlags.value = true
        m_StopPollingState.value = false
        viewModelScope.launch {

            do {
                delay(15.seconds)

                withContext(Dispatchers.IO) {
                    newMessagesRepository.getNewMessagesFlags().collect { listOfMessagesFlags ->
                        if (listOfMessagesFlags.isEmpty()) {
                            _MessagesFlagsList.value = emptyList()
                        } else {
                            _MessagesFlagsList.value = listOfMessagesFlags
                        }
                    }
                }
            } while(!m_StopPollingState.value)
        }
    }


    fun getNewMessagesFlags() : StateFlow<List<NewMessagesFlag>> {
        return m_MessagesFlagsList
    }

}
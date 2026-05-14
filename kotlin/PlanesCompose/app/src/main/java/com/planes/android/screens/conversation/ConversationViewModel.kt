package com.planes.android.screens.conversation

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.planes.android.data.ChatMessage
import com.planes.android.repository.ChatDbRepository
import com.planes.android.repository.PlanesUserRepository
import com.planes.android.utils.DateTimeUtils
import com.planes.multiplayer_engine.requests.PlayersListRequest
import com.planes.multiplayer_engine.requests.ReceiveChatMessagesRequest
import com.planes.multiplayer_engine.requests.SendChatMessageRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import java.util.Date
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class ConversationViewModel @Inject constructor(private val chatRepository: ChatDbRepository,
    private val userRepository: PlanesUserRepository): ViewModel() {

    val m_TextToSend : MutableState<String> = mutableStateOf("")
    private var m_Authorization = mutableStateOf<String?>(null)
    private var m_UserName = mutableStateOf<String?>(null)
    private var m_UserId = mutableStateOf<String?>(null)

    private var m_ChatPartnerId = mutableStateOf<String?>(null)

    private var m_ChatPartnerUsername = mutableStateOf<String?>(null)

    private val _MessagesList = MutableStateFlow<List<ChatMessage>>(emptyList())
    private val m_MessagesList : StateFlow<List<ChatMessage>> = _MessagesList

    private val m_MessageIdx = mutableStateOf(0L)

    private val m_PollForChatMessages = mutableStateOf(false)

    fun setTextToSend(text: String) {
        m_TextToSend.value = text
    }

    fun getTextToSend(): String {
        return m_TextToSend.value
    }

    fun getMessagesList() : StateFlow<List<ChatMessage>> {
        return m_MessagesList
    }

    fun setCredentials(authorization: MutableState<String?>,
                       username: MutableState<String?>, userid: MutableState<String?>,
                       chatPartnerId: String, chatPartnerUsername: String) {
        m_Authorization = authorization

        m_UserName = username
        m_UserId = userid

        m_ChatPartnerId.value = chatPartnerId
        m_ChatPartnerUsername.value = chatPartnerUsername

    }

    fun sendMessage() {

        if (m_TextToSend.value.trim().isEmpty())
            return

        val message = m_TextToSend.value.trim()

        val sendChatMessageRequest = SendChatMessageRequest(m_ChatPartnerId.value!!,
            message, m_MessageIdx.value.toString(),m_UserId.value!!, m_UserName.value!!
            )

        m_MessageIdx.value += 1

        viewModelScope.launch {

            chatRepository.addChatMessage(
                m_UserId.value?.toLong()!! , m_UserName.value!!,
                message, Date.from(Instant.now()),
                m_ChatPartnerId.value?.toLong()!!, m_ChatPartnerUsername.value!!,
                m_UserId.value?.toLong()!! , m_UserName.value!!,
                )

            userRepository.sendChatMessage(m_Authorization.value!!, sendChatMessageRequest)
        }
    }

    fun resetMessage() {
        m_TextToSend.value = ""
    }

    fun updateChatMessagesFromDb() {

        viewModelScope.launch {

            chatRepository.getMessages(
                m_UserName.value!!, m_UserId.value?.toLong()!!,
                m_ChatPartnerUsername.value!!, m_ChatPartnerId.value?.toLong()!!,
                m_UserName.value!!, m_UserId.value?.toLong()!!).
                    collect {
                        listOfMessages ->
                        if (listOfMessages.isEmpty()) {
                            _MessagesList.value = emptyList()
                        } else {
                            _MessagesList.value = listOfMessages
                        }
                    }
        }

    }

    //TODO: poll for messages from chat partner

    fun pollForChatMessages() {
        if (m_PollForChatMessages.value == true)
            return

        m_PollForChatMessages.value = true

        val receiveChatMessagesRequest = ReceiveChatMessagesRequest(m_UserId.value!!, m_UserName.value!!)

        viewModelScope.launch {
            do {
                delay(5.seconds)

                val resultPolling = userRepository.getChatMessages(m_Authorization.value!!,
                    receiveChatMessagesRequest
                    )

                if (resultPolling.data != null) {
                    val receiveChatMessagesResponse = resultPolling.data!!

                    for (message in receiveChatMessagesResponse.m_Messages) {

                        var msgDate = DateTimeUtils.parseDate(message.m_CreatedAt)
                        if (msgDate == null)
                            msgDate = Date.from(Instant.now())

                        chatRepository.addChatMessage(
                            message.m_SenderId.toLong() , message.m_SenderName,
                            message.m_Message, msgDate,
                            m_UserId.value?.toLong()!!, m_UserName.value!!,
                            m_UserId.value?.toLong()!! , m_UserName.value!!,
                        )

                        //TODO: update displayed messages
                    }

                }

            } while (m_PollForChatMessages.value)
        }
    }


}
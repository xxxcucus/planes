package com.planes.android.screens.conversation

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.planes.android.data.ChatMessage
import com.planes.android.repository.ChatDbRepository
import com.planes.android.repository.PlanesUserRepository
import com.planes.multiplayer_engine.requests.PlayersListRequest
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
class ConversationViewModel @Inject constructor(private val chatRepository: ChatDbRepository): ViewModel() {

    val m_TextToSend : MutableState<String> = mutableStateOf("")
    private var m_Authorization = mutableStateOf<String?>(null)
    private var m_UserName = mutableStateOf<String?>(null)
    private var m_UserId = mutableStateOf<String?>(null)

    private var m_ChatPartnerId = mutableStateOf<String?>(null)

    private var m_ChatPartnerUsername = mutableStateOf<String?>(null)

    private val _MessagesList = MutableStateFlow<List<ChatMessage>>(emptyList())
    private val m_MessagesList : StateFlow<List<ChatMessage>> = _MessagesList


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

        viewModelScope.launch {

            chatRepository.addChatMessage(
                m_UserId.value?.toLong()!! , m_UserName.value!!,
                m_TextToSend.value.trim(), Date.from(Instant.now()),
                m_ChatPartnerId.value?.toLong()!!, m_ChatPartnerUsername.value!!,
                m_UserId.value?.toLong()!! , m_UserName.value!!,
                )
        }
    }

    fun resetMessage() {
        m_TextToSend.value = ""
    }

    fun updateChatMessagesFromDb() {

        viewModelScope.launch {

            chatRepository.getMessages(
                m_UserName.value.toString(), m_UserId.value?.toLong()!!,
                m_ChatPartnerUsername.value!!, m_ChatPartnerId.value?.toLong()!!,
                m_UserName.value.toString(), m_UserId.value?.toLong()!!).
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
}
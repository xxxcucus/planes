package com.planes.android.screens.conversation

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.planes.android.data.ChatMessage
import com.planes.android.repository.ChatDbRepository
import com.planes.android.repository.PlanesUserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

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
}
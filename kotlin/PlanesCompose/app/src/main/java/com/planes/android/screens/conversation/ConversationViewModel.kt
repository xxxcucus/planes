package com.planes.android.screens.conversation

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.planes.android.repository.ChatDbRepository
import com.planes.android.repository.PlanesUserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ConversationViewModel @Inject constructor(private val chatRepository: ChatDbRepository): ViewModel() {

    val m_TextToSend : MutableState<String> = mutableStateOf("")

    fun setTextToSend(text: String) {
        m_TextToSend.value = text
    }

    fun getTextToSend(): String {
        return m_TextToSend.value
    }
}
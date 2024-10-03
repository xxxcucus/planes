package com.planes.android.conversation

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.planes.android.ApplicationScreens
import com.planes.android.MainActivity
import com.planes.android.R
import com.planes.android.chat.ChatAdapter
import com.planes.android.chat.ChatEntryModel
import com.planes.android.chat.ChatMessage
import com.planes.android.chat.DatabaseServiceGlobal
import com.planes.android.login.ReceiveChatMessagesServiceGlobal
import com.planes.multiplayer_engine.MultiplayerRoundJava
import com.planes.multiplayer_engine.responses.UserWithLastLoginResponse
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class ConversationFragment: Fragment() {

    private lateinit var m_MessagesList: List<ChatMessageModel>
    private lateinit var m_ConversationAdapter: ConversationAdapter
    private var m_ReceivedChatMessagesService =  ReceiveChatMessagesServiceGlobal()
    private var m_DatabaseService = DatabaseServiceGlobal()
    private var m_MultiplayerRound = MultiplayerRoundJava()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        m_DatabaseService.createService(context)
        m_ReceivedChatMessagesService.createService(m_DatabaseService)
        //m_ReceivedChatMessageService.setChatFragmentUpdateFunction TODO
        m_MultiplayerRound.createPlanesRound()
        prepareMessagesList()
    }

    override fun onResume() {
        super.onResume()
        //prepareMessagesListResume()
    }

    private fun prepareMessagesList() {
        var messagesFromDb : List<ChatMessage>? = null
        runBlocking { // this: CoroutineScope
            launch {
                messagesFromDb = m_DatabaseService.getMessages(m_MultiplayerRound.getUsername(), m_MultiplayerRound.getUserId())
            }
        }

        if (messagesFromDb == null)
            return

        m_MessagesList = transformMessagesListToConversationModel(messagesFromDb!!)
        m_ConversationAdapter = ConversationAdapter(m_MessagesList)
        m_ConversationAdapter.notifyDataSetChanged()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_chat, container, false)

        val recyclerView: RecyclerView = rootView.findViewById(R.id.recycler_chat)
        val mLayoutManager = LinearLayoutManager(activity)
        recyclerView.layoutManager = mLayoutManager
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.adapter = m_ConversationAdapter

        if (activity is MainActivity) {
            (activity as MainActivity).setActionBarTitle(getString(R.string.conversation))
            (activity as MainActivity).setCurrentFragmentId(ApplicationScreens.Conversation)
            (activity as MainActivity).updateOptionsMenu()
        }
        return rootView
    }

    private fun transformMessagesListToConversationModel(messagesList: List<ChatMessage>) : List<ChatMessageModel> {
        return messagesList.map { entry -> ChatMessageModel(entry, m_MultiplayerRound.getUsername(), m_MultiplayerRound.getUserId()) }
    }

}

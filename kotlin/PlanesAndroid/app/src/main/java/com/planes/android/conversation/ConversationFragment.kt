package com.planes.android.conversation

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.planes.android.ApplicationScreens
import com.planes.android.MainActivity
import com.planes.android.R
import com.planes.android.chat.ChatMessage
import com.planes.android.chat.DatabaseServiceGlobal
import com.planes.android.login.ReceiveChatMessagesServiceGlobal
import com.planes.multiplayer_engine.MultiplayerRoundJava
import com.planes.multiplayer_engine.commobj.SimpleRequestNotConnectedToGameWithoutLoadingCommObj
import com.planes.multiplayer_engine.commobj.SimpleRequestWithoutLoadingCommObj
import com.planes.multiplayer_engine.responses.ChatMessageResponse
import com.planes.multiplayer_engine.responses.SendChatMessageResponse
import com.planes.multiplayer_engine.responses.SendNotSentMovesResponse
import com.planes.utils.DateTimeUtils
import io.reactivex.Observable
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.Response
import java.time.Instant
import java.util.Date

class ConversationFragment: Fragment() {

    private lateinit var m_MessagesList: List<ChatMessageModel>
    private lateinit var m_ConversationAdapter: ConversationAdapter
    private var m_ReceivedChatMessagesService =  ReceiveChatMessagesServiceGlobal()
    private var m_DatabaseService = DatabaseServiceGlobal()
    private var m_MultiplayerRound = MultiplayerRoundJava()
    private var m_UserId: Long = 0L
    private lateinit var m_Username: String

    //TODO: this should also work when the user is not connected to a game
    private lateinit var m_SendChatMessageCommObj: SimpleRequestNotConnectedToGameWithoutLoadingCommObj<SendChatMessageResponse>

    private lateinit var m_EditText: EditText
    private var m_NotSentMessagesList = mutableListOf<Pair<Long, String>>()

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

    override fun onDetach() {
        super.onDetach()
        disposeSubscription()
    }

    private fun prepareMessagesList() {

        m_UserId = requireArguments().getLong("conversation/userid")
        m_Username = requireArguments().getString("conversation/username")!!

        var messagesFromDb : List<ChatMessage>? = null
        runBlocking { // this: CoroutineScope
            launch {
                messagesFromDb = m_DatabaseService.getMessages(m_MultiplayerRound.getUsername(), m_MultiplayerRound.getUserId(), m_Username, m_UserId, m_MultiplayerRound.getUsername(), m_MultiplayerRound.getUserId() )
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
        val rootView = inflater.inflate(R.layout.fragment_conversation1, container, false)

        val sendMessageButton = rootView.findViewById(R.id.send_message) as Button
        m_EditText = rootView.findViewById(R.id.message_edittext)

        val recyclerView: RecyclerView = rootView.findViewById(R.id.recycler_conversation)
        val mLayoutManager = LinearLayoutManager(activity)
        recyclerView.layoutManager = mLayoutManager
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.adapter = m_ConversationAdapter

        if (activity is MainActivity) {
            (activity as MainActivity).setActionBarTitle(getString(R.string.conversation) + " " + m_Username)
            (activity as MainActivity).setCurrentFragmentId(ApplicationScreens.Conversation)
            (activity as MainActivity).updateOptionsMenu()
        }

        return rootView
    }

    private fun transformMessagesListToConversationModel(messagesList: List<ChatMessage>) : List<ChatMessageModel> {
        return messagesList.map { entry -> ChatMessageModel(entry, m_MultiplayerRound.getUsername(), m_MultiplayerRound.getUserId()) }
    }

    private fun sendMessage() {
        val message = m_EditText.text.toString()

        var formattedDate = DateTimeUtils.getDateTimeNowAsString()

        val messageResponse = ChatMessageResponse(m_MultiplayerRound.getUserId().toString(), m_MultiplayerRound.getUsername(), m_UserId.toString(), m_Username, message, formattedDate)

        runBlocking { // this: CoroutineScope
            launch {
                m_DatabaseService.addChatMessage(
                    messageResponse,
                    m_MultiplayerRound.getUserId(),
                    m_MultiplayerRound.getUsername()
                )
            }
        }

        val chatMessage = ChatMessage(0, m_MultiplayerRound.getUserId().toInt(), m_MultiplayerRound.getUsername(), m_UserId.toInt(), m_Username,  m_MultiplayerRound.getUserId().toInt(), m_MultiplayerRound.getUsername(), message, Date.from(Instant.now()))
        val chatMessageModel = ChatMessageModel(chatMessage, m_MultiplayerRound.getUsername(), m_MultiplayerRound.getUserId())

        m_MessagesList = m_MessagesList + chatMessageModel
        m_ConversationAdapter = ConversationAdapter(m_MessagesList)
        m_ConversationAdapter.notifyDataSetChanged()

        //TODO: call the send message service
    }

    private fun disposeSubscription() {
        if (this::m_SendChatMessageCommObj.isInitialized)
            m_SendChatMessageCommObj.disposeSubscription()
    }

    private fun createObservableSendChatMessage(message: String) : Observable<Response<SendChatMessageResponse>> {
        return m_MultiplayerRound.sendChatMessage(m_UserId, message)
    }

    private fun sendChatMessageResponse(response: SendChatMessageResponse) {
        
    }
}

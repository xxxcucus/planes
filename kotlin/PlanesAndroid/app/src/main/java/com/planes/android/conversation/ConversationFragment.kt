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
import com.planes.android.Tools
import com.planes.android.chat.ChatMessage
import com.planes.android.chat.DatabaseServiceGlobal
import com.planes.android.chat.NewMessagesServiceGlobal
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
import java.util.function.Predicate

class ConversationFragment: Fragment() {

    private lateinit var m_MessagesList: MutableList<ChatMessageModel>
    private lateinit var m_ConversationAdapter: ConversationAdapter
    private lateinit var m_RecyclerView: RecyclerView
    private var m_ReceivedChatMessagesService =  ReceiveChatMessagesServiceGlobal()
    private var m_DatabaseService = DatabaseServiceGlobal()
    private var m_NewMessagesService = NewMessagesServiceGlobal()
    private var m_MultiplayerRound = MultiplayerRoundJava()
    private var m_UserId: Long = 0L
    private lateinit var m_Username: String
    private var m_MessageIndex = 0L
    private lateinit var m_Context: Context

    private lateinit var m_SendChatMessageCommObj: SimpleRequestNotConnectedToGameWithoutLoadingCommObj<SendChatMessageResponse>

    private lateinit var m_EditText: EditText
    private var m_NotSentMessagesList = mutableListOf<Pair<Long, String>>()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        m_Context = context
        m_DatabaseService.createService(context)
        m_NewMessagesService.createService()
        m_ReceivedChatMessagesService.createService(m_DatabaseService, m_NewMessagesService)
        m_ReceivedChatMessagesService.setConversationFragmentUpdateFunction(::updateConversationsWithResponses)
        m_MultiplayerRound.createPlanesRound()
        prepareMessagesList()
    }

    override fun onResume() {
        super.onResume()
        //TODO: to test this when the application goes into background or the fragment is closed
        prepareMessagesListResume()
    }

    override fun onDetach() {
        super.onDetach()
        disposeSubscription()
        m_ReceivedChatMessagesService.deactivateUpdateOfConversation()
    }

    private fun prepareMessagesList() {

        m_UserId = requireArguments().getLong("conversation/userid")
        m_Username = requireArguments().getString("conversation/username")!!

        var ownUserId = m_MultiplayerRound.getUserId()
        var ownUsername = m_MultiplayerRound.getUsername()

        var messagesFromDb : List<ChatMessage>? = null
        runBlocking { // this: CoroutineScope
            launch {
                messagesFromDb = m_DatabaseService.getMessages(ownUsername, ownUserId , m_Username, m_UserId, ownUsername, ownUserId )
            }
        }

        if (messagesFromDb == null)
            return

        m_MessagesList = transformMessagesListToConversationModel(messagesFromDb!!)
        m_ConversationAdapter = ConversationAdapter(m_MessagesList)
        m_ConversationAdapter.notifyDataSetChanged()
        //m_RecyclerView.scrollToPosition(m_MessagesList.size - 1)
    }

    private fun prepareMessagesListResume() {

        m_UserId = requireArguments().getLong("conversation/userid")
        m_Username = requireArguments().getString("conversation/username")!!

        var ownUserId = m_MultiplayerRound.getUserId()
        var ownUsername = m_MultiplayerRound.getUsername()

        var messagesFromDb : List<ChatMessage>? = null
        runBlocking { // this: CoroutineScope
            launch {
                messagesFromDb = m_DatabaseService.getMessages(ownUsername, ownUserId , m_Username, m_UserId, ownUsername, ownUserId )
            }
        }

        if (messagesFromDb == null)
            return

        m_MessagesList = transformMessagesListToConversationModel(messagesFromDb!!)
        m_ConversationAdapter.updateSections(m_MessagesList)
        m_RecyclerView.scrollToPosition(m_MessagesList.size - 1)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_conversation1, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sendMessageButton = view.findViewById(R.id.send_message) as Button
        sendMessageButton.setOnClickListener {
            sendMessage()
        }
        m_EditText = view.findViewById(R.id.message_edittext)

        m_RecyclerView = view.findViewById(R.id.recycler_conversation)
        var mLayoutManager = LinearLayoutManager(activity)
        m_RecyclerView.layoutManager = mLayoutManager
        m_RecyclerView.itemAnimator = DefaultItemAnimator()
        m_RecyclerView.adapter = m_ConversationAdapter
        m_RecyclerView.scrollToPosition(m_MessagesList.size - 1)

        if (activity is MainActivity) {
            (activity as MainActivity).setActionBarTitle(getString(R.string.conversation) + " " + m_Username)
            (activity as MainActivity).setCurrentFragmentId(ApplicationScreens.Conversation)
            (activity as MainActivity).updateOptionsMenu()
        }
    }

    private fun transformMessagesListToConversationModel(messagesList: List<ChatMessage>) : MutableList<ChatMessageModel> {
        return messagesList.map { entry -> ChatMessageModel(entry, m_MultiplayerRound.getUsername(), m_MultiplayerRound.getUserId()) }.toMutableList()
    }

    private fun sendMessage() {
        val message = m_EditText.text.toString().trim()
        if (message.isEmpty())
            return
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

        m_MessagesList.add(chatMessageModel)
        m_ConversationAdapter.updateSections(m_MessagesList)
        m_RecyclerView.scrollToPosition(m_MessagesList.size - 1)

        m_SendChatMessageCommObj = SimpleRequestNotConnectedToGameWithoutLoadingCommObj(::createObservableSendChatMessage,
            getString(R.string.send_chat_message_error), getString(R.string.unknownerror), getString(R.string.validation_user_not_loggedin),
             ::receivedSendChatMessageResponse, ::finalizeSendChatMessageSuccess, ::finalizeSendChatMessageError,  requireActivity())
        m_SendChatMessageCommObj.makeRequest()
        m_EditText.setText("")
    }

    private fun disposeSubscription() {
        if (this::m_SendChatMessageCommObj.isInitialized)
            m_SendChatMessageCommObj.disposeSubscription()
    }

    private fun createObservableSendChatMessage() : Observable<Response<SendChatMessageResponse>> {
        val message = m_EditText.text.toString()
        m_MessageIndex++
        m_NotSentMessagesList.add(Pair(m_MessageIndex, message))
        return m_MultiplayerRound.sendChatMessage(m_UserId, message, m_MessageIndex)
    }

    private fun receivedSendChatMessageResponse(response: SendChatMessageResponse): String {

        if (response.m_Sent) {
            m_NotSentMessagesList.removeIf{ x: Pair<Long, String> -> x.first == response.m_MessageId.toLong() }
            return ""
        }

        var errorString = getString(R.string.chat_message_not_sent)
        Tools.displayToast(errorString, m_Context)

        return errorString
    }

    private fun finalizeSendChatMessageSuccess() {
        //TODO: if necessary
    }

    private fun finalizeSendChatMessageError() {
        //TODO: if necessary
    }

    private fun updateConversationsWithResponses(messages : List<ChatMessageResponse>) {
        if (messages.isEmpty())
            return

        for (m in messages) {
            if (m.m_SenderId.toLong() != m_UserId || m.m_SenderName != m_Username)
                continue;
            var msgDate = DateTimeUtils.parseDate(m.m_CreatedAt)
            if (msgDate == null)
                msgDate = Date.from(Instant.now())
            val chatMessage = ChatMessage(0, m.m_SenderId.toInt(), m.m_SenderName, m_MultiplayerRound.getUserId().toInt(), m_MultiplayerRound.getUsername(),  m_MultiplayerRound.getUserId().toInt(), m_MultiplayerRound.getUsername(), m.m_Message, msgDate!!)
            val chatMessageModel = ChatMessageModel(chatMessage, m_MultiplayerRound.getUsername(), m_MultiplayerRound.getUserId())

            m_MessagesList.add(chatMessageModel)
        }

        m_ConversationAdapter.updateSections(m_MessagesList)
        m_RecyclerView.scrollToPosition(m_MessagesList.size - 1)

    }
}

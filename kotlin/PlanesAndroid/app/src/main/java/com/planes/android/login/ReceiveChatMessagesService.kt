package com.planes.android.login

import com.planes.android.chat.IDatabaseService
import com.planes.android.chat.INewMessagesService
import com.planes.android.chat.NewMessageIdent
import com.planes.multiplayer_engine.MultiplayerRoundJava
import com.planes.multiplayer_engine.responses.ChatMessageResponse
import com.planes.multiplayer_engine.responses.ReceiveChatMessagesResponse
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.concurrent.TimeUnit

class ReceiveChatMessagesService(databaseService: IDatabaseService, newMesssagesService: INewMessagesService) : IReceiveChatMessagesService {
    private lateinit var m_PollChatMessagesSubscription: Disposable
    private var m_PlaneRound = MultiplayerRoundJava()
    private lateinit var m_ChatUpdateFunction: (List<ChatMessageResponse>) -> Unit
    private lateinit var m_ConversationUpdateFunction: (List<ChatMessageResponse>) -> Unit
    private var m_UpdateChat = false
    private var m_UpdateConversation = false
    private var m_DatabaseService : IDatabaseService
    private var m_NewMessagesService: INewMessagesService

    init {
        m_DatabaseService = databaseService
        m_NewMessagesService = newMesssagesService
    }

    override fun startPolling() {

        m_PlaneRound.createPlanesRound()

        if (this::m_PollChatMessagesSubscription.isInitialized && !m_PollChatMessagesSubscription.isDisposed)
            return

        m_PlaneRound.createPlanesRound()
        m_PollChatMessagesSubscription =
            Observable.interval(1,30, TimeUnit.SECONDS, Schedulers.io())
                .switchMap { m_PlaneRound.getChatMessages() }
                .retry()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ data -> reactToChatMessagesInPolling(data.body()) }
                ) { }
    }

    override fun stopPolling() {
        destroySubscription()
    }

    fun destroySubscription() {
        if (this::m_PollChatMessagesSubscription.isInitialized)
            m_PollChatMessagesSubscription.dispose()
    }

    override fun isPolling(): Boolean {
        return !m_PollChatMessagesSubscription.isDisposed && this::m_PollChatMessagesSubscription.isInitialized
    }

    fun reactToChatMessagesInPolling(body: ReceiveChatMessagesResponse?) {
        if (body == null)
            return;
        var chatMessages = body.m_Messages


        runBlocking { // this: CoroutineScope
            launch {
                for (message in chatMessages) {
                    m_DatabaseService.addChatMessage(message, m_PlaneRound.getUserId(), m_PlaneRound.getUsername())
                }
            }
        }

        for (message in chatMessages) {
            m_NewMessagesService.setNewMessage(NewMessageIdent(message.m_SenderName, message.m_SenderId.toLong(), m_PlaneRound.getUsername(), m_PlaneRound.getUserId()), true)
        }

        if (m_UpdateChat)
            m_ChatUpdateFunction(chatMessages)

        if (m_UpdateConversation)
            m_ConversationUpdateFunction(chatMessages)
    }


    override fun setChatFragmentUpdateFunction(updateFunction: (List<ChatMessageResponse>)->Unit) {
        m_UpdateChat = true
        m_ChatUpdateFunction = updateFunction
    }

    override fun setConversationFragmentUpdateFunction(updateFunction: (List<ChatMessageResponse>)->Unit) {
        m_UpdateConversation = true
        m_ConversationUpdateFunction = updateFunction
    }

    override fun deactivateUpdateOfChat() {
        m_UpdateChat = false
    }

    override fun deactivateUpdateOfConversation() {
        m_UpdateConversation = false
    }
}
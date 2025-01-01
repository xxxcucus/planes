package com.planes.android.chat

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
import com.planes.android.login.PlayersListServiceGlobal
import com.planes.android.login.ReceiveChatMessagesServiceGlobal
import com.planes.multiplayer_engine.MultiplayerRoundJava
import com.planes.multiplayer_engine.responses.ChatMessageResponse
import com.planes.multiplayer_engine.responses.UserWithLastLoginResponse

class ChatFragment : Fragment() {

    private lateinit var m_SectionsList: List<ChatEntryModel>
    private lateinit var m_ChatAdapter: ChatAdapter
    private var m_PlayersListService = PlayersListServiceGlobal()
    private var m_ReceivedChatMessagesService =  ReceiveChatMessagesServiceGlobal()
    private var m_NewMessagesService = NewMessagesServiceGlobal()
    private var m_DatabaseService = DatabaseServiceGlobal()
    private var m_MultiplayerRound = MultiplayerRoundJava()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        m_DatabaseService.createService(context)
        m_PlayersListService.createService()
        m_PlayersListService.setChatFragmentUpdateFunction(::updateSectionsList)
        m_NewMessagesService.createService()
        m_ReceivedChatMessagesService.createService(m_DatabaseService, m_NewMessagesService)
        m_ReceivedChatMessagesService.setChatFragmentUpdateFunction(::updateConversationsWithResponses)
        m_MultiplayerRound.createPlanesRound()
        prepareSectionsList()
    }

    override fun onResume() {
        super.onResume()
        prepareSectionsListResume()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_chat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView: RecyclerView = view.findViewById(R.id.recycler_chat)
        val mLayoutManager = LinearLayoutManager(activity)
        recyclerView.layoutManager = mLayoutManager
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.adapter = m_ChatAdapter

        if (activity is MainActivity) {
            (activity as MainActivity).setActionBarTitle(getString(R.string.chat))
            (activity as MainActivity).setCurrentFragmentId(ApplicationScreens.Chat)
            (activity as MainActivity).updateOptionsMenu()
        }
    }

    private fun prepareSectionsList() {
        var playersList = emptyList<UserWithLastLoginResponse>()
        if (m_PlayersListService.isPolling()) {
            playersList = m_PlayersListService.getPlayersList()
        }

        m_SectionsList = transformUserListToChatModel(playersList, emptyList())
        m_ChatAdapter = ChatAdapter(m_MultiplayerRound.getUsername(), m_MultiplayerRound.getUserId(), m_NewMessagesService, m_SectionsList, requireActivity())
        m_ChatAdapter.notifyDataSetChanged()
    }

    private fun prepareSectionsListResume() {
        var playersList = emptyList<UserWithLastLoginResponse>()
        if (m_PlayersListService.isPolling()) {
            playersList = m_PlayersListService.getPlayersList()
        }

        m_SectionsList = transformUserListToChatModel(playersList, m_SectionsList)
        m_ChatAdapter.updateSections(m_SectionsList)
        m_ChatAdapter.notifyDataSetChanged()
    }

    private fun updateSectionsList(playersList : List<UserWithLastLoginResponse>) {
        m_SectionsList = transformUserListToChatModel(playersList, m_SectionsList)
        m_ChatAdapter.updateSections(m_SectionsList)
        m_ChatAdapter.notifyDataSetChanged()
    }

    override fun onDetach () {
        super.onDetach()
        m_PlayersListService.deactivateUpdateOfChat()
        m_ReceivedChatMessagesService.deactivateUpdateOfChat()
    }

    private fun transformUserListToChatModel(playersList: List<UserWithLastLoginResponse>, oldPlayersList: List<ChatEntryModel>) : List<ChatEntryModel> {
        var retVal = playersList.filter{ entry -> entry.m_UserName != m_MultiplayerRound.getUsername()
                && entry.m_UserId != m_MultiplayerRound.getUserId().toString()}.map { entry -> ChatEntryModel(entry) }

        for (entry in retVal) {
            var sameInOldList = oldPlayersList.find { it.getPlayerId() == entry.getPlayerId() && it.getPlayerName() == entry.getPlayerName()}
            if (sameInOldList != null) {
                entry.setNewMessages(sameInOldList.areNewMessages())
                m_NewMessagesService.setNewMessage(NewMessageIdent(entry.getPlayerName(), entry.getPlayerId(), m_MultiplayerRound.getUsername(), m_MultiplayerRound.getUserId()), sameInOldList.areNewMessages())
            } else {
                val newMessages = m_NewMessagesService.getNewMessage(NewMessageIdent(entry.getPlayerName(), entry.getPlayerId(), m_MultiplayerRound.getUsername(), m_MultiplayerRound.getUserId()))
                if (newMessages != null && newMessages!!) {
                    entry.setNewMessages(true)
                }
            }
        }

        return retVal
    }

    private fun updateConversationsWithResponses(messages : List<ChatMessageResponse>) {
        for (s in m_SectionsList) {
            for (m in messages) {
                if (s.getPlayerId() == m.m_SenderId.toLong()) {
                    s.setNewMessages(true)
                    m_NewMessagesService.setNewMessage(NewMessageIdent(s.getPlayerName(), s.getPlayerId(), m_MultiplayerRound.getUsername(), m_MultiplayerRound.getUserId()), true)
                    break
                }
            }
        }
        m_ChatAdapter.notifyDataSetChanged()
    }
}
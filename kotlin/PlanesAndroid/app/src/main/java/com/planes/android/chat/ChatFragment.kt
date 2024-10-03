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
import com.planes.multiplayer_engine.responses.UserWithLastLoginResponse


class ChatFragment : Fragment() {

    private lateinit var m_SectionsList: List<ChatEntryModel>
    private lateinit var m_ChatAdapter: ChatAdapter
    private var m_PlayersListService = PlayersListServiceGlobal()
    private var m_ReceivedChatMessagesService =  ReceiveChatMessagesServiceGlobal()
    private var m_DatabaseService = DatabaseServiceGlobal()
    private var m_MultiplayerRound = MultiplayerRoundJava()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        m_DatabaseService.createService(context)
        m_PlayersListService.createService()
        m_PlayersListService.setChatFragmentUpdateFunction(::updateSectionsList)
        m_ReceivedChatMessagesService.createService(m_DatabaseService)
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
        val rootView = inflater.inflate(R.layout.fragment_chat, container, false)

        val recyclerView: RecyclerView = rootView.findViewById(R.id.recycler_chat)
        val mLayoutManager = LinearLayoutManager(activity)
        recyclerView.layoutManager = mLayoutManager
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.adapter = m_ChatAdapter

        if (activity is MainActivity) {
            (activity as MainActivity).setActionBarTitle(getString(R.string.chat))
            (activity as MainActivity).setCurrentFragmentId(ApplicationScreens.Chat)
            (activity as MainActivity).updateOptionsMenu()
        }
        return rootView
    }

    private fun prepareSectionsList() {
        var playersList = emptyList<UserWithLastLoginResponse>()
        if (m_PlayersListService.isPolling()) {
            playersList = m_PlayersListService.getPlayersList()
        }

        m_SectionsList = transformUserListToChatModel(playersList)
        m_ChatAdapter = ChatAdapter(m_SectionsList)
        m_ChatAdapter.notifyDataSetChanged()
    }

    private fun prepareSectionsListResume() {
        var playersList = emptyList<UserWithLastLoginResponse>()
        if (m_PlayersListService.isPolling()) {
            playersList = m_PlayersListService.getPlayersList()
        }

        m_SectionsList = transformUserListToChatModel(playersList)
        m_ChatAdapter.updateSections(m_SectionsList)
        m_ChatAdapter.notifyDataSetChanged()
    }

    private fun updateSectionsList(playersList : List<UserWithLastLoginResponse>) {
        m_SectionsList = transformUserListToChatModel(playersList)
        m_ChatAdapter.updateSections(m_SectionsList)
        m_ChatAdapter.notifyDataSetChanged()
    }

    override fun onDetach () {
        super.onDetach()
        m_PlayersListService.deactivateUpdateOfChat()
    }

    private fun transformUserListToChatModel(playersList: List<UserWithLastLoginResponse>) : List<ChatEntryModel> {
        return playersList.filter{ entry -> entry.m_UserName != m_MultiplayerRound.getUsername()
                && entry.m_UserId != m_MultiplayerRound.getUserId().toString()}.map { entry -> ChatEntryModel(entry) }
    }
}
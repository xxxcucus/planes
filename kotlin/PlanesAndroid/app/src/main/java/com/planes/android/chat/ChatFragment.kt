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


class ChatFragment : Fragment() {

    private lateinit var m_SectionsList: List<ChatEntryModel>
    private lateinit var m_ChatAdapter: ChatAdapter
    private lateinit var m_PlayersListService: PlayersListServiceGlobal

    override fun onAttach(context: Context) {
        super.onAttach(context)
        m_PlayersListService.createService()
        prepareSectionsList()
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
            (activity as MainActivity).setActionBarTitle(getString(R.string.about))
            (activity as MainActivity).setCurrentFragmentId(ApplicationScreens.Chat)
            (activity as MainActivity).updateOptionsMenu()
        }
        return rootView
    }

    private fun prepareSectionsList() {
        var playersList = emptyList<String>()
        if (m_PlayersListService.isPolling()) {
            playersList = m_PlayersListService.getPlayersList()
        }

        m_SectionsList = playersList.map { entry -> ChatEntryModel(entry) }
        m_ChatAdapter = ChatAdapter(m_SectionsList)
        m_ChatAdapter.notifyDataSetChanged()
    }
}
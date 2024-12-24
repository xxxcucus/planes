package com.planes.android.chat

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.planes.android.MainActivity
import com.planes.android.R


class ChatAdapter(playerName: String, playerId: Long, newMessagesService: INewMessagesService, chatSectionsList: List<ChatEntryModel>, activity: FragmentActivity) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var m_SectionsList: List<ChatEntryModel> = chatSectionsList
    private var m_Activity: FragmentActivity = activity
    private var m_NewMessagesService: INewMessagesService = newMessagesService
    private var m_PlayerName = playerName
    private var m_PlayerId = playerId

    inner class MyViewHolder(view: View, context: Context) : RecyclerView.ViewHolder(view) {
        var m_PlayerName: TextView = view.findViewById(R.id.chat_player_name)
        var m_PlayerStatus: TextView = view.findViewById(R.id.chat_player_status)
        var m_NewMessages: ImageView = view.findViewById(R.id.chat_new_messages)
        var m_Context: Context = context
    }

    override fun getItemViewType(position: Int): Int {
        return 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.chatlayout, parent, false)
        return MyViewHolder(itemView, parent.context)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val section: ChatEntryModel = m_SectionsList[position]
        val holderView = holder as MyViewHolder
        holderView.m_PlayerName.text = section.getPlayerName()
        holderView.m_PlayerStatus.text = if (section.isPlayerOnline()) "Online" else "Offline"
        val newMessageInService = m_NewMessagesService.getNewMessage(NewMessageIdent(section.getPlayerName(), section.getPlayerId(),m_PlayerName, m_PlayerId))
        holderView.m_NewMessages.isVisible =  if (newMessageInService == null) section.areNewMessages() else newMessageInService
        holderView.m_PlayerName.setOnClickListener(object: View.OnClickListener {
            override fun onClick(view : View) {
                var useridx = holderView.adapterPosition
                var chatEntryModel = m_SectionsList[useridx]
                m_SectionsList[useridx].setNewMessages(false)
                m_NewMessagesService.setNewMessage(NewMessageIdent(m_SectionsList[useridx].getPlayerName(), m_SectionsList[useridx].getPlayerId(), m_PlayerName, m_PlayerId), false)
                (m_Activity as MainActivity).startConversationFragment(chatEntryModel.getPlayerId(), chatEntryModel.getPlayerName())
            }
        })
    }

    override fun getItemCount(): Int {
        return m_SectionsList.size
    }

    fun updateSections(sectionsList : List<ChatEntryModel>) {
        m_SectionsList = sectionsList
    }
}
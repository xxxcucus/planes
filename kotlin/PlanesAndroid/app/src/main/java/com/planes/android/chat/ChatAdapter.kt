package com.planes.android.chat

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.planes.android.R


class ChatAdapter(chatSectionsList: List<ChatEntryModel>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var m_SectionsList: List<ChatEntryModel> = chatSectionsList

    inner class MyViewHolder(view: View, context: Context) : RecyclerView.ViewHolder(view) {
        var m_PlayerName: TextView = view.findViewById(R.id.chat_player_name)
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
    }

    override fun getItemCount(): Int {
        return m_SectionsList.size
    }

    fun updateSections(sectionsList : List<ChatEntryModel>) {
        m_SectionsList = sectionsList
    }
}
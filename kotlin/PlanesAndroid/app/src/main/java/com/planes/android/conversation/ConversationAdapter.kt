package com.planes.android.conversation

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.planes.android.R

class ConversationAdapter(messagesList: List<ChatMessageModel>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var m_MessagesList: MutableList<ChatMessageModel> = messagesList.toMutableList()

    inner class MyViewHolder(view: View, context: Context) : RecyclerView.ViewHolder(view) {
        var m_Sender: TextView = view.findViewById(R.id.conversation_sender)
        var m_Message: TextView = view.findViewById(R.id.conversation_message)
        var m_Date: TextView = view.findViewById(R.id.conversation_timestamp)
        var m_Context: Context = context  //TODO: do I need this ?
    }

    override fun getItemViewType(position: Int): Int {
        val message: ChatMessageModel = m_MessagesList[position]
        if (message.isSentByMe())
            return 0
        return 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == 0) {
            val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.conversationlayout_me, parent, false)
            return MyViewHolder(itemView, parent.context)
        }

        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.conversationlayout_other, parent, false)
        return MyViewHolder(itemView, parent.context)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message: ChatMessageModel = m_MessagesList[position]
        val holderView = holder as MyViewHolder
        holderView.m_Sender.text = message.getSender()
        holderView.m_Message.text = message.getMessage()
        holderView.m_Date.text = message.getTimestamp()
    }

    override fun getItemCount(): Int {
        return m_MessagesList.size
    }

    fun updateSections(messagesList : MutableList<ChatMessageModel>) {
        m_MessagesList.clear()
        m_MessagesList.addAll(messagesList)
        notifyDataSetChanged()
    }
}
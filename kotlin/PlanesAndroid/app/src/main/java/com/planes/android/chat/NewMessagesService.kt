package com.planes.android.chat

class NewMessagesService : INewMessagesService {

    private val m_NewMessagesStatus: HashMap<String, Boolean> = HashMap<String, Boolean>()

    override fun setNewMessage(player: String, value: Boolean) {
        m_NewMessagesStatus.put(player, value)
    }

    override fun getNewMessage(player: String): Boolean? {
        if (!m_NewMessagesStatus.containsKey(player))
            return null
        return m_NewMessagesStatus.get(player)
    }
}
package com.planes.android.chat

class NewMessagesService : INewMessagesService {

    private val m_NewMessagesStatus: HashMap<NewMessageIdent, Boolean> = HashMap<NewMessageIdent, Boolean>()

    override fun setNewMessage(messageIdent: NewMessageIdent, value: Boolean) {
        m_NewMessagesStatus.put(messageIdent, value)
    }

    override fun getNewMessage(messageIdent: NewMessageIdent): Boolean? {
        if (!m_NewMessagesStatus.containsKey(messageIdent))
            return null
        return m_NewMessagesStatus.get(messageIdent)
    }
}
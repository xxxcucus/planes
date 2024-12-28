package com.planes.android.chat

class NewMessagesService : INewMessagesService {

    private val m_NewMessagesStatus: HashMap<NewMessageIdent, Boolean> =
        HashMap<NewMessageIdent, Boolean>()

    override fun setNewMessage(messageIdent: NewMessageIdent, value: Boolean) {
        m_NewMessagesStatus.put(messageIdent, value)
    }

    override fun getNewMessage(messageIdent: NewMessageIdent): Boolean? {
        if (!m_NewMessagesStatus.containsKey(messageIdent))
            return null
        return m_NewMessagesStatus.get(messageIdent)
    }

    override fun areNewMessagesForPlayer(receiverName: String, receiverId: Long) : Boolean {
        for ((messageIdent, newMessages) in m_NewMessagesStatus) {
            if (newMessages && messageIdent.receiverId == receiverId && messageIdent.receiverName == receiverName)
                return true
        }

        return false
    }

    override fun resetFlags(newMessagesFlags: List<NewMessagesFlag>) {
        m_NewMessagesStatus.clear()

        for (newMessage in newMessagesFlags) {
            m_NewMessagesStatus.put(NewMessageIdent(newMessage.m_SenderName, newMessage.m_SenderId.toLong(), newMessage.m_ReceiverName, newMessage.m_ReceiverId.toLong()), newMessage.m_NewMessages)
        }
    }

    override fun getNewMessagesFlags(): HashMap<NewMessageIdent, Boolean> {
        return m_NewMessagesStatus
    }
}
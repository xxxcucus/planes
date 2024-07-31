#ifndef __RECEIVED_CHAT_MESSAGE_VIEW_MODEL__
#define __RECEIVED_CHAT_MESSAGE_VIEW_MODEL__

#include <QString>
#include <QDateTime>

struct ReceivedChatMessageViewModel {
    QString m_SenderName;
    long int m_SenderId;
    QString m_ReceiverName;
    long int m_ReceiverId;
    QString m_Message;
    QDateTime m_CreatedAt;
};

#endif

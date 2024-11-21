#ifndef __CHAT_MESSAGE_REQUEST_VIEW_MODEL__
#define __CHAT_MESSAGE_REQUEST_VIEW_MODEL__

#include "basisrequestviewmodel.h"

#include <QString>
#include <QJsonObject>

struct SendChatMessageViewModel: public BasisRequestViewModel {
    long int m_ReceiverId = 0;
    long int m_MessageId = 0;
    QString m_Message;

    QJsonObject toJson() {
        QJsonObject retVal = BasisRequestViewModel::toJson();
        retVal.insert("receiverId", QString::number(m_ReceiverId));
        retVal.insert("message",  m_Message);
        retVal.insert("messageId", QString::number(m_MessageId));
        return retVal;
    }
};

#endif

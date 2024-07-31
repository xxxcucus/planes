#ifndef __CHAT_MESSAGE_REQUEST_VIEW_MODEL__
#define __CHAT_MESSAGE_REQUEST_VIEW_MODEL__

#include "basisrequestviewmodel.h"

#include <QString>
#include <QJsonObject>

struct SendChatMessageViewModel: public BasisRequestViewModel {
    long int m_ReceiverId = 0;
    QString m_Message;

    QJsonObject toJson() {
        QJsonObject retVal = BasisRequestViewModel::toJson();
        retVal.insert("receiverId", QString::number(m_ReceiverId));
        retVal.insert("message",  m_Message);
        return retVal;
    }
};

#endif

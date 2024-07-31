#ifndef __GET_CHAT_MESSAGES_VIEWMODEL__
#define __GET_CHAT_MESSAGES_VIEWMODEL__

#include <QJsonObject>
#include "basisrequestviewmodel.h"

struct GetChatMessagesViewModel: BasisRequestViewModel {
    QJsonObject toJson() {
        QJsonObject retVal = BasisRequestViewModel::toJson();
        return retVal;
    }
};

#endif


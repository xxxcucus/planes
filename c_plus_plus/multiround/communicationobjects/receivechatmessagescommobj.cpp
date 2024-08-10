#include "receivechatmessagescommobj.h"

#include <QMessageBox>
#include <QJsonArray>
#include "multiplayerround.h"
#include "communicationtools.h"

bool ReceiveChatMessagesCommObj::makeRequest()
{
    if (m_IsSinglePlayer) {
        //qDebug() << "makeRequestBasis in single player modus";
        return false;
    }

    if (m_GlobalData->m_UserData.m_UserName.isEmpty()) {
        if (m_ParentWidget != nullptr) {
            QMessageBox msgBox(m_ParentWidget);
            msgBox.setText("No user logged in");
            msgBox.exec();
        }
        return false;
    }

    m_RequestData = prepareViewModel().toJson();

    makeRequestBasis(true);
    return true;
}

GetChatMessagesViewModel ReceiveChatMessagesCommObj::prepareViewModel() {
    GetChatMessagesViewModel chatMessagesViewModel;
    chatMessagesViewModel.m_UserId = m_GlobalData->m_UserData.m_UserId;
    chatMessagesViewModel.m_Username = m_GlobalData->m_UserData.m_UserName;

    return chatMessagesViewModel;
}


void ReceiveChatMessagesCommObj::finishedRequest()
{
    QJsonObject retJson;
    if (!finishRequestHelper(retJson))
        return;

    processResponse(retJson);
}

void ReceiveChatMessagesCommObj::processResponse(const QJsonObject& retJson) {
    QJsonValue messagesObject = retJson.value("messages");
    QJsonArray messagesArray = messagesObject.toArray();

    std::vector<ReceivedChatMessageViewModel> receivedMessages = std::vector<ReceivedChatMessageViewModel>();

    for (int i = 0; i < messagesArray.size(); i++) {
        QJsonValue messageValue = messagesArray.at(i);
        QJsonObject messageObject = messageValue.toObject();
        if (messageObject.contains("senderId") && messageObject.contains("senderName") && messageObject.contains("receiverId") &&
            messageObject.contains("receiverName") && messageObject.contains("message") && messageObject.contains("createdAt"))
        {
            ReceivedChatMessageViewModel receivedMessage;
            receivedMessage.m_SenderId = messageObject.value("senderId").toString().toLong();
            receivedMessage.m_SenderName = messageObject.value("senderName").toString();
            receivedMessage.m_ReceiverId = messageObject.value("receiverId").toString().toLong();
            receivedMessage.m_ReceiverName = messageObject.value("receiverName").toString();
            receivedMessage.m_Message = messageObject.value("message").toString();
            receivedMessage.m_CreatedAt = CommunicationTools::parseDateFromString(messageObject.value("createdAt").toString());
            receivedMessages.push_back(receivedMessage);
        }
    }

    emit chatMessagesReceived(receivedMessages);
}


bool ReceiveChatMessagesCommObj::validateReply(const QJsonObject& reply) {
    if (!(reply.contains("messages"))) {
        //qDebug() << "error 1";
        return false;
    }

    QJsonValue messagesObject = reply.value("messages");
    if (!messagesObject.isArray()) {
        //qDebug() << "error 2";
        return false;
    }

    QJsonArray messagesArray = messagesObject.toArray();
    if (messagesArray.size() > 100) {
        //qDebug() << "error 3 " << movesArray.size();
        return false;
    }

    for (int i = 0; i < messagesArray.size(); i++) {
        QJsonValue messageValue = messagesArray.at(i);
        if (!messageValue.isObject()) {
            //qDebug() << "error 4 " << i;
            return false;
        }

        QJsonObject messageObject = messageValue.toObject();
        if (!(messageObject.contains("senderId") && messageObject.contains("senderName")
            && messageObject.contains("receiverId") && messageObject.contains("receiverName")
            && messageObject.contains("message") && messageObject.contains("createdAt")))
            return false;

        if (!(checkLong(messageObject.value("senderId").toString())))
            return false;

        if (!(checkLong(messageObject.value("receiverId").toString())))
            return false;

        //TODO: other checks
    }

    return true;
}


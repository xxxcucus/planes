#include "sendchatmessagecommobj.h"

#include <QJsonValue>
#include <QJsonArray>
#include <QMessageBox>


bool SendChatMessageCommObj::makeRequest(long int receiverId, const QString& message, long int messageId) {
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

    m_RequestData = prepareViewModel(receiverId, message, messageId).toJson();

    makeRequestBasis(true);
    return true;
}

bool SendChatMessageCommObj::validateReply(const QJsonObject& retJson) {
    if (!(retJson.contains("sent")))
        return false;

    if (!(retJson.contains("messageId")))
        return false;

    return true;
}

void SendChatMessageCommObj::finishedRequest() {
    QJsonObject retJson;
    if (!finishRequestHelper(retJson))
        return;

    processResponse(retJson);
}

void SendChatMessageCommObj::processResponse(const QJsonObject& retJson) {
    bool sent = retJson.value("sent").toBool();
    bool messageId = retJson.value("messageId").toInt();

    if (sent) {
        emit messageSent(messageId);
    }
}

SendChatMessageViewModel SendChatMessageCommObj::prepareViewModel(long int receiverId, const QString& message, long int messageId) {
    QString shortenedMessage = message.left(m_MaxMessageLength);

    if (shortenedMessage.size() != message.size()) {
        qDebug() << "Message " << message << " was cut to ";
        qDebug() << shortenedMessage;
    }

    SendChatMessageViewModel viewModel;
    viewModel.m_UserId = m_GlobalData->m_UserData.m_UserId;
    viewModel.m_Username = m_GlobalData->m_UserData.m_UserName;
    viewModel.m_ReceiverId = receiverId;
    viewModel.m_Message = shortenedMessage;
    viewModel.m_MessageId = messageId;
    return viewModel;
}


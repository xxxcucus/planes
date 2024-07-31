#include "sendchatmessagecommobj.h"

#include <QJsonValue>
#include <QJsonArray>
#include <QMessageBox>


bool SendChatMessageCommObj::makeRequest(long int receiverId, const QString& message) {
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

    m_RequestData = prepareViewModel(receiverId, message).toJson();

    makeRequestBasis(true);
    return true;
}

bool SendChatMessageCommObj::validateReply(const QJsonObject& retJson) {
    if (!(retJson.contains("sent")))
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
    //TODO:
}

SendChatMessageViewModel SendChatMessageCommObj::prepareViewModel(long int receiverId, const QString& message) {
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
    return viewModel;
}


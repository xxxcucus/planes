#include "logoutcommobj.h"
#include "multiplayerround.h"

bool LogoutCommObj::makeRequest(const QString& username) {
    if (m_IsSinglePlayer) {
        //qDebug() << "makeRequestBasis in single player modus";
        return false;
    }

    if (m_GlobalData->m_UserData.m_UserName.isEmpty()) {
        if (m_ParentWidget != nullptr) { //nullptr is in tests
            QMessageBox msgBox(m_ParentWidget);
            msgBox.setText("No user logged in");
            msgBox.exec();
        }
        return false;
    }

    m_RequestData = prepareViewModel(username).toJson();

    makeRequestBasis(true);
    return true;

}

LogoutViewModel LogoutCommObj::prepareViewModel(const QString& username) {
    LogoutViewModel logoutData;
    logoutData.m_UserName = username;
    return logoutData;
}

bool LogoutCommObj::validateReply(const QJsonObject& reply) {
    return (reply.contains("loggedOut"));
}

void LogoutCommObj::finishedRequest()
{
    QJsonObject retJson;
    if (!finishRequestHelper(retJson))
        return;


    processResponse();
}

void LogoutCommObj::processResponse() {
    emit logoutCompleted();
}

#include "deactivateusercommobj.h"

bool DeactivateUserCommObj::makeRequest(const QString& username) {
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

    m_RequestData = prepareViewModel(username).toJson();

    makeRequestBasis(true);
    return true;
}

DeactivateUserViewModel DeactivateUserCommObj::prepareViewModel(const QString& username) {
    DeactivateUserViewModel requestViewModel;
    requestViewModel.m_UserName = username;
    return requestViewModel;
}

void DeactivateUserCommObj::finishedRequest() {
    QJsonObject retJson;
    if (!finishRequestHelper(retJson))
        return;

    processResponse(retJson);
}

void DeactivateUserCommObj::processResponse(const QJsonObject& retJson) {
    emit userDeactivated();
}

bool DeactivateUserCommObj::validateReply(const QJsonObject& reply) {
    return (reply.contains("deactivated"));
}

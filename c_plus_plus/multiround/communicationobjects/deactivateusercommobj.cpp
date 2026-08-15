#include "deactivateusercommobj.h"

#include <QTimer>

DeactivateUserCommObj::~DeactivateUserCommObj() {
    if (m_NoUserMessageBox != nullptr)
        delete m_NoUserMessageBox;
}

bool DeactivateUserCommObj::makeRequest(const QString& username) {
    if (m_IsSinglePlayer) {
        //qDebug() << "makeRequestBasis in single player modus";
        return false;
    }

    if (m_GlobalData->m_UserData.m_UserName.isEmpty()) {
        if (m_ParentWidget != nullptr) {
            m_NoUserMessageBox->show();
            QTimer::singleShot(2000, m_NoUserMessageBox, &QMessageBox::hide);
        }
        return false;
    }

    m_RequestData = prepareViewModel(username).toJson();

    makeRequestBasis(true);
    return true;
}

DeactivateUserViewModel DeactivateUserCommObj::prepareViewModel(const QString& username) {
    DeactivateUserViewModel requestViewModel;
    requestViewModel.m_Username = m_GlobalData->m_UserData.m_UserName;
    requestViewModel.m_UserId = m_GlobalData->m_UserData.m_UserId;
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

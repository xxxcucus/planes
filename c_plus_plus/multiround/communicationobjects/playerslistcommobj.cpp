#include "playerslistcommobj.h"

#include <QJsonValue>
#include <QJsonArray>
#include <QMessageBox>


bool PlayersListCommObj::makeRequest() {
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

bool PlayersListCommObj::validateReply(const QJsonObject& retJson) {
    if (!(retJson.contains("usernames")))
        return false;

    return true;
}

void PlayersListCommObj::finishedRequest() {
    QJsonObject retJson;
    if (!finishRequestHelper(retJson))
        return;

    processResponse(retJson);
}

void PlayersListCommObj::processResponse(const QJsonObject& retJson) {
    QJsonValue playersObject = retJson.value("usernames");
    QJsonArray playersArray = playersObject.toArray();

    QStringList playersList;
    for (int i = 0; i < playersArray.size(); i++) {
        QJsonValue playerValue = playersArray.at(i);
        QString playerName = playerValue.toString();
        playersList.append(playerName);
    }

    emit playersListReceived(playersList);
}

BasisRequestViewModel PlayersListCommObj::prepareViewModel() {
    BasisRequestViewModel viewModel;
    viewModel.m_UserId = m_GlobalData->m_UserData.m_UserId;
    viewModel.m_Username = m_GlobalData->m_UserData.m_UserName;
    return viewModel;
}

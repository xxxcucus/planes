#include "sendwinnercommobj.h"


#include <QMessageBox>
#include "viewmodels/sendwinnerviewmodel.h"

bool SendWinnerCommObj::makeRequest(bool draw, long int winnerId)
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

    m_RequestData = prepareViewModel(draw, winnerId).toJson();
    
    makeRequestBasis(true);
    return true;
}

SendWinnerViewModel SendWinnerCommObj::prepareViewModel(bool draw, long int winnerId) {
    SendWinnerViewModel sendWinnerData;
    sendWinnerData.m_UserId = m_GlobalData->m_GameData.m_UserId;
    sendWinnerData.m_Username = m_GlobalData->m_UserData.m_UserName;
    sendWinnerData.m_GameId = m_GlobalData->m_GameData.m_GameId;
    sendWinnerData.m_RoundId = m_GlobalData->m_GameData.m_RoundId;
    sendWinnerData.m_Draw = draw;
    sendWinnerData.m_WinnerId = winnerId;
    return sendWinnerData;
}

void SendWinnerCommObj::finishedRequest()
{
    QJsonObject retJson;
    if (!finishRequestHelper(retJson)) 
        return;
}

bool SendWinnerCommObj::validateReply(const QJsonObject& reply) {
    return (reply.contains("roundId"));   
}

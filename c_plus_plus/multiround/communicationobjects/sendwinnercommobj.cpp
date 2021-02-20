#include "sendwinnercommobj.h"


#include <QMessageBox>
#include "viewmodels/sendwinnerviewmodel.h"

bool SendWinnerCommObj::makeRequest(bool draw, long int winnerId)
{
    //TODO: check that multiplayer
    if (m_GlobalData->m_UserData.m_UserName.isEmpty()) {
        QMessageBox msgBox;
        msgBox.setText("No user logged in"); 
        msgBox.exec();
        return false;
    }

    SendWinnerViewModel sendWinnerData;
    sendWinnerData.m_GameId = m_GlobalData->m_GameData.m_GameId;
    sendWinnerData.m_RoundId = m_GlobalData->m_GameData.m_RoundId;
    sendWinnerData.m_Draw = draw;
    sendWinnerData.m_WinnerId = winnerId;
    
    m_RequestData = sendWinnerData.toJson();
    
    makeRequestBasis(true);
    return true;
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

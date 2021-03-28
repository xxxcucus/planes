#include "cancelroundcommobj.h"

#include <QMessageBox>
#include "viewmodels/cancelroundviewmodel.h"
#include "multiplayerround.h"


bool CancelRoundCommObj::makeRequest()
{ 
    if (m_IsSinglePlayer) {
        qDebug() << "makeRequestBasis in single player modus";
        return false;
    }
    if (m_GlobalData->m_UserData.m_UserName.isEmpty()) {
        QMessageBox msgBox;
        msgBox.setText("No user logged in"); 
        msgBox.exec();
        return false;
    }

    CancelRoundViewModel cancelRoundData;
    cancelRoundData.m_RoundId = m_GlobalData->m_GameData.m_RoundId;
    cancelRoundData.m_GameId = m_GlobalData->m_GameData.m_GameId;

    m_RequestData = cancelRoundData.toJson();
    
    makeRequestBasis(true);
    return true;
}

void CancelRoundCommObj::finishedRequest()
{
    QJsonObject retJson;
    if (!finishRequestHelper(retJson)) 
        return;

    m_MultiRound->setRoundCancelled();
    emit roundCancelled();
}

bool CancelRoundCommObj::validateReply(const QJsonObject& reply) {
    return (reply.contains("roundId"));
}

#include "startnewroundcommobj.h"

#include <QMessageBox>
#include "multiplayerround.h"
#include "viewmodels/startnewroundviewmodel.h"

bool StartNewRoundCommObj::makeRequest()
{
    if (m_GlobalData->m_UserData.m_UserName.isEmpty()) {
        QMessageBox msgBox;
        msgBox.setText("No user logged in"); 
        msgBox.exec();
        return false;
    }

    StartNewRoundViewModel startNewRoundData;
    startNewRoundData.m_GameId = m_GlobalData->m_GameData.m_GameId;
    startNewRoundData.m_OwnUserId = m_GlobalData->m_GameData.m_UserId;
    startNewRoundData.m_OpponentUserId = m_GlobalData->m_GameData.m_OtherUserId;

    m_RequestData = startNewRoundData.toJson();
    
    makeRequestBasis(true);
    return true;
}

void StartNewRoundCommObj::finishedRequest()
{
    QJsonObject retJson;
    if (!finishRequestHelper(retJson)) 
        return;

    long int roundId = retJson.value("roundId").toString().toLong(); //TODO: to add validation
    m_GlobalData->m_GameData.m_RoundId = roundId;
    
    m_MultiRound->initRound();
    
    emit startNewRound();

}

bool StartNewRoundCommObj::validateReply(const QJsonObject& reply) {
    if  (!(reply.contains("roundId") && reply.contains("newRoundCreated")))
        return false;
    
      if (!checkLong(reply.value("roundId").toString()))
        return false;
      
      if (!reply.value("newRoundCreated").isBool())
          return false;
      
      return true;
}

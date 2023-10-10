#include "startnewroundcommobj.h"

#include <QMessageBox>
#include "multiplayerround.h"


bool StartNewRoundCommObj::makeRequest()
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

    m_RequestData = prepareViewModel().toJson();
    
    makeRequestBasis(true);
    return true;
}

StartNewRoundViewModel StartNewRoundCommObj::prepareViewModel() {
    StartNewRoundViewModel startNewRoundData;
    startNewRoundData.m_GameId = m_GlobalData->m_GameData.m_GameId;
    startNewRoundData.m_UserId = m_GlobalData->m_GameData.m_UserId;
    startNewRoundData.m_Username = m_GlobalData->m_UserData.m_UserName;
    startNewRoundData.m_OpponentUserId = m_GlobalData->m_GameData.m_OtherUserId;
    return startNewRoundData;
}

void StartNewRoundCommObj::finishedRequest()
{
    QJsonObject retJson;
    if (!finishRequestHelper(retJson)) 
        return;

    processResponse(retJson);
}

void StartNewRoundCommObj::processResponse(const QJsonObject& retJson) {
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

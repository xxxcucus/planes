#include "startnewroundcommobj.h"

#include <QMessageBox>
#include "multiplayerround.h"
#include "viewmodels/startnewroundviewmodel.h"

bool StartNewRoundCommObj::makeRequest()
{
    //TODO: check that multiplayer
    if (m_GlobalData->m_UserData.m_UserName.isEmpty()) {
        QMessageBox msgBox;
        msgBox.setText("No user logged in"); 
        msgBox.exec();
        return false;
    }

    StartNewRoundViewModel startNewRoundData;
    startNewRoundData.m_GameId = m_GlobalData->m_GameData.m_GameId;


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
    return (reply.contains("roundId") && reply.contains("newRoundCreated"));   
}

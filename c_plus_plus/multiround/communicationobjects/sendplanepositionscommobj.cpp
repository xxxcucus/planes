#include "sendplanepositionscommobj.h"

#include <QMessageBox>
#include "multiplayerround.h"

bool SendPlanePositionsCommObj::makeRequest()
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

PlanesPositionsViewModel SendPlanePositionsCommObj::prepareViewModel() {

    PlanesPositionsViewModel planesPositionsData;
    planesPositionsData.m_GameId = m_GlobalData->m_GameData.m_GameId;
    planesPositionsData.m_RoundId = m_GlobalData->m_GameData.m_RoundId;
    planesPositionsData.m_UserId = m_GlobalData->m_UserData.m_UserId;
    planesPositionsData.m_OpponentUserId = m_GlobalData->m_GameData.m_OtherUserId;
    planesPositionsData.m_Username = m_GlobalData->m_UserData.m_UserName;

    Plane pl1;
    Plane pl2;
    Plane pl3;


    m_MultiRound->getPlayerPlaneNo(0, pl1);
    m_MultiRound->getPlayerPlaneNo(1, pl2);
    m_MultiRound->getPlayerPlaneNo(2, pl3);

    planesPositionsData.m_Plane1X = pl1.row();
    planesPositionsData.m_Plane1Y = pl1.col();
    planesPositionsData.m_Plane1Orient = pl1.orientation();
    planesPositionsData.m_Plane2X = pl2.row();
    planesPositionsData.m_Plane2Y = pl2.col();
    planesPositionsData.m_Plane2Orient = pl2.orientation();
    planesPositionsData.m_Plane3X = pl3.row();
    planesPositionsData.m_Plane3Y = pl3.col();
    planesPositionsData.m_Plane3Orient = pl3.orientation();

    return planesPositionsData;

}

void SendPlanePositionsCommObj::finishedRequest()
{
    QJsonObject retJson;
    if (!finishRequestHelper(retJson)) 
        return;

    bool roundCancelled = retJson.value("cancelled").toBool();
    
    if (roundCancelled) {
        m_MultiRound->setRoundCancelled();
        //activateStartGameTab();
        emit roundCancelled;
        return;
    }
    
    processResponse(retJson);
}

void SendPlanePositionsCommObj::processResponse(const QJsonObject& retJson) {
    bool otherPositionsExist = retJson.value("otherExist").toBool();
    if (otherPositionsExist) {
        int plane1_x = retJson.value("plane1_x").toInt();
        int plane1_y = retJson.value("plane1_y").toInt();
        int plane1_orient = retJson.value("plane1_orient").toInt();
        int plane2_x = retJson.value("plane2_x").toInt();
        int plane2_y = retJson.value("plane2_y").toInt();
        int plane2_orient = retJson.value("plane2_orient").toInt();
        int plane3_x = retJson.value("plane3_x").toInt();
        int plane3_y = retJson.value("plane3_y").toInt();
        int plane3_orient = retJson.value("plane3_orient").toInt();
        //qDebug() << "Plane 1 from opponent " << plane1_x << " " << plane1_y << " " << plane1_orient;
        //qDebug() << "Plane 2 from opponent" << plane2_x << " " << plane2_y << " " << plane2_orient;
        //qDebug() << "Plane 3 from opponent" << plane3_x << " " << plane3_y << " " << plane3_orient;
        bool setOk = m_MultiRound->setComputerPlanes(plane1_x, plane1_y, (Plane::Orientation)plane1_orient, plane2_x, plane2_y, (Plane::Orientation)plane2_orient, plane3_x, plane3_y, (Plane::Orientation)plane3_orient);
        if (!setOk) {
            if (m_ParentWidget != nullptr) {
                QMessageBox msgBox(m_ParentWidget);
                msgBox.setText("Planes positions from opponent are not valid");
                msgBox.exec();
                return;
            }
        }
        emit opponentPlanePositionsReceived();
    }
    else {
        m_MultiRound->setCurrentStage(AbstractPlaneRound::GameStages::WaitForOpponentPlanesPositions);
        emit waitForOpponentPlanePositions();
    }
}

bool SendPlanePositionsCommObj::validateReply(const QJsonObject& reply) {
      if  (!((reply.contains("otherExist") && reply.contains("cancelled") && reply.contains("plane1_x") && 
        reply.contains("plane1_y") && reply.contains("plane1_orient") && 
        reply.contains("plane2_x") && reply.contains("plane2_y") && reply.contains("plane2_orient") &&
        reply.contains("plane3_x") && reply.contains("plane3_y") && reply.contains("plane3_orient"))))
          return false;
          
      if (!reply.value("otherExist").isBool() || !reply.value("cancelled").isBool())
          return false;
      
      if (!reply.value("plane1_x").isDouble() || !reply.value("plane1_y").isDouble() && !reply.value("plane1_orient").isDouble() ||
        !reply.value("plane2_x").isDouble() || !reply.value("plane2_y").isDouble() || !reply.value("plane2_orient").isDouble() ||
        !reply.value("plane3_x").isDouble() || !reply.value("plane3_y").isDouble() || !reply.value("plane3_orient").isDouble())
          return false;  
      
      if (!checkInt(reply.value("plane1_x")) || !checkInt(reply.value("plane1_y")) && !checkInt(reply.value("plane1_orient")) ||
        !checkInt(reply.value("plane2_x")) || !checkInt(reply.value("plane2_y")) || !checkInt(reply.value("plane2_orient")) ||
        !checkInt(reply.value("plane3_x")) || !checkInt(reply.value("plane3_y")) || !checkInt(reply.value("plane3_orient")))
          return false;  
          
      return true;
}

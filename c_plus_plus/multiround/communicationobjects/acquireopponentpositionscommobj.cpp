#include "acquireopponentpositionscommobj.h"

#include <QMessageBox>

#include "viewmodels/getopponentplanespositionsviewmodel.h"
#include "multiplayerround.h"

bool AcquireOpponentPositionsCommObj::makeRequest()
{
    if (m_GlobalData->m_UserData.m_UserName.isEmpty()) {
        QMessageBox msgBox;
        msgBox.setText("No user logged in"); 
        msgBox.exec();
        return false;
    }
    
    GetOpponentsPlanesPositionsViewModel opponentViewModel;
    opponentViewModel.m_RoundId = m_GlobalData->m_GameData.m_RoundId;
    opponentViewModel.m_UserId = m_GlobalData->m_GameData.m_OtherUserId; 

    m_RequestData = opponentViewModel.toJson();
        
    makeRequestBasis(true);
    return true;
}

void AcquireOpponentPositionsCommObj::finishedRequest()
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
    
    bool otherPositionsExist = retJson.value("otherExist").toBool();

    if (!otherPositionsExist) {
        QMessageBox msgBox;
        msgBox.setText("Opponents' planes positions are not available yet!"); 
        msgBox.exec();
        return;
    }

        
    //TODO treat errors
        int plane1_x = retJson.value("plane1_x").toInt();
        int plane1_y = retJson.value("plane1_y").toInt();
        int plane1_orient = retJson.value("plane1_orient").toInt(); //TODO to check this
        int plane2_x = retJson.value("plane2_x").toInt();
        int plane2_y = retJson.value("plane2_y").toInt();
        int plane2_orient = retJson.value("plane2_orient").toInt(); //TODO to check this
        int plane3_x = retJson.value("plane3_x").toInt();
        int plane3_y = retJson.value("plane3_y").toInt();
        int plane3_orient = retJson.value("plane3_orient").toInt(); //TODO to check this        
        qDebug() << "Plane 1 from opponent " << plane1_x << " " << plane1_y << " " << plane1_orient;
        qDebug() << "Plane 2 from opponent" << plane2_x << " " << plane2_y << " " << plane2_orient;
        qDebug() << "Plane 3 from opponent" << plane3_x << " " << plane3_y << " " << plane3_orient;
        bool setOk = m_MultiRound->setComputerPlanes(plane1_x, plane1_y, (Plane::Orientation)plane1_orient, plane2_x, plane2_y, (Plane::Orientation)plane2_orient, plane3_x, plane3_y, (Plane::Orientation)plane3_orient);
        if (!setOk) {
            QMessageBox msgBox;
            msgBox.setText("Planes positions from opponent are not valid"); 
            msgBox.exec();
            return;            
        }
        emit opponentPlanePositionsReceived();
        //activateGameTabDeactivateButtons();
    
}

bool AcquireOpponentPositionsCommObj::validateReply(const QJsonObject& reply) {
      return (reply.contains("otherExist") && reply.contains("cancelled") && reply.contains("plane1_x") && 
        reply.contains("plane1_y") && reply.contains("plane1_orient") && 
        reply.contains("plane2_x") && reply.contains("plane2_y") && reply.contains("plane2_orient") &&
        reply.contains("plane3_x") && reply.contains("plane3_y") && reply.contains("plane3_orient"));
}

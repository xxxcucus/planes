#ifndef __PLANES_POSITIONS_VIEW_MODEL__
#define __PLANES_POSITIONS_VIEW_MODEL__

#include <QJsonObject>
#include "plane.h"
#include "basisrequestviewmodel.h"

struct PlanesPositionsViewModel: public BasisRequestViewModel {
    long int m_GameId;
    long int m_RoundId;
    long int m_OpponentUserId;
    int m_Plane1X;
    int m_Plane1Y;
    Plane::Orientation m_Plane1Orient;
    int m_Plane2X;
    int m_Plane2Y;
    Plane::Orientation m_Plane2Orient;
    int m_Plane3X;
    int m_Plane3Y;
    Plane::Orientation m_Plane3Orient;
    
    QJsonObject toJson() {
        QJsonObject retVal = BasisRequestViewModel::toJson();
        retVal.insert("gameId", QString::number(m_GameId));
        retVal.insert("roundId", QString::number(m_RoundId));
        retVal.insert("opponentUserId", QString::number(m_OpponentUserId));
        retVal.insert("plane1_x", m_Plane1X);
        retVal.insert("plane1_y", m_Plane1Y);
        retVal.insert("plane1_orient", (int)m_Plane1Orient);
        retVal.insert("plane2_x", m_Plane2X);
        retVal.insert("plane2_y", m_Plane2Y);
        retVal.insert("plane2_orient", (int)m_Plane2Orient);
        retVal.insert("plane3_x", m_Plane3X);
        retVal.insert("plane3_y", m_Plane3Y);
        retVal.insert("plane3_orient", (int)m_Plane3Orient);
        return retVal;
    }
    
};















#endif

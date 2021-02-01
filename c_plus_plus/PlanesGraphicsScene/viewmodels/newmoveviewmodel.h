#ifndef __NEW_MOVE_VIEWMODEL__
#define __NEW_MOVE_VIEWMODEL__


struct NewMoveViewModel {
    long int m_GameId;
    long int m_RoundId;
    long int m_OwnUserId;
    long int m_OpponentUserId;
    int m_OwnMoveIndex;
    int m_OpponentMoveIndex;
    int m_MoveX;
    int m_MoveY;
    
    QJsonObject toJson() {
        QJsonObject retVal;
        retVal.insert("gameId", QString::number(m_GameId));
        retVal.insert("roundId", QString::number(m_RoundId));
        retVal.insert("ownUserId", QString::number(m_OwnUserId));
        retVal.insert("opponentUserId", QString::number(m_OpponentUserId));
        retVal.insert("ownMoveIndex", m_OwnMoveIndex); 
        retVal.insert("opponentMoveIndex", m_OpponentMoveIndex);
        retVal.insert("moveX", m_MoveX);
        retVal.insert("moveY", m_MoveY);
        return retVal;
    }
};






















#endif

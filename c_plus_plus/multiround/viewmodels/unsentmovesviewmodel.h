#ifndef __UNSENT_MOVES_VIEWMODEL__
#define __UNSENT_MOVES_VIEWMODEL__

#include <vector>
#include <QString>
#include <QJsonObject>
#include <QJsonArray>

#include "basisrequestviewmodel.h"

struct SingleMoveViewModel {
  int m_MoveX;
  int m_MoveY;
  int m_MoveIndex;
  
  QJsonObject toJson() {
      QJsonObject retVal;
      retVal.insert("moveIndex", m_MoveIndex);
      retVal.insert("moveX", m_MoveX);
      retVal.insert("moveY", m_MoveY);
      return retVal;
  }
};


struct UnsentMovesViewModel : public BasisRequestViewModel {
    long int m_GameId;
    long int m_RoundId;
    long int m_OpponentUserId;
    std::vector<SingleMoveViewModel> m_NotSentMovesIndex;
    int m_OpponentMoveIndex;
    std::vector<int> m_NotReceivedMoveIndex;
    
    QJsonObject toJson() {
        QJsonObject retVal = BasisRequestViewModel::toJson();
        retVal.insert("gameId", QString::number(m_GameId));
        retVal.insert("roundId", QString::number(m_RoundId));
        retVal.insert("opponentUserId", QString::number(m_OpponentUserId));
        retVal.insert("opponentMoveIndex", m_OpponentMoveIndex);
        
        QJsonArray movesArray;
        for (auto singlemove : m_NotSentMovesIndex) {
            QJsonObject obj = singlemove.toJson();
            movesArray.append(obj);
        }
        
        retVal.insert("listMoves", movesArray);
        
        QJsonArray notReceivedArray;
        for (auto idx: m_NotReceivedMoveIndex) {
            QJsonValue jsonValue(idx);
            notReceivedArray.append(jsonValue);
        }
        
        retVal.insert("listNotReceivedMoves", notReceivedArray);
        
        return retVal;
    }
};














#endif

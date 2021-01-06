#ifndef __MULTIPLAYER_ROUND__
#define __MULTIPLAYER_ROUND__

#include "abstractplaneround.h"


class MultiplayerRound : public AbstractPlaneRound {
    
private:
    long int m_RoundId = 0;
    
    
public:
    MultiplayerRound(int rows, int cols, int planeNo);
    void reset() override;
    void initRound() override;

    void playerGuess(const GuessPoint& gp, PlayerGuessReaction& pgr) override;
    void playerGuessIncomplete(int row, int col, GuessPoint::Type& guessRes, PlayerGuessReaction& pgr) override;
    
    void setRoundId(long int id) {
        m_RoundId = id;
    }
    long int getRoundId() {
        return m_RoundId;
    }
};













#endif

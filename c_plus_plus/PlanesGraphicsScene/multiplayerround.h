#ifndef __MULTIPLAYER_ROUND__
#define __MULTIPLAYER_ROUND__

#include "abstractplaneround.h"


class MultiplayerRound : public AbstractPlaneRound {
    
    
public:
    MultiplayerRound(int rows, int cols, int planeNo);
    void reset() override;
    void initRound() override;

    void playerGuess(const GuessPoint& gp, PlayerGuessReaction& pgr) override;
    void playerGuessIncomplete(int row, int col, GuessPoint::Type& guessRes, PlayerGuessReaction& pgr) override;
};













#endif

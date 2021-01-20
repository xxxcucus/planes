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
    
    void getPlayerPlaneNo(int pos, Plane& pl);
    bool setComputerPlanes(int plane1_x, int plane1_y, Plane::Orientation plane1_orient, int plane2_x, int plane2_y, Plane::Orientation plane2_orient, int plane3_x, int plane3_y, Plane::Orientation plane3_orient); 
};













#endif

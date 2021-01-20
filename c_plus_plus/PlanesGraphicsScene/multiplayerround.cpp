#include "multiplayerround.h"


MultiplayerRound::MultiplayerRound(int rows, int cols, int planeNo)
    : AbstractPlaneRound(rows, cols, planeNo)
{
    reset();
    initRound();
}

//resets the PlaneRound object
void MultiplayerRound::reset()
{
    AbstractPlaneRound::reset();
}

void MultiplayerRound::initRound()
{
    AbstractPlaneRound::initRound();
}

void MultiplayerRound::playerGuess(const GuessPoint& gp, PlayerGuessReaction& pgr) {
}

void MultiplayerRound::playerGuessIncomplete(int row, int col, GuessPoint::Type& guessRes, PlayerGuessReaction& pgr) {
}

//TODO to add function in plane round as well and in AbstractPlaneRound
void MultiplayerRound::getPlayerPlaneNo(int pos, Plane& pl) {
    m_PlayerGrid->getPlane(pos, pl);
}

bool MultiplayerRound::setComputerPlanes(int plane1_x, int plane1_y, Plane::Orientation plane1_orient, int plane2_x, int plane2_y, Plane::Orientation plane2_orient, int plane3_x, int plane3_y, Plane::Orientation plane3_orient) {
    return m_ComputerGrid->initGridByUser(plane1_x, plane1_y, plane1_orient, plane2_x, plane2_y, plane2_orient, plane3_x, plane3_y, plane3_orient);
}

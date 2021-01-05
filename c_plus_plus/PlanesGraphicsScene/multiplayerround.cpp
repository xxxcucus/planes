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

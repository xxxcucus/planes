#include "planeround.h"

#include <QDebug>
#include <cstdlib>
#include "coordinate2d.h"

//constructor
PlaneRound::PlaneRound(PlaneGrid* playerGrid, PlaneGrid* computerGrid, ComputerLogic* logic, bool isComputerFirst):
    m_isComputerFirst(isComputerFirst),
    m_PlayerGrid(playerGrid),
    m_ComputerGrid(computerGrid),
    m_computerLogic(logic)
{
    reset();
}

//resets the PlaneRound object
void PlaneRound::reset()
{
    m_PlayerGrid->resetGrid();
    m_ComputerGrid->resetGrid();

    m_playerGuessList.clear();
    m_computerGuessList.clear();

    m_gameStats.reset();
    m_computerLogic->reset();
}

//starts the round waiting for the player to edit his grid
void PlaneRound::initRound()
{
    m_PlayerGrid->initGrid();
    emit displayStatusMessage("Player edit your grid");
    m_ComputerGrid->initGrid();
}

//starts to play
void PlaneRound::play()
{
    m_isComputerFirst = !m_isComputerFirst;
    reset();
    //waits for the player to finish the drawing and draws the planes for the computer
    initRound();

    emit initGraphics();
    emit statsUpdated(m_gameStats);
    //after the player has finished editing the board
    //a signal is emited and the corresponding slot
    //deals with the player move
}

//returns whether the round endet or not and whether the player is the winner
bool PlaneRound::isRoundEndet(bool& isPlayerWinner) const
{
    //at equal scores computer wins
    isPlayerWinner = false;

    bool playerFinished = enoughGuesses(m_PlayerGrid, m_computerGuessList);
    bool computerFinished = enoughGuesses(m_ComputerGrid, m_playerGuessList);

    if (!computerFinished && playerFinished)
        isPlayerWinner=true;

    return(playerFinished || computerFinished);
}

//decides whether all the planes have been guessed
bool PlaneRound::enoughGuesses(PlaneGrid* pg, const std::vector<GuessPoint>& guessList ) const
{
    int count = 0;

    for(unsigned int i = 0; i < guessList.size(); i++) {
        GuessPoint gp = guessList.at(i);
        if (gp.m_type == GuessPoint::Dead)
            count++;
    }

    return (count >= pg->getPlaneNo());
}

//guesses a computer move
GuessPoint PlaneRound::guessComputerMove()
{
    PlanesCommonTools::Coordinate2D qp;
    //use the computer strategy to get a move
    m_computerLogic->makeChoice(qp);

    //use the player grid to see the result of the grid
    GuessPoint::Type tp = m_PlayerGrid->getGuessResult(qp);
    GuessPoint gp(qp.x(), qp.y(), tp);

    //add the data to the computer strategy
    m_computerLogic->addData(gp);

    //update the computer guess list
    m_computerGuessList.push_back(gp);

    return gp;
}

//request a move from the player
void PlaneRound::readPlayerMove() const
{
    emit needPlayerGuess();
}

//treats a player's guess
void PlaneRound::receivedPlayerGuess(const GuessPoint& gp)
{
    //update the game statistics
    updateGameStats(gp, false);
    //add the player's guess to the list of guesses
    //assume that the guess is different from the other guesses
    m_playerGuessList.push_back(gp);

    //if the player is  first
    //run the computer's move
    if (!m_isComputerFirst)
    {
        GuessPoint gp = guessComputerMove();
        updateGameStats(gp, true);
        emit computerMoveGenerated(gp);
    }

    //play step is finished
    //verify if round is finished
    bool isComputerWinner = false;
    if (!isRoundEndet(isComputerWinner))
        playStep();
    else
    {
        QString text = tr("Round endet.");

        if (isComputerWinner) {
            text += tr(" Computer is winner!");
            m_gameStats.updateWins(true);
            emit statsUpdated(m_gameStats);
        } else {
            text+=tr(" Player is winner!");
            m_gameStats.updateWins(false);
            emit statsUpdated(m_gameStats);
        }

        emit displayStatusMessage(text);
        emit roundEnds(!isComputerWinner);
    }
}

//plays one computer move and one player move
void PlaneRound::playStep()
{
    //if computer is first guesses the computer move
    //and waits for the player's move
    if (m_isComputerFirst) {
        GuessPoint gp = guessComputerMove();
        updateGameStats(gp, true);
        emit computerMoveGenerated(gp);
        emit needPlayerGuess();
        emit displayStatusMessage(tr("Player's turn"));
    } else {
    //if player is first waits for the player's guess
        emit needPlayerGuess();
    }
}

//based on a guesspoint updates the game stats
void PlaneRound::updateGameStats(const GuessPoint& gp, bool isComputer)
{
    m_gameStats.updateStats(gp, isComputer);
    emit statsUpdated(m_gameStats);
}

#ifndef PLANEGAME_H
#define PLANEGAME_H

#include "plane.h"
#include "planegrid.h"
#include "computerlogic.h"
#include "gamestatistics.h"
#include <QObject>

//implements the control logic for playing one round of planes
class PlaneRound: public QObject
{
    Q_OBJECT

    //whether the computer or the player moves first
    bool m_isComputerFirst;
    //the  game statistics
    GameStatistics m_gameStats;

    //the player and computer's grid
    PlaneGrid* m_PlayerGrid;
    PlaneGrid* m_ComputerGrid;

    //the list of guesses for computer and player
    std::vector<GuessPoint> m_computerGuessList;
    std::vector<GuessPoint> m_playerGuessList;

    //the computer's strategy
    ComputerLogic* m_computerLogic;

public:
    //constructs the round object
    PlaneRound(PlaneGrid* playerGrid, PlaneGrid* computerGrid, ComputerLogic* logic, bool isComputerFirst);
    //returns whether the round has ended or not and gives the winner
    bool isRoundEndet(bool& isPlayerWinner) const;
    //based on the available information makes the next move for the computer
    GuessPoint guessComputerMove();

    //resets the round
    void reset();

    //tests whether all of the planes have been guessed
    bool enoughGuesses(PlaneGrid* pg, const std::vector<GuessPoint>& guessList) const;
    //inits a new round
    void initRound();
    //update game statistics
    void updateGameStats(const GuessPoint& gp, bool isComputer);

signals:
    //signals that a guess from the player is needed
    void needPlayerGuess() const;
    //signals that computer has generated a move
    void computerMoveGenerated(const GuessPoint& gp);
    //signals that a message has to be displayed in the edit control window
    void displayStatusMessage(const std::string& text);
    //signals that the round has endet
    void roundEnds(bool isPlayerWinner);
    //signals that the render areas should be reset
    void initGraphics();
    //signals that statistics have to be updated on screen
    void statsUpdated(const GameStatistics& gs);

public slots:

    //received a player guess from the computer render area
    void receivedPlayerGuess(const GuessPoint& gp);
    //plays one step
    void playStep();
    //plays the game
    void play();
};



#endif // PLANEGAME_H

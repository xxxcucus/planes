#ifndef PLANEGAMEQML_H
#define PLANEGAMEQML_H

#include <QObject>
#include "planesmodel.h"
#include "planeround.h"
#include "guesspoint.h"

class PlaneGameQML : public QObject
{
    Q_OBJECT
public:
    PlaneGameQML();

public:
    Q_INVOKABLE void doneEditing();
    Q_INVOKABLE inline int getPlayerMoves() { return m_Stats.m_playerMoves; }
    Q_INVOKABLE inline int getPlayerHits() { return m_Stats.m_playerHits; }
    Q_INVOKABLE inline int getPlayerDead() { return m_Stats.m_playerDead; }
    Q_INVOKABLE inline int getPlayerMisses() { return m_Stats.m_playerMisses; }
    Q_INVOKABLE inline int getPlayerWins() { return m_Stats.m_playerWins; }
    Q_INVOKABLE inline int getComputerMoves() { return m_Stats.m_computerMoves; }
    Q_INVOKABLE inline int getComputerHits() { return m_Stats.m_computerHits; }
    Q_INVOKABLE inline int getComputerDead() { return m_Stats.m_computerDead; }
    Q_INVOKABLE inline int getComputerMisses() { return m_Stats.m_computerMisses; }
    Q_INVOKABLE inline int getComputerWins() { return m_Stats.m_computerWins; }

    Q_INVOKABLE void startNewGame();

    inline PlaneGrid* playerGrid() { return mPlanesModel->playerGrid(); }
    inline PlaneGrid* computerGrid() { return mPlanesModel->computerGrid(); }

signals:
    void guessMade(const GuessPoint& gp);
    void computerMoveGenerated(const GuessPoint& gp);    
    void updateStats();
    void roundEnds(bool isPlayerWinner);
	void resetGrid();

public slots:
    void statsUpdated(const GameStatistics& stats);
	void receivedPlayerGuess(const GuessPoint& gp);

private:
    //The model object
    PlanesModel* mPlanesModel;
    //The controller object
    PlaneRound* mRound;

    GameStatistics m_Stats;
};

#endif // PLANEGAMEQML_H

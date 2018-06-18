#include "planegameqml.h"
#include <QDebug>

PlaneGameQML::PlaneGameQML()
{
    //builds the model object
    mPlanesModel = new PlanesModel(10, 10, 3);

    //builds the game object - the controller
    mRound = new PlaneRound(mPlanesModel->playerGrid(), mPlanesModel->computerGrid(), mPlanesModel->computerLogic(), false);
    connect(this, SIGNAL(guessMade(const GuessPoint&)), mRound, SLOT(receivedPlayerGuess(const GuessPoint&)));
    connect(mRound, SIGNAL(computerMoveGenerated(const GuessPoint&)), this, SIGNAL(computerMoveGenerated(const GuessPoint&)));
    connect(mRound, SIGNAL(statsUpdated(const GameStatistics&)), this, SLOT(statsUpdated(const GameStatistics&)));
    connect(mRound, SIGNAL(roundEnds(bool)), this, SIGNAL(roundEnds(bool)));
    mRound->play();

}

void PlaneGameQML::doneEditing() {
    mRound->playStep();
}

void PlaneGameQML::statsUpdated(const GameStatistics& stats) {
    m_Stats = stats;
    emit updateStats();
}

void PlaneGameQML::startNewGame() {
    mRound->play();
}
///controls for editing the player's board in the first round of the game
//OK - connect(m_LeftPane, SIGNAL(selectPlaneClicked(bool)), m_RightPane, SLOT(selectPlaneClicked(bool)));
//OK - connect(m_LeftPane, SIGNAL(rotatePlaneClicked(bool)), m_RightPane, SLOT(rotatePlaneClicked(bool)));
//OK - connect(m_LeftPane, SIGNAL(upPlaneClicked(bool)), m_RightPane, SLOT(upPlaneClicked(bool)));
//OK - connect(m_LeftPane, SIGNAL(downPlaneClicked(bool)), m_RightPane, SLOT(downPlaneClicked(bool)));
//OK - connect(m_LeftPane, SIGNAL(leftPlaneClicked(bool)), m_RightPane, SLOT(leftPlaneClicked(bool)));
//OK - connect(m_LeftPane, SIGNAL(rightPlaneClicked(bool)), m_RightPane, SLOT(rightPlaneClicked(bool)));
//connect(m_LeftPane, SIGNAL(doneClicked(bool)), m_RightPane, SLOT(doneClicked(bool)));

///activate the board editing tab in the left pane
//connect(m_playerGrid, SIGNAL(initPlayerGrid()), this, SLOT(activateBoardEditingTab()));
///reset the game board when starting the game
//connect(m_round, SIGNAL(initGraphics()), m_RightPane, SLOT(resetGameBoard()));
//connect(m_round, SIGNAL(initGraphics()), m_LeftPane, SLOT(activateEditingBoard()));

//connect(m_RightPane, SIGNAL(planePositionNotValid(bool)), m_LeftPane, SLOT(activateDoneButton(bool)));
//OK - connect(m_LeftPane, SIGNAL(doneClicked(bool)), m_round, SLOT(playStep()));
//OK - connect(m_round, SIGNAL(computerMoveGenerated(const GuessPoint&)), m_RightPane, SIGNAL(showComputerMove(const GuessPoint&)));
//OK - connect(m_RightPane, SIGNAL(guessMade(const GuessPoint&)), m_round, SLOT(receivedPlayerGuess(const GuessPoint&)));
//connect(m_round, SIGNAL(displayStatusMessage(QString)), this, SLOT(displayStatusMsg(QString)));
//OK - connect(m_round, SIGNAL(roundEnds(bool)), m_LeftPane, SLOT(endRound(bool)));
//connect(m_round, SIGNAL(roundEnds(bool)), m_RightPane, SLOT(endRound(bool)));
//connect(m_LeftPane, SIGNAL(startNewGame()), m_round, SLOT(play()));
//connect(m_LeftPane, SIGNAL(startNewGame()), m_RightPane, SLOT(startNewGame()));
//connect(m_round, SIGNAL(statsUpdated(const GameStatistics&)), m_LeftPane, SLOT(updateGameStatistics(const GameStatistics&)));

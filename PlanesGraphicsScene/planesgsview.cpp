#include "planesgsview.h"

#include <QHBoxLayout>
#include "customhorizlayout.h"

PlanesGSView::PlanesGSView(PlaneGrid *pGrid, PlaneGrid* cGrid, ComputerLogic* cLogic, PlaneRoundJavaFx *rd, QWidget *parent)
    : QWidget(parent), m_playerGrid(pGrid), m_computerGrid(cGrid), m_round(rd)
{
    CustomHorizLayout* hLayout = new CustomHorizLayout(this);
    m_LeftPane = new LeftPane(this);
    m_LeftPane->setMinWidth();
    //m_LeftPane->setSizePolicy(QSizePolicy::Fixed, QSizePolicy::Expanding);

    m_RightPane = new RightPane(*m_playerGrid, *m_computerGrid, this);
    m_RightPane->setMinWidth();

    hLayout->addWidget(m_LeftPane);
    hLayout->addWidget(m_RightPane);
    setLayout(hLayout);

    ///controls for editing the player's board in the first round of the game
    connect(m_LeftPane, SIGNAL(selectPlaneClicked(bool)), m_RightPane, SLOT(selectPlaneClicked(bool)));
    connect(m_LeftPane, SIGNAL(rotatePlaneClicked(bool)), m_RightPane, SLOT(rotatePlaneClicked(bool)));
    connect(m_LeftPane, SIGNAL(upPlaneClicked(bool)), m_RightPane, SLOT(upPlaneClicked(bool)));
    connect(m_LeftPane, SIGNAL(downPlaneClicked(bool)), m_RightPane, SLOT(downPlaneClicked(bool)));
    connect(m_LeftPane, SIGNAL(leftPlaneClicked(bool)), m_RightPane, SLOT(leftPlaneClicked(bool)));
    connect(m_LeftPane, SIGNAL(rightPlaneClicked(bool)), m_RightPane, SLOT(rightPlaneClicked(bool)));
    connect(m_LeftPane, SIGNAL(doneClicked(bool)), m_RightPane, SLOT(doneClicked(bool)));

    ///activate the board editing tab in the left pane
    //connect(m_playerGrid, SIGNAL(initPlayerGrid()), this, SLOT(activateBoardEditingTab()));
    ///reset the game board when starting the game
    /*connect(m_round, SIGNAL(initGraphics()), m_RightPane, SLOT(resetGameBoard()));
    connect(m_round, SIGNAL(initGraphics()), m_LeftPane, SLOT(activateEditingBoard()));*/

    connect(m_RightPane, SIGNAL(planePositionNotValid(bool)), m_LeftPane, SLOT(activateDoneButton(bool)));
    /*connect(m_LeftPane, SIGNAL(doneClicked(bool)), m_round, SLOT(playStep()));
    connect(m_round, SIGNAL(computerMoveGenerated(const GuessPoint&)), m_RightPane, SIGNAL(showComputerMove(const GuessPoint&)));
    connect(m_RightPane, SIGNAL(guessMade(const GuessPoint&)), m_round, SLOT(receivedPlayerGuess(const GuessPoint&)));
    connect(m_round, SIGNAL(displayStatusMessage(const std::string&)), this, SLOT(displayStatusMsg(const std::string&)));
    connect(m_round, SIGNAL(roundEnds(bool)), m_LeftPane, SLOT(endRound(bool)));
    connect(m_round, SIGNAL(roundEnds(bool)), m_RightPane, SLOT(endRound(bool)));
    connect(m_LeftPane, SIGNAL(startNewGame()), m_round, SLOT(play()));
    connect(m_LeftPane, SIGNAL(startNewGame()), m_RightPane, SLOT(startNewGame()));
    connect(m_round, SIGNAL(statsUpdated(const GameStatistics&)), m_LeftPane, SLOT(updateGameStatistics(const GameStatistics&)));*/

	m_RightPane->resetGameBoard();
	m_LeftPane->activateEditingBoard();
}


void PlanesGSView::displayStatusMsg(const std::string& str)
{
    //qDebug() << "Game Status: " << str.c_str();
}

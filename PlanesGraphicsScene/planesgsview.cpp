#include "planesgsview.h"

#include <QDebug>
#include <QHBoxLayout>

PlanesGSView::PlanesGSView(PlaneGrid *pGrid, PlaneGrid* cGrid, ComputerLogic* cLogic, PlaneRound *rd, QWidget *parent)
    : QWidget(parent), m_playerGrid(pGrid), m_computerGrid(cGrid), m_computerLogic(cLogic), m_round(rd)
{
    QHBoxLayout* hLayout = new QHBoxLayout();
    m_LeftPane = new LeftPane(this);
    m_LeftPane->setSizePolicy(QSizePolicy::Fixed, QSizePolicy::Expanding);

    m_RightPane = new RightPane(*m_playerGrid, *m_computerGrid, this);

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
    connect(m_playerGrid, SIGNAL(initPlayerGrid()), this, SLOT(activateBoardEditingTab()));
    ///reset the game board when starting the game
    connect(m_round, SIGNAL(initGraphics()), m_RightPane, SLOT(resetGameBoard()));
    connect(m_round, SIGNAL(initGraphics()), m_LeftPane, SLOT(activateEditingBoard()));

    connect(m_RightPane, SIGNAL(planePositionNotValid(bool)), m_LeftPane, SLOT(activateDoneButton(bool)));

//    connect(playerGrid, SIGNAL(initPlayerGrid()),
//                     editPlanesWidget, SLOT(initButtons()));
//    connect(playerArea,SIGNAL(displayMsg(QString)),
//                     editPlanesWidget, SLOT(displayMsg(QString)));
//    connect(playerArea, SIGNAL(enoughPlanes()),
//                     editPlanesWidget, SLOT(deactivateAddPlane()));
//    connect(playerArea, SIGNAL(displayStatusMsg(QString)),
//                     editPlanesWidget, SLOT(displayStatusMsg(QString)));


    connect(m_LeftPane, SIGNAL(doneClicked(bool)), m_round, SLOT(playStep()));
    connect(m_round, SIGNAL(computerMoveGenerated(const GuessPoint&)), m_RightPane, SIGNAL(showComputerMove(const GuessPoint&)));
//    connect(round, SIGNAL(needPlayerGuess()),
//                     computerArea, SLOT(activateGameMode()));
    connect(m_RightPane, SIGNAL(guessMade(const GuessPoint&)), m_round, SLOT(receivedPlayerGuess(const GuessPoint&)));
    connect(m_round, SIGNAL(displayStatusMessage(QString)), this, SLOT(displayStatusMsg(QString)));
    connect(m_round, SIGNAL(roundEndet()), m_LeftPane, SLOT(endRound()));
    connect(m_round, SIGNAL(roundEndet()), m_RightPane, SLOT(endRound()));
//    connect(gameStatsWidget, SIGNAL(startGame()),
//                     round, SLOT(play()));
    connect(m_round, SIGNAL(statsUpdated(const GameStatistics&)), m_LeftPane, SLOT(updateGameStatistics(const GameStatistics&)));

//    connect(listWidget, SIGNAL(currentRowChanged(int)),
//                stackedLayout, SLOT(setCurrentIndex(int)));
//    connect(listWidget, SIGNAL(currentRowChanged(int)),
//                this, SLOT(widgetSelected(int)));
//    connect(this, SIGNAL(debugWidgetSelected()),
//            choiceDebugWidget, SLOT(setLogic()));
}


void PlanesGSView::displayStatusMsg(const QString& str)
{
    qDebug() << "Game Status: " << str;
}

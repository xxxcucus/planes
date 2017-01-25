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

    connect(m_playerGrid, SIGNAL(initPlayerGrid()), this, SLOT(activateBoardEditingTab()));

//    connect(playerGrid, SIGNAL(initPlayerGrid()),
//                     editPlanesWidget, SLOT(initButtons()));
//    connect(playerArea, SIGNAL(operationEndet()),
//                     editPlanesWidget, SLOT(cancel_clicked()));
//    connect(playerArea,SIGNAL(displayMsg(QString)),
//                     editPlanesWidget, SLOT(displayMsg(QString)));
//    connect(playerArea, SIGNAL(enoughPlanes()),
//                     editPlanesWidget, SLOT(deactivateAddPlane()));
//    connect(playerArea, SIGNAL(displayStatusMsg(QString)),
//                     editPlanesWidget, SLOT(displayStatusMsg(QString)));
//    connect(playerArea, SIGNAL(notEnoughPlanes()),
//                     editPlanesWidget, SLOT(deactivateDoneButton()));
//    connect(playerArea, SIGNAL(activateDone()),
//                     editPlanesWidget, SLOT(activateDoneButton()));
//    connect(editPlanesWidget, SIGNAL(doneClicked()),
//                     playerArea, SLOT(changeMode()));
//    connect(editPlanesWidget, SIGNAL(doneClicked()),
//                     round, SLOT(playStep()));
//    connect(round, SIGNAL(computerMoveGenerated(GuessPoint)),
//                     playerArea, SLOT(showMove(GuessPoint)));
//    connect(round, SIGNAL(needPlayerGuess()),
//                     computerArea, SLOT(activateGameMode()));
//    connect(computerArea, SIGNAL(guessMade(GuessPoint)),
//                     round, SLOT(receivedPlayerGuess(GuessPoint)));
//    connect(round, SIGNAL(displayStatusMessage(QString)),
//                     editPlanesWidget, SLOT(displayStatusMsg(QString)));
//    connect(round, SIGNAL(roundEndet()),
//                     computerArea, SLOT(roundEndet()));
//    connect(round, SIGNAL(roundEndet()),
//                     gameStatsWidget, SLOT(roundEndet()));
//    connect(gameStatsWidget, SIGNAL(startGame()),
//                     round, SLOT(play()));
    connect(m_round, SIGNAL(initGraphics()),
                     m_RightPane, SLOT(resetGameBoard()));
//    connect(round, SIGNAL(statsUpdated(GameStatistics)),
//                     gameStatsWidget, SLOT(updateStats(GameStatistics)));

//    connect(listWidget, SIGNAL(currentRowChanged(int)),
//                stackedLayout, SLOT(setCurrentIndex(int)));
//    connect(listWidget, SIGNAL(currentRowChanged(int)),
//                this, SLOT(widgetSelected(int)));
//    connect(this, SIGNAL(debugWidgetSelected()),
//            choiceDebugWidget, SLOT(setLogic()));
}




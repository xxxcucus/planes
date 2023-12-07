#include "planesgsview.h"

#include <QHBoxLayout>
#include <QDebug>
#include "customhorizlayout.h"

PlanesGSView::PlanesGSView(PlaneRound *rd, MultiplayerRound* mrd, GlobalData* globalData, QNetworkAccessManager* networkManager, GameInfo* gameInfo, QSettings* settings, StompClient* stompClient, QWidget *parent)
    : QWidget(parent), m_round(rd), m_MultiRound(mrd), m_GlobalData(globalData), m_NetworkManager(networkManager), m_GameInfo(gameInfo), m_Settings(settings), m_StompClient(stompClient)
{
    m_StatusBarWidget = new StatusBarWidget(m_GameInfo, m_GlobalData);
    
    QWidget* controlsAndBoardsWidget = new QWidget();
    CustomHorizLayout* hLayout = new CustomHorizLayout(20, controlsAndBoardsWidget);
    m_LeftPane = new LeftPane(m_GameInfo, m_NetworkManager, m_GlobalData, m_Settings, m_MultiRound, this);
    m_LeftPane->setMinWidth();
    //m_LeftPane->setSizePolicy(QSizePolicy::Fixed, QSizePolicy::Expanding);

    m_RightPane = new RightPane(m_round, m_MultiRound, m_Settings, m_GlobalData, m_NetworkManager, m_GameInfo, m_StompClient, this);
    m_RightPane->setMinWidth();

    hLayout->addWidget(m_LeftPane);
    hLayout->addWidget(m_RightPane);
    controlsAndBoardsWidget->setLayout(hLayout);
    
    QVBoxLayout* vLayout2 = new QVBoxLayout();
    vLayout2->addWidget(m_StatusBarWidget);
    vLayout2->addWidget(controlsAndBoardsWidget);
    setLayout(vLayout2);

    ///controls for editing the player's board in the first round of the game
    connect(m_LeftPane, SIGNAL(selectPlaneClicked(bool)), m_RightPane, SLOT(selectPlaneClicked(bool)));
    connect(m_LeftPane, SIGNAL(rotatePlaneClicked(bool)), m_RightPane, SLOT(rotatePlaneClicked(bool)));
    connect(m_LeftPane, SIGNAL(upPlaneClicked(bool)), m_RightPane, SLOT(upPlaneClicked(bool)));
    connect(m_LeftPane, SIGNAL(downPlaneClicked(bool)), m_RightPane, SLOT(downPlaneClicked(bool)));
    connect(m_LeftPane, SIGNAL(leftPlaneClicked(bool)), m_RightPane, SLOT(leftPlaneClicked(bool)));
    connect(m_LeftPane, SIGNAL(rightPlaneClicked(bool)), m_RightPane, SLOT(rightPlaneClicked(bool)));
    connect(m_LeftPane, SIGNAL(doneClicked()), m_RightPane, SLOT(doneClicked()));
    connect(m_LeftPane, SIGNAL(roundWasCancelled()), this, SLOT(roundWasCancelledSlot()));
    
    connect(m_RightPane, SIGNAL(planePositionNotValid(bool)), m_LeftPane, SLOT(activateDoneButton(bool)));
    connect(m_LeftPane, SIGNAL(doneClicked()), this, SLOT(doneClicked()));
    connect(m_RightPane, SIGNAL(guessMade(const GuessPoint&)), this, SLOT(receivedPlayerGuess(const GuessPoint&)));
    connect(m_LeftPane, SIGNAL(newRoundStarted()), this, SLOT(startNewGame()));
    connect(m_MultiRound, &MultiplayerRound::logoutCompleted, m_StatusBarWidget, &StatusBarWidget::updateSlot);
    connect(m_MultiRound, &MultiplayerRound::userDeactivated, m_StatusBarWidget, &StatusBarWidget::updateSlot);
    connect(m_LeftPane, &LeftPane::newRoundStarted, m_StatusBarWidget, &StatusBarWidget::updateSlot);
    connect(m_StatusBarWidget, &StatusBarWidget::logoutPressed, this, &PlanesGSView::logoutPressed);
    connect(m_MultiRound, SIGNAL(newRoundStarted()), this, SLOT(startNewGame()));
    
    connect(m_MultiRound, &MultiplayerRound::opponentMoveGenerated, this, &PlanesGSView::opponentMoveGeneratedSlot);
    connect(m_MultiRound, &MultiplayerRound::loginCompleted, m_StatusBarWidget, &StatusBarWidget::updateSlot);
    connect(m_MultiRound, &MultiplayerRound::gameCreated, m_StatusBarWidget, &StatusBarWidget::updateSlot);
    connect(m_MultiRound, &MultiplayerRound::gameConnectedTo, m_StatusBarWidget, &StatusBarWidget::updateSlot);   
    connect(m_MultiRound, &MultiplayerRound::refreshStatus, m_StatusBarWidget, &StatusBarWidget::updateSlot);
   
    
	m_round->initRound();
	m_RightPane->resetGameBoard();
    if (m_GameInfo->getSinglePlayer())
        m_LeftPane->activateEditingBoard();
    else
        m_LeftPane->activateAccountWidget();

}

void PlanesGSView::startNewGame() {
	//qDebug() << "Start new round";
    
    if (m_GameInfo->getSinglePlayer()) {
        if (m_round->didRoundEnd()) {
            m_round->initRound();
            m_RightPane->startNewGame();
            m_RightPane->resetGameBoard();
            m_LeftPane->activateEditingBoard();
            m_LeftPane->updateGameStatistics(GameStatistics());
        }
        return;
    }
    
    m_RightPane->startNewGame();
    m_RightPane->resetGameBoard();
}

void PlanesGSView::displayStatusMsg(const std::string& str)
{
    //qDebug() << "Game Status: " << str.c_str();
}

void PlanesGSView::receivedPlayerGuess(const GuessPoint& gp)
{

    if (m_GameInfo->getSinglePlayer()) {
        PlayerGuessReaction pgr;
        m_round->playerGuess(gp, pgr);

        if (pgr.m_ComputerMoveGenerated) {
            m_RightPane->showComputerMove(pgr.m_ComputerGuess);
        }
        if (pgr.m_RoundEnds) {
            printf("Round ends\n");
            m_LeftPane->endRound(!pgr.m_isPlayerWinner);
            m_RightPane->endRound(!pgr.m_isPlayerWinner, pgr.m_isDraw);
            m_round->roundEnds();
        }

        m_LeftPane->updateGameStatistics(pgr.m_GameStats);
    } else {
        PlayerGuessReaction pgr;
        m_MultiRound->playerGuess(gp, pgr);
    }
}

void PlanesGSView::opponentMoveGeneratedSlot(const GuessPoint& gp)
{
    //() << "opponentMoveGeneratedSlot";
    m_RightPane->showComputerMove(gp);
}


void PlanesGSView::doneClicked()
{
	m_round->doneEditing();
	m_LeftPane->activateGameTab();
	m_RightPane->doneClicked();
}

void PlanesGSView::roundWasCancelledSlot() {
    m_round->setRoundCancelled();
    m_RightPane->roundWasCancelledSlot();
}

void PlanesGSView::logoutPressed() {
    m_MultiRound->logout(m_GlobalData->m_UserData.m_UserName);
    m_StatusBarWidget->updateSlot();
}

#include "planesgsview.h"

#include <QHBoxLayout>
#include "customhorizlayout.h"

PlanesGSView::PlanesGSView(PlaneRound *rd, MultiplayerRound* mrd, GlobalData* globalData, QNetworkAccessManager* networkManager, GameInfo* gameInfo, QSettings* settings, QWidget *parent)
    : QWidget(parent), m_round(rd), m_MultiRound(mrd), m_GlobalData(globalData), m_NetworkManager(networkManager), m_GameInfo(gameInfo), m_Settings(settings)
{
    CustomHorizLayout* hLayout = new CustomHorizLayout(20, this);
    m_LeftPane = new LeftPane(m_GameInfo, m_NetworkManager, m_GlobalData, m_Settings, m_MultiRound, this);
    m_LeftPane->setMinWidth();
    //m_LeftPane->setSizePolicy(QSizePolicy::Fixed, QSizePolicy::Expanding);

    m_RightPane = new RightPane(m_round, m_MultiRound, m_Settings, m_GlobalData, m_NetworkManager, m_GameInfo, this);
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
    connect(m_LeftPane, SIGNAL(doneClicked()), m_RightPane, SLOT(doneClicked()));

    connect(m_RightPane, SIGNAL(planePositionNotValid(bool)), m_LeftPane, SLOT(activateDoneButton(bool)));
    connect(m_LeftPane, SIGNAL(doneClicked()), this, SLOT(doneClicked()));
    connect(m_RightPane, SIGNAL(guessMade(const GuessPoint&)), this, SLOT(receivedPlayerGuess(const GuessPoint&)));
    connect(m_LeftPane, SIGNAL(startNewGame()), this, SLOT(startNewGame()));

	m_round->initRound();
	m_RightPane->resetGameBoard();
	m_LeftPane->activateEditingBoard();
}

void PlanesGSView::startNewGame() {
	printf("Start new round\n");
	if (m_round->didRoundEnd()) {
		m_round->initRound();
		m_RightPane->startNewGame();
		m_RightPane->resetGameBoard();
		m_LeftPane->activateEditingBoard();
		m_LeftPane->updateGameStatistics(GameStatistics());
	}
}

void PlanesGSView::displayStatusMsg(const std::string& str)
{
    //qDebug() << "Game Status: " << str.c_str();
}

void PlanesGSView::receivedPlayerGuess(const GuessPoint& gp)
{
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
}

void PlanesGSView::doneClicked()
{
	m_round->doneEditing();
	m_LeftPane->activateGameTab();
	m_RightPane->doneClicked();
}

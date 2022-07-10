#include "rightpane.h"
#include <QGraphicsScene>
#include <QGraphicsView>
#include <QHBoxLayout>
#include <QTextEdit>
#include <QFile>
#include <QTextStream>
#include "options/optionswindow.h"
#include "planeround.h"

RightPane::RightPane(PlaneRound* pr, MultiplayerRound* mrd, QSettings* settings, GlobalData* globalData, QNetworkAccessManager* networkManager, GameInfo* gameInfo, QWidget* parent) : QTabWidget(parent), m_PlaneRound(pr), m_MultiRound(mrd), m_GlobalData(globalData), m_NetworkManager(networkManager), m_Settings(settings), m_GameInfo(gameInfo)
{
    QWidget* helpWidget = new QWidget();
    QHBoxLayout* layout = new QHBoxLayout();
    QTextEdit* textEdit = new QTextEdit();
    QFile file(":/help.html");
    file.open(QFile::ReadOnly | QFile::Text);
    QTextStream stream(&file);
    textEdit->setHtml(stream.readAll());
    textEdit->setTextInteractionFlags(Qt::LinksAccessibleByMouse);
    layout->addWidget(textEdit);
    helpWidget->setLayout(layout);

    QWidget* aboutWidget = new QWidget();
    QHBoxLayout* hlayout1 = new QHBoxLayout();
    QTextEdit* textEdit1 = new QTextEdit();
    QFile file1(":/about.html");
    file1.open(QFile::ReadOnly | QFile::Text);
    QTextStream stream1(&file1);
    textEdit1->setHtml(stream1.readAll());
    textEdit1->setTextInteractionFlags(Qt::LinksAccessibleByMouse);
    hlayout1->addWidget(textEdit1);
    aboutWidget->setLayout(hlayout1);
    
    
    if (m_GameInfo->getSinglePlayer()) {
        m_PlayerBoard = new PlayerBoard(*m_PlaneRound->playerGrid());
        m_ComputerBoard = new ComputerBoard(*m_PlaneRound->computerGrid());
    } else {
        m_PlayerBoard = new PlayerBoard(*m_MultiRound->playerGrid());
        m_ComputerBoard = new ComputerBoard(*m_MultiRound->computerGrid());
    }
	

	OptionsWindow* optionsWindow = new OptionsWindow(m_PlaneRound, m_Settings, m_GameInfo);
    //AccountWidget* accountWidget = new AccountWidget(m_Settings, m_GlobalData, m_NetworkManager, m_GameInfo, m_MultiRound);
    //GameWidget* gameWidget = new GameWidget(m_GlobalData, m_GameInfo, m_NetworkManager, m_Settings, m_MultiRound);

    m_OwnBoardIndex = addTab(m_PlayerBoard->getView(), "Player Board");
    m_OpponentBoardIndex = addTab(m_ComputerBoard->getView(), m_GameInfo->getSinglePlayer() ? "Computer Board" : "Opponent Board");
	addTab(optionsWindow, "Options");
    //m_AccountWidgetIndex = addTab(accountWidget, "Account");
    //m_GameWidgetIndex = addTab(gameWidget, "Game");
    /*if (m_GameInfo->getSinglePlayer()) {
        setTabEnabled(m_AccountWidgetIndex, false);
        setTabEnabled(m_GameWidgetIndex, false);
    }*/
    addTab(helpWidget, "Help");
    addTab(aboutWidget, "About");

    connect(m_PlayerBoard, SIGNAL(planePositionNotValid(bool)), this, SIGNAL(planePositionNotValid(bool)));
    connect(m_ComputerBoard, SIGNAL(guessMade(const GuessPoint&)), this, SIGNAL(guessMade(const GuessPoint&)));
    connect(m_MultiRound, &MultiplayerRound::gameConnectedTo, this, &RightPane::gameConnectedToSlot);
    connect(m_MultiRound, &MultiplayerRound::refreshStatus, this, &RightPane::multiplayerRoundReset);
    connect(m_MultiRound, SIGNAL(roundWasCancelled()), this, SLOT(roundWasCancelledSlot()));
    connect(m_MultiRound, &MultiplayerRound::winnerSent, this, &RightPane::endRound);
}

RightPane::~RightPane()
{
    delete m_PlayerBoard;
    delete m_ComputerBoard;
}

void RightPane::selectPlaneClicked(bool val)
{
    m_PlayerBoard->selectPlaneClicked(val);
}

void RightPane::rotatePlaneClicked(bool val)
{
    m_PlayerBoard->rotatePlaneClicked(val);
}

void RightPane::upPlaneClicked(bool val)
{
    m_PlayerBoard->upPlaneClicked(val);
}

void RightPane::downPlaneClicked(bool val)
{
    m_PlayerBoard->downPlaneClicked(val);
}

void RightPane::leftPlaneClicked(bool val)
{
    m_PlayerBoard->leftPlaneClicked(val);
}

void RightPane::rightPlaneClicked(bool val)
{
    m_PlayerBoard->rightPlaneClicked(val);
}

void RightPane::doneClicked()
{
    setCurrentIndex(m_OpponentBoardIndex);
    m_PlayerBoard->setGameStage(GenericBoard::GameStages::Game);
    m_PlayerBoard->setSelectedPlaneIndex(-1);
    m_ComputerBoard->setGameStage(GenericBoard::GameStages::Game);
}

void RightPane::endRound(bool isPlayerWinner, bool isDraw) {
    m_PlayerBoard->endRound(isPlayerWinner, isDraw, m_GameInfo->getSinglePlayer());
    m_ComputerBoard->endRound(isPlayerWinner, isDraw, m_GameInfo->getSinglePlayer());
}

void RightPane::startNewGame() {
    setCurrentIndex(m_OwnBoardIndex);
    if (!m_GameInfo->getSinglePlayer()) {
        m_PlayerBoard->refreshPlanes();
        m_ComputerBoard->refreshPlanes();
    } else {
        m_PlayerBoard->reset();
        m_ComputerBoard->reset();
        m_PlayerBoard->refreshPlanes();
        m_ComputerBoard->refreshPlanes();        
    }
}

void RightPane::setMinWidth() {
    setMinimumWidth(m_PlayerBoard->getMinWidth());
}

void RightPane::showComputerMove(const GuessPoint& gp) {
	m_PlayerBoard->showMove(gp);
}

void RightPane::gameConnectedToSlot(const QString& gameName, const QString& firstPlayerName, const QString& secondPlayerName, const QString& currentRoundIdQString) {
    multiplayerRoundReset(true, gameName, firstPlayerName, secondPlayerName, currentRoundIdQString);
}


void RightPane::multiplayerRoundReset(bool exists, const QString& gameName, const QString& firstPlayerName, const QString& secondPlayerName, const QString& currentRoundIdQString) {
    
    if (!exists)
        return;
    bool okConv = true;
    long int desiredRoundId = currentRoundIdQString.toLong(&okConv);
    if (!okConv) {
        qDebug() << "Convertion error QString -> long int";
        return;
    }

    qDebug() << "Multiround reset!! " << desiredRoundId;

    m_PlayerBoard->refreshPlanes();
    m_ComputerBoard->refreshPlanes();
}

void RightPane::roundWasCancelledSlot() {
    m_PlayerBoard->setGameStage(GenericBoard::GameStages::GameNotStarted); //to deactivate guessing
    m_ComputerBoard->setGameStage(GenericBoard::GameStages::GameNotStarted);
}

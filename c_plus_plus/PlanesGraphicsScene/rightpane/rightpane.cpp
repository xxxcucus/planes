#include "rightpane.h"
#include <QGraphicsScene>
#include <QGraphicsView>
#include <QHBoxLayout>
#include <QTextEdit>
#include <QFile>
#include <QTextStream>
#include "options/optionswindow.h"
#include "account/accountwidget.h"
#include "planeround.h"
#include "game/gamewidget.h"

RightPane::RightPane(PlaneRound* pr, MultiplayerRound* mrd, QSettings* settings, GlobalData* globalData, QNetworkAccessManager* networkManager, GameInfo* gameInfo, QWidget* parent) : QTabWidget(parent), m_PlaneRound(pr), m_MultiRound(mrd), m_GlobalData(globalData), m_NetworkManager(networkManager), m_Settings(settings), m_GameInfo(gameInfo)
{
    QWidget* helpWidget = new QWidget();
    QHBoxLayout* layout = new QHBoxLayout();
    QTextEdit* textEdit = new QTextEdit();
    QFile file(":/help.html");
    file.open(QFile::ReadOnly | QFile::Text);
    QTextStream stream(&file);
    textEdit->setHtml(stream.readAll());
    layout->addWidget(textEdit);
    helpWidget->setLayout(layout);
//    defineHelpWindow(helpWidget);

    if (m_GameInfo->getSinglePlayer()) {
        m_PlayerBoard = new PlayerBoard(*m_PlaneRound->playerGrid());
        m_ComputerBoard = new ComputerBoard(*m_PlaneRound->computerGrid());
    } else {
        m_PlayerBoard = new PlayerBoard(*m_MultiRound->playerGrid());
        m_ComputerBoard = new ComputerBoard(*m_MultiRound->computerGrid());
    }
	

	OptionsWindow* optionsWindow = new OptionsWindow(m_PlaneRound, m_Settings, m_GameInfo);
    AccountWidget* accountWidget = new AccountWidget(m_Settings, m_GlobalData, m_NetworkManager, m_GameInfo);
    GameWidget* gameWidget = new GameWidget(m_GlobalData, m_GameInfo, m_NetworkManager, m_Settings, m_MultiRound);

    m_OwnBoardIndex = addTab(m_PlayerBoard->getView(), "Player Board");
    m_OpponentBoardIndex = addTab(m_ComputerBoard->getView(), "Computer Board");
	addTab(optionsWindow, "Options");
    m_AccountWidgetIndex = addTab(accountWidget, "Account");
    m_GameWidgetIndex = addTab(gameWidget, "Game");
    if (m_GameInfo->getSinglePlayer())
        setTabEnabled(m_AccountWidgetIndex, false);
    addTab(helpWidget, "Help");

    connect(m_PlayerBoard, SIGNAL(planePositionNotValid(bool)), this, SIGNAL(planePositionNotValid(bool)));
    //connect(this, SIGNAL(showComputerMove(const GuessPoint&)), m_PlayerBoard, SLOT(showMove(const GuessPoint&)));
    connect(m_ComputerBoard, SIGNAL(guessMade(const GuessPoint&)), this, SIGNAL(guessMade(const GuessPoint&)));
    connect(gameWidget, &GameWidget::gameConnectedTo, this, &RightPane::multiplayerRoundReset);
    connect(m_MultiRound, SIGNAL(roundWasCancelled()), this, SLOT(roundWasCancelledSlot()));
}

RightPane::~RightPane()
{
    delete m_PlayerBoard;
    delete m_ComputerBoard;
	delete m_Settings;
}

void RightPane::defineHelpWindow(QWidget* w)
{
    QHBoxLayout* layout = new QHBoxLayout();
    QTextEdit* textEdit = new QTextEdit();
    QFile file("qrc:/../doc/index.html");
    file.open(QIODevice::ReadOnly | QIODevice::Text);
    QByteArray dump = file.readAll();
    //qDebug() << "contents: " << dump;
    //qDebug() << "error status:" << file.error();
    textEdit->loadResource(QTextDocument::HtmlResource, QUrl("qrc://help.html"));
    textEdit->setSizePolicy(QSizePolicy::Expanding, QSizePolicy::Expanding);
    layout->addWidget(textEdit);
    w->setLayout(layout);
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
    m_PlayerBoard->endRound(isPlayerWinner, isDraw);
    m_ComputerBoard->endRound(isPlayerWinner, isDraw);
}

void RightPane::startNewGame() {
    setCurrentIndex(m_OwnBoardIndex);
    if (!m_GameInfo->getSinglePlayer()) {
        m_PlayerBoard->refreshPlanes();
        m_ComputerBoard->refreshPlanes();
    }
}

void RightPane::setMinWidth()
{
    setMinimumWidth(m_PlayerBoard->getMinWidth());
}

void RightPane::showComputerMove(const GuessPoint& gp)
{
	m_PlayerBoard->showMove(gp);
}

void RightPane::multiplayerRoundReset(const QString& gameName, const QString& firstPlayerName, const QString& secondPlayerName, const QString& currentRoundIdQString) {
    
    bool okConv = true;
    long int desiredRoundId = currentRoundIdQString.toLong(&okConv);
    if (!okConv) {
        qDebug() << "Convertion error QString -> long int";
        return;
    }

    qDebug() << "Multiround reset!! " << desiredRoundId;

    
    long int currentRoundId =  m_MultiRound->getRoundId();
    if (currentRoundId == desiredRoundId)
        return;
 
    //TODO cancel current round
    m_MultiRound->initRound();
    m_MultiRound->setRoundId(desiredRoundId);
    
    m_PlayerBoard->refreshPlanes();
    m_ComputerBoard->refreshPlanes();

    qDebug() << "Multiround reset!! " << desiredRoundId;

}

void RightPane::roundWasCancelledSlot()
{
    m_PlayerBoard->setGameStage(GenericBoard::GameStages::GameNotStarted); //to deactivate guessing
    m_ComputerBoard->setGameStage(GenericBoard::GameStages::GameNotStarted);
}

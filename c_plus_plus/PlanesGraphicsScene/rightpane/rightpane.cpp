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

RightPane::RightPane(PlaneGrid* pGrid, PlaneGrid* cGrid, PlaneRound* pr, GlobalData* globalData, QNetworkAccessManager* networkManager, GameInfo* gameInfo, QWidget* parent) : QTabWidget(parent), m_PlaneRound(pr), m_GlobalData(globalData), m_NetworkManager(networkManager), m_GameInfo(gameInfo)
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

    m_PlayerBoard = new PlayerBoard(*pGrid);
    m_ComputerBoard = new ComputerBoard(*cGrid);
	m_Settings = new QSettings("Cristian Cucu", "Planes");

	OptionsWindow* optionsWindow = new OptionsWindow(m_PlaneRound, m_Settings, m_GameInfo);
    AccountWidget* accountWidget = new AccountWidget(m_Settings, m_GlobalData, m_NetworkManager, m_GameInfo);
    GameWidget* gameWidget = new GameWidget(m_GlobalData, m_GameInfo, m_NetworkManager, m_Settings);

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
}

void RightPane::setMinWidth()
{
    setMinimumWidth(m_PlayerBoard->getMinWidth());
}

void RightPane::showComputerMove(const GuessPoint& gp)
{
	m_PlayerBoard->showMove(gp);
}

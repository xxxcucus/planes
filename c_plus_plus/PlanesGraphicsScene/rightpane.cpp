#include "rightpane.h"
#include <QGraphicsScene>
#include <QGraphicsView>
#include <QHBoxLayout>
#include <QTextEdit>
#include <QFile>
#include <QTextStream>
#include "optionswindow.h"
#include "planeround.h"

RightPane::RightPane(PlaneGrid* pGrid, PlaneGrid* cGrid, PlaneRound* pr, QWidget* parent) : QTabWidget(parent), m_PlaneRound(pr)
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

	OptionsWindow* optionsWindow = new OptionsWindow(m_PlaneRound, m_Settings);

    addTab(m_PlayerBoard->getView(), "Player Board");
    addTab(m_ComputerBoard->getView(), "Computer Board");
	addTab(optionsWindow, "Options");
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
    setCurrentIndex(1);
    m_PlayerBoard->setGameStage(GenericBoard::GameStages::Game);
    m_PlayerBoard->setSelectedPlaneIndex(-1);
    m_ComputerBoard->setGameStage(GenericBoard::GameStages::Game);
}

void RightPane::endRound(bool isPlayerWinner, bool isDraw) {
    m_PlayerBoard->endRound(isPlayerWinner, isDraw);
    m_ComputerBoard->endRound(isPlayerWinner, isDraw);
}

void RightPane::startNewGame() {
    setCurrentIndex(0);
}

void RightPane::setMinWidth()
{
    setMinimumWidth(m_PlayerBoard->getMinWidth());
}

void RightPane::showComputerMove(const GuessPoint& gp)
{
	m_PlayerBoard->showMove(gp);
}
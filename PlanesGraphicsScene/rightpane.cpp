#include "rightpane.h"
#include <QGraphicsScene>
#include <QGraphicsView>

RightPane::RightPane(PlaneGrid& pGrid, PlaneGrid& cGrid, QWidget* parent) : QTabWidget(parent)
{
    QWidget* helpWidget = new QWidget();
    defineHelpWindow(helpWidget);

    m_PlayerBoard = new PlayerBoard(pGrid);
    m_ComputerBoard = new ComputerBoard(cGrid);

    QWidget* startGameWidget = new QWidget();

    addTab(m_PlayerBoard->getView(), "Player Board");
    addTab(m_ComputerBoard->getView(), "Computer Board");
    addTab(helpWidget, "Help");
    addTab(startGameWidget, "Start Game");

    connect(m_PlayerBoard, SIGNAL(planePositionNotValid(bool)), this, SIGNAL(planePositionNotValid(bool)));
    connect(this, SIGNAL(showComputerMove(const GuessPoint&)), m_PlayerBoard, SLOT(showMove(const GuessPoint&)));
}

RightPane::~RightPane()
{
    delete m_PlayerBoard;
    delete m_ComputerBoard;
}

void RightPane::defineHelpWindow(QWidget* w)
{
    Q_UNUSED(w)
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

void RightPane::doneClicked(bool val)
{
    Q_UNUSED(val)
    setCurrentIndex(1);
    m_PlayerBoard->setGameStage(GenericBoard::GameStages::Game);
    m_ComputerBoard->setGameStage(GenericBoard::GameStages::Game);
}

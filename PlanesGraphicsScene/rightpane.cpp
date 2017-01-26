#include "rightpane.h"
#include <QGraphicsScene>
#include <QGraphicsView>

RightPane::RightPane(PlaneGrid& pGrid, PlaneGrid& cGrid, QWidget* parent) : QTabWidget(parent)
{
    QWidget* helpWidget = new QWidget();
    defineHelpWindow(helpWidget);

    m_GameBoard = new GameBoard(pGrid, cGrid);

    addTab(m_GameBoard->getView(), "Boards");
    addTab(helpWidget, "Help");
}

void RightPane::defineHelpWindow(QWidget* w)
{
    Q_UNUSED(w)
}


void RightPane::selectPlaneClicked(bool val)
{
    m_GameBoard->selectPlaneClicked(val);
}

void RightPane::rotatePlaneClicked(bool val)
{
    m_GameBoard->rotatePlaneClicked(val);
}

void RightPane::upPlaneClicked(bool val)
{
    m_GameBoard->upPlaneClicked(val);
}

void RightPane::downPlaneClicked(bool val)
{
    m_GameBoard->downPlaneClicked(val);
}

void RightPane::leftPlaneClicked(bool val)
{
    m_GameBoard->leftPlaneClicked(val);
}

void RightPane::rightPlaneClicked(bool val)
{
    m_GameBoard->rightPlaneClicked(val);
}

void RightPane::doneClicked(bool val)
{
    Q_UNUSED(val)
}

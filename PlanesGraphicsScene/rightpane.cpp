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


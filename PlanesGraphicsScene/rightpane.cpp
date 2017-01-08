#include "rightpane.h"
#include <QGraphicsScene>
#include <QGraphicsView>

RightPane::RightPane(QWidget* parent) : QTabWidget(parent)
{
    QWidget* helpWidget = new QWidget();
    defineHelpWindow(helpWidget);

    m_GameBoard = new GameBoard();

    addTab(m_GameBoard->getView(), "Boards");
    addTab(helpWidget, "Help");
}

void RightPane::defineHelpWindow(QWidget* w)
{
    Q_UNUSED(w)
}


#include "rightpane.h"
#include <QGraphicsScene>
#include <QGraphicsView>

RightPane::RightPane(QWidget* parent) : QTabWidget(parent)
{
    m_Scene = new QGraphicsScene();
    m_Scene->addText("Hello world!");
    m_View = new QGraphicsView(m_Scene);

    QWidget* helpWidget = new QWidget();
    defineHelpWindow(helpWidget);

    addTab(m_View, "Boards");
    addTab(helpWidget, "Help");
}

void RightPane::defineHelpWindow(QWidget* w)
{
    Q_UNUSED(w)
}


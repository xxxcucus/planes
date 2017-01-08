#include "gameboard.h"

GameBoard::GameBoard()
{
    m_Scene = new QGraphicsScene();
    m_Scene->addText("Hello world!");
    m_View = new QGraphicsView(m_Scene);
}

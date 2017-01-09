#include "gameboard.h"
#include "boardrectangle.h"

GameBoard::GameBoard(PlaneGrid& pGrid, PlaneGrid& cGrid): m_PlayerGrid(pGrid), m_ComputerGrid(cGrid)
{
    m_Scene = new QGraphicsScene();
    m_Scene->addText("Hello world!");
    m_View = new QGraphicsView(m_Scene);
}

void GameBoard::showEditorBoard()
{
    for (int i = 0; i < m_PlayerGrid.getRowNo(); i++)
        for (int j = 0; j < m_PlayerGrid.getColNo(); j++) {
            BoardRectangle* br = new BoardRectangle(i, j, m_SquareWidth);
            m_Scene->addItem(br);
            br->setPos(i * m_SquareWidth, j * m_SquareWidth);
        }
}

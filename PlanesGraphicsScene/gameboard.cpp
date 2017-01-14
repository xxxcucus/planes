#include "gameboard.h"
#include "gridsquare.h"
#include "playareagridsquare.h"

GameBoard::GameBoard(PlaneGrid& pGrid, PlaneGrid& cGrid): m_PlayerGrid(pGrid), m_ComputerGrid(cGrid)
{
    m_Scene = new QGraphicsScene();
    m_Scene->addText("Hello world!");
    m_View = new QGraphicsView(m_Scene);
}

void GameBoard::showEditorBoard()
{
    int rows = m_PlayerGrid.getRowNo() + 2 * m_PaddingEditingBoard;
    int cols = m_PlayerGrid.getColNo() + 2 * m_PaddingEditingBoard;


    for (int i = 0; i < rows; i++)
        for (int j = 0; j < cols; j++) {
            if (i < m_PaddingEditingBoard || abs(i - rows) <= m_PaddingEditingBoard
                    || j < m_PaddingEditingBoard || abs (j - cols) <= m_PaddingEditingBoard) {
                GridSquare* br = new GridSquare(i, j, m_SquareWidth);
                m_Scene->addItem(br);
                br->setPos(i * m_SquareWidth, j * m_SquareWidth);
            } else {
                PlayAreaGridSquare* pabr = new PlayAreaGridSquare(i, j, m_SquareWidth);
                m_Scene->addItem(pabr);
                pabr->setPos(i * m_SquareWidth, j * m_SquareWidth);
            }
        }
}

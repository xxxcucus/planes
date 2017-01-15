#include "gameboard.h"
#include "gridsquare.h"
#include "playareagridsquare.h"
#include "plane.h"

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


///shows the planes on the grid
void GameBoard::displayComputerPlanes() {

}

///@todo: deal with overlapping planes
void GameBoard::displayPlayerPlanes() {
    for (int i = 0; i < m_PlayerGrid.getPlaneNo(); i++) {
        Plane pl;
        if (!m_PlayerGrid.getPlane(i, pl))
            continue;
        PlanePointIterator ppi(pl);
        QPoint head = pl.head();
        while (ppi.hasNext()) {
            QPoint pt = ppi.next();
        }
    }
}


///shows the computer guess on the grid
void GameBoard::displayComputerGuesses() {

}

///shows the player guess on the grid
void GameBoard::displayPlayerGuesses() {

}


///initializes the computer grid
void GameBoard::initializeComputerGrid() {
//    m_ComputerGrid.initGridByAutomaticGeneration();
}

///initializes the player grid
void GameBoard::initializePlayerGrid() {
//    m_PlayerGrid.initGridByAutomaticGeneration();
}

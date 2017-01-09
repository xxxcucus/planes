#ifndef GAMEBOARD_H
#define GAMEBOARD_H

#include <QGraphicsScene>
#include <QGraphicsView>
#include "planegrid.h"

class GameBoard
{
public:
    GameBoard(PlaneGrid& pGrid, PlaneGrid& cGrid);

    inline QWidget* getView() { return m_View; }

    ///deletes all the objects in the graphicscene
    ///and creates the board for placing the planes
    inline void reset() {
        clearBoard();
        showEditorBoard();
    }

private:
    inline void clearBoard() { m_Scene->clear();}
    void showEditorBoard();

private:
    QGraphicsScene* m_Scene;
    QGraphicsView* m_View;

    PlaneGrid& m_PlayerGrid;
    PlaneGrid& m_ComputerGrid;

    const int m_SquareWidth = 50;

};

#endif // GAMEBOARD_H

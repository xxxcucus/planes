#ifndef GAMEBOARD_H
#define GAMEBOARD_H

#include <map>

#include <QGraphicsScene>
#include <QGraphicsView>
#include "planegrid.h"
#include "gridsquare.h"

///the board containing the playing grids and additional cells for editing
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
    void initializeBoardEditingItems();
    ///shows the planes on the grid
    void displayPlayerPlanes();
    ///shows the planes on the grid
    void displayComputerPlanes();
    ///shows the computer guess on the grid
    void displayComputerGuesses();
    ///shows the player guesses on the grid
    void displayPlayerGuesses();

    void showPlane(const Plane& pl);
    void showSelectedPlane(const Plane& pl);

private:
    QGraphicsScene* m_Scene;
    QGraphicsView* m_View;

    PlaneGrid& m_PlayerGrid;
    PlaneGrid& m_ComputerGrid;

    ///the width of a square inside the player grid
    const int m_SquareWidth = 30;
    ///padding is added so that the plane is always completely
    /// shown also when not completely inside the game area
    const int m_PaddingEditingBoard = 3;

    ///initialy a grid with m_Rows + 2 * m_Padding width is built
    ///in this grid the position of the players' planes are decided

    std::map<std::pair<int, int>, GridSquare*> m_SceneItems;

    ///which plane can be moved on the editor board
    int m_SelectedPlane = 0;
};

#endif // GAMEBOARD_H

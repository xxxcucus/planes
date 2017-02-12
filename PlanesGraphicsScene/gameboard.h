#ifndef GAMEBOARD_H
#define GAMEBOARD_H

#include <map>

#include <QGraphicsScene>
#include <QGraphicsView>
#include "planegrid.h"
#include "gridsquare.h"

///the board containing the playing grids and additional cells for editing
class GameBoard : public QObject
{
    Q_OBJECT
public:
    enum class GameStages { BoardEditing, Game };

    GameBoard(PlaneGrid& pGrid, PlaneGrid& cGrid);

    inline QWidget* getView() { return m_View; }

    ///deletes all the objects in the graphicscene
    ///and creates the board for placing the planes
    void reset();

    ///functions to edit the player's game board
    void selectPlaneClicked(bool );
    void rotatePlaneClicked(bool );
    void upPlaneClicked(bool );
    void downPlaneClicked(bool );
    void leftPlaneClicked(bool );
    void rightPlaneClicked(bool );
    void doneClicked(bool );


signals:
    void planesOverlap(bool);

private:
    inline void clearBoard() { m_Scene->clear();}
    /*void showEditorBoard();*/
    /**
     * @brief Generates the graphics scene items for the board
     */
    void generateBoardItems();
    void generateBoardItemsEditingStage();
    void generateBoardItemsGameStage();
    ///shows the planes on the grid
    void displayPlayerPlanes();
    ///hide player planes
    void hidePlayerPlanes();
    ///shows the planes on the grid
    void displayComputerPlanes();
    ///shows the computer guess on the grid
    void displayComputerGuesses();
    ///shows the player guesses on the grid
    void displayPlayerGuesses();

    void showPlane(const Plane& pl);
    void showSelectedPlane(const Plane& pl);
    void updateEditorBoard();

private:
    QGraphicsScene* m_Scene;
    QGraphicsView* m_View;

    PlaneGrid& m_PlayerGrid;
    PlaneGrid& m_ComputerGrid;

    ///widths of a square inside the grid
    const int m_BigSquareWidth = 30;
    const int m_SmallSquareWidth = 20;
    ///padding is added so that the plane is always completely
    /// shown also when not completely inside the game area
    const int m_PaddingEditingBoard = 3;

    ///initialy a grid with m_Rows + 2 * m_Padding width is built
    ///in this grid the position of the players' planes are decided

    std::map<std::pair<int, int>, GridSquare*> m_SceneItems;

    ///which plane can be moved on the editor board
    int m_SelectedPlane = 0;

    ///the game has two parts: player edits his board, game is played against the computer
    GameStages m_CurStage = GameStages::BoardEditing;

    ///two grids are shown at the same time in the game board
    /// one in the lowest left corner and one in the highest right corner
    /// depending on the current setting one uses big squares and the other small squares
    bool m_LowerGridUsesBigSquares = false;
};

#endif // GAMEBOARD_H

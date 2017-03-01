#ifndef GENERICBOARD_H
#define GENERICBOARD_H

#include <QGraphicsScene>
#include <QGraphicsView>

#include "planegrid.h"
#include "gridsquare.h"

///the board where the player lays his/her planes and where the computer tries to guess their position
class GenericBoard : public QObject
{
    Q_OBJECT
public:
    enum class GameStages { BoardEditing, Game };
    GenericBoard(PlaneGrid& grid, int squareWidth = 30);

    inline QWidget* getView() { return m_View; }
    ///deletes all the objects in the graphicscene
    ///and creates the board for placing the planes
    void reset();
    inline void setGameStage(GameStages stage) { m_CurStage = stage; }

signals:
    void planePositionNotValid(bool);

public slots:
    /**
     * @brief shows a guess made by computer or player
     */
    void showMove(const GuessPoint&);

protected:
    void clearBoard();
    /**
     * @brief Generates the graphics scene items for the board
     */
    void generateBoardItems();
    virtual void generateBoardItemsEditingStage();
    virtual void generateBoardItemsGameStage();
    ///shows the planes on the grid
    void displayPlanes();
    ///hide the planes
    ///@todo: hide everything
    void hidePlanes();
    ///shows the guesses on the grid
    void displayGuesses();

    void showPlane(const Plane& pl);
    void showSelectedPlane(const Plane& pl);
    void showGuessPoint(const GuessPoint& gp);
    /**
     * @brief Hides planes, checks if their position is valid and then displays them again.
     */
    void updateEditorBoard();

protected:
    QGraphicsScene* m_Scene;
    QGraphicsView* m_View;

    PlaneGrid& m_Grid;
    int m_SquareWidth = 30;

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

    ///list of guesses made up to now
    QList<GuessPoint> m_GuessList;
};



#endif // GENERICBOARD_H

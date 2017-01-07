#ifndef RENDERAREA_H
#define RENDERAREA_H

#include <QWidget>
#include "plane.h"
#include "planegame.h"
#include "baserenderarea.h"


//this is the grid that is used to add planes and to play the game
//it supports multiple modes and multiple operations
class GameRenderArea: public BaseRenderArea
{

    Q_OBJECT

public:
    enum Operation {No_operation, Move_plane, Delete_plane, Rotate_plane, Add_plane};
    enum Mode {Editor, Game};

private:

    //the current drawing operation
    Operation m_currentOperation;
    //the current operation mode: Editor or Game
    Mode m_currentMode;

    //stores the position of the temporary plane
    Plane m_tempPlane;
    //backup plane which will be restored when user cancels operation
    Plane m_backupPlane;

    //stores the current position of the mouse cursor
    int m_curMouseRow, m_curMouseCol;
    //position of last drawn plane is inside the grid
    bool m_planePosValid;
    //whether a plane has been selected
    bool m_planeSelected;

    //the plane grid associated with this render area
    PlaneGrid* m_grid;
    //list of guesses for this render area
    QList <GuessPoint> m_guessPointList;

    //in game mode says whether the round has endet
    bool m_roundEndet;


public:
    //constructor sets the dimension of the render area
    //and attaches a plane grid object to it
    GameRenderArea(PlaneGrid* grid, QWidget* parent =0);

    //sets the current operation in the render area
    bool setOperation(Operation o);

    //restores the backup plane
    void restoreBackupPlane();

    //sets the current mode: Editor or Game
    void setMode(Mode m);
    //whether more planes need to be added or not
    bool addMorePlanes() const;
    //returns the underlying grid
    //PlaneGrid* getGrid() {return m_grid; }
    //tests whether the render area corresponds to computer or to player
    bool isComputer() const {return m_grid->isComputer(); }


protected:
    //the widget's paint event
    void paintEvent(QPaintEvent *event);
    //the mouse move event
    void mouseMoveEvent(QMouseEvent *event);
    //the mouse pressed event
    void mousePressEvent(QMouseEvent *event);

private:


    //fills a rect in the grid
    void fillGridRect(int row, int col, QString color, QPainter *painter);

    //functions for player editor mode

    //draws m_tempPlane
    void drawTempPlane(QPainter *painter);
    //draws a plane
    void drawPlane(Plane pl, QString color, QPainter *painter);
    //resets the current operation
    void resetOperation();
    //saves the m_tempPlane in the m_grid object
    void saveTempPlane();
    //saves a plane in the m_grid object
    void savePlane(Plane pl);
    //draws the planes in the m_grid object
    void drawConfirmedPlanes(QPainter *painter);
    //draws the guesses made until now
    void drawGuesses(QPainter *painter) const;
    //searches a plane in the m_grid object
    int searchPlane(Plane pl) const;
    //searches a plane in the m_grid object by the coordinates
    //of the plane's head
    int searchPlane(int row, int col) const;
    //selects a plane from the m_grid object
    bool selectPlane();
    //rotates m_tempPlane
    void rotateTempPlane();
    //removes a plane from the m_grid object
    bool removePlane(int idx);
    //mouse press event handler for player in editor mode
    void mousePressEventPlayerEditor(QMouseEvent *event);
    //mouse press event handler for computer in game
    void mousePressEventComputerGame(QMouseEvent *event);
    //temp plane intersects other planse
    bool tempPlaneIntersectsOtherPlanes() const;


signals:
    //notifies that the current operation has endet
    void operationEndet();
    //sends a QString message
    void displayMsg(QString);
    //sends a QString message
    void displayStatusMsg(QString);
    //notifies that the required number of planes has been drawn
    void enoughPlanes();
    //notifies that the required number of planes has not been drawn
    void notEnoughPlanes();
    //notifies that all planes are there and do not superimpose
    void activateDone();
    //signals that the player has chosen a point on computer's grid
    void guessMade(GuessPoint gp);

public slots:
    //toggles the mode of the area from Editor to Game
    void changeMode();
    //shows a move
    void showMove(GuessPoint gp);
    //activate Game mode in which the player clicks to find planes
    void activateGameMode();
    //round ends activate Editor mode for computer grid
    void activateEditorMode();
    //round ends and display the real positions of the planes
    void roundEndet();
    //clears the list of planes and guesses
    void reset();
};

#endif // RENDERAREA_H

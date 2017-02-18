#ifndef PLAYERBOARD_H
#define PLAYERBOARD_H

#include "genericboard.h"
#include "planegrid.h"


///the board where the player lays his/her planes and where the computer tries to guess their position
class PlayerBoard : public GenericBoard
{
    Q_OBJECT
public:
    PlayerBoard(PlaneGrid& grid, int squareWidth = 30) : GenericBoard(grid, squareWidth) {}

    ///functions to edit the player's game board
    void selectPlaneClicked(bool );
    void rotatePlaneClicked(bool );
    void upPlaneClicked(bool );
    void downPlaneClicked(bool );
    void leftPlaneClicked(bool );
    void rightPlaneClicked(bool );
    void doneClicked(bool );
};

#endif // PLAYERBOARD_H

#ifndef COMPUTERBOARD_H
#define COMPUTERBOARD_H

#include "genericboard.h"

class ComputerBoard : public GenericBoard
{
    Q_OBJECT
public:
    ComputerBoard(PlaneGrid& grid, int squareWidth = 30);

public slots:
    /*
     * What happens when the player clicks in the computer board
     * row, col - the coordinates of the grid square where it was clicked
     */
    void gridSquareClicked(int row, int col);

signals:
    void guessMade(const GuessPoint& gp);

};

#endif // COMPUTERBOARD_H

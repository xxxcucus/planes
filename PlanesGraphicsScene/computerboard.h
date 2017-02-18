#ifndef COMPUTERBOARD_H
#define COMPUTERBOARD_H

#include "genericboard.h"

class ComputerBoard : public GenericBoard
{
public:
    ComputerBoard(PlaneGrid& grid, int squareWidth = 30) : GenericBoard(grid, squareWidth) {}
};

#endif // COMPUTERBOARD_H

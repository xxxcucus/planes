#ifndef _REVERT_COMPUTER_LOGIC_
#define _REVERT_COMPUTER_LOGIC_

#include "computerlogic.h"
#include <vector>

//this is a class that reverts the moves in the computer strategy
class RevertComputerLogic: public ComputerLogic
{
    //the list of guess points
    std::vector<GuessPoint> m_playList;
    //the current position in the list of guess points
    int m_pos;

public:
    //constructor
    RevertComputerLogic(int row, int col, int planeno);

    //assignment of a computerlogic operator
    void operator=(const ComputerLogic& cl);
    //reverts the computer strategy by n steps
    void revert(int n);
    //plays the computer strategy forward
    void next();

    bool hasPrev() { return m_pos >= 0; }
    bool hasNext() { return m_pos < static_cast<int>(m_playList.size()) - 1; }
};

#endif
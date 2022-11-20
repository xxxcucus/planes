#ifndef __HEAD_DATA_
#define __HEAD_DATA_

#include "planeorientationdata.h"

struct HeadData
{
    //size of the grid
    int m_row, m_col;
    //position of the head
    int m_headRow, m_headCol;
    //the correct plane orientation if decided
    int m_correctOrient;

    //statistics about the 4 orientations with this head
    PlaneOrientationData m_options[4];

    HeadData(int row, int col, int headRow, int headCol);
    //update the current data with a guess
    //return true if a plane is confirmed
    bool update(const GuessPoint& gp);

    friend class HeadDataTest;
};

#endif
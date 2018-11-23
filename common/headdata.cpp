#include "headdata.h"

//constructs the head data structure
//m_row and m_col give the size of the grid
//m_headRow and m_headCol
//are the positions of the planes head
//m_correctOrient is the orientation of the plane
HeadData::HeadData(int row, int col, int headRow, int headCol):
    m_row(row),
    m_col(col),
    m_headRow(headRow),
    m_headCol(headCol),
    m_correctOrient(-1)
{

    for(int i = 0;i < 4; i++)
    {
        Plane pl(m_headRow, m_headCol, (Plane::Orientation)i);
        //create the four planes for each head position
        m_options[i] = PlaneOrientationData(pl, false);

        if(!pl.isPositionValid(m_row, m_col))
            m_options[i].m_discarded = true;
    }
    //check which of the four possible orientation is a valid position
    //for the invalid positions mark that orientation as discarded
}

//update a head data structure with a guess
bool HeadData::update(const GuessPoint& gp)
{
    //if the head data is already conclusive ignore
    if(m_correctOrient != -1)
        return true;

    //update the four plane positions with this new data
    for(int i = 0;i < 4; i++)
        m_options[i].update(gp);

    //verify if we checked all points of a plane
    for(int i = 0;i < 4; i++)
    {
        if(!m_options[i].m_discarded && m_options[i].areAllPointsChecked())
        {
            m_correctOrient = i;
            return true;
        }
    }

    //verify if 3 of the 4 possible orientations are discarded
    int count = 0;
    int good_orientation = -1;
    for(int i = 0;i < 4; i++)
    {
        if(m_options[i].m_discarded)
            count++;
        else
            good_orientation = i;
    }
    //if there are exactly 3 wrong orientations
    if(count == 3)
    {
        m_correctOrient = good_orientation;
        return true;
    }
    return false;
}

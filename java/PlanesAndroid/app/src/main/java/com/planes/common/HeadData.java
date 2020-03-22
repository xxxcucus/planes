package com.planes.common;

public class HeadData {

    //size of the grid
    public int m_row;
    public int m_col;
    //position of the head
    public int m_headRow;
    public int m_headCol;
    //the correct plane orientation if decided
    public int m_correctOrient;

    //statistics about the 4 positions with this head
    public PlaneOrientationData m_options[];

    public HeadData(int row, int col, int headRow, int headCol) {
        m_row = row;
        m_col = col;
        m_headRow = headRow;
        m_headCol = headCol;
        m_correctOrient = -1;
        m_options = new PlaneOrientationData[4];

        for(int i = 0; i < 4; i++)
        {
            Plane pl = new Plane(m_headRow, m_headCol, Orientation.values()[i]);
            //create the four planes for each head position
            m_options[i] = new PlaneOrientationData(pl, false);

            if(!pl.isPositionValid(m_row, m_col))
                m_options[i].m_discarded = true;
        }
    }
    //update the current data with a guess
    //return true if a plane is confirmed
    public boolean update(final GuessPoint gp) {
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

}

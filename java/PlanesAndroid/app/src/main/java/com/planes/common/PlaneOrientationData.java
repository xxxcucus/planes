package com.planes.common;

import java.util.Vector;

public class PlaneOrientationData {

    //default constructor
    public PlaneOrientationData() {
        m_plane = new Plane(0, 0, Orientation.values()[0]);
        m_discarded = true;
        m_pointsNotTested.clear();
    }
    //another constructor
    public PlaneOrientationData(final Plane pl, boolean isDiscarded) {
        m_plane = (Plane)pl.clone();
        m_discarded = isDiscarded;

        PlanePointIterator ppi = new PlanePointIterator(m_plane);
        ppi.next();

        while (ppi.hasNext()) {
            m_pointsNotTested.add((Coordinate2D)ppi.next().clone());
        }
    }
    //copy constructor
    public PlaneOrientationData(final PlaneOrientationData pod) {
        m_plane = (Plane)pod.m_plane.clone();
        m_discarded = pod.m_discarded;
        m_pointsNotTested = (Vector<Coordinate2D>)pod.m_pointsNotTested.clone();
    }
    //equals operator
    //TODO
    //void operator=(const PlaneOrientationData& pod);

    //update the info about this plane with another guess point
    //a guess point is a pair (position, guess result)
    public void update(final GuessPoint gp) {
        //if plane is discarded return
        if (m_discarded)
            return;

        //find the guess point in the list of points not tested
        int idx = m_pointsNotTested.indexOf(new Coordinate2D(gp.m_row, gp.m_col));

        //if point not found return
        if (idx < 0)
            return;

        //if point found
        //if dead and idx = 0 remove the head from the list of untested points
        if (gp.m_type == Type.Dead && idx == 0)
        {
            m_pointsNotTested.remove(idx);
            return;
        }

        //if miss or dead discard plane
        if (gp.m_type == Type.Miss || gp.m_type == Type.Dead)
            m_discarded = true;

        //if hit take point out of the list of points not tested
        if (gp.m_type == Type.Hit)
            m_pointsNotTested.remove(idx);

    }
    //verifies if all the points in the current orientation were already checked
    public boolean areAllPointsChecked() {
        return m_pointsNotTested.isEmpty();
    }



    //the position of the plane
    public Plane m_plane;

    //whether this orientation was discarded
    public boolean m_discarded;
    //points on this plane that were not tested
    //if m_discarded is false it means that all the
    //tested points were hits
    public Vector<Coordinate2D> m_pointsNotTested;
}

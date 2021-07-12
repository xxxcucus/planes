package com.planes.common;

import java.util.Vector;

public class PlaneOrientationData {

    //default constructor
    public PlaneOrientationData() {
        m_plane = new Plane(0, 0, Orientation.values()[0]);
        m_discarded = true;
        m_pointsNotTested = new Vector<Coordinate2D>();
    }
    //another constructor
    public PlaneOrientationData(final Plane pl, boolean isDiscarded) {
        m_plane = (Plane)pl.clone();
        m_discarded = isDiscarded;
        m_pointsNotTested = new Vector<Coordinate2D>();

        PlanePointIterator ppi = new PlanePointIterator(m_plane);
        ppi.next();

        while (ppi.hasNext()) {
            m_pointsNotTested.add((Coordinate2D)ppi.next().clone());
        }
    }

    //update the info about this plane with another guess point
    //a guess point is a pair (position, guess result)
    public void update(final GuessPoint gp) {
        //if plane is discarded return
        if (m_discarded)
            return;

        //find the guess point in the list of points not tested
        int idx = m_pointsNotTested.indexOf(new Coordinate2D(gp.row(), gp.col()));

        //if point not found return
        if (idx < 0)
            return;

        //if point found
        //if dead and point is the planes head remove the head from the list of untested points
        if (gp.type() == Type.Dead && m_plane.isHead(new Coordinate2D(gp.row(), gp.col())))
        {
            m_pointsNotTested.remove(idx);
            return;
        }

        //if miss or dead discard plane
        if (gp.type() == Type.Miss || gp.type() == Type.Dead)
            m_discarded = true;

        //if hit take point out of the list of points not tested
        if (gp.type() == Type.Hit)
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

package com.planes.common;

//The computer is trying to guess where the player's planes are
//For every position on the table and each of the 4 plane orientations
//it keeps a PlaneOrientationData structure that
//contains all the knowledge that the computer has
//about the corresponding plane

//For each computer guess there can be 3 results
//Miss - the guess is not on a plane
//Hit - the guess is on a plane but it is not on the plane's head
//Dead - the guess is the plane's head
//describes the data that is available about a given plane position
//This structure keeps the information about the position of the head of the planes

import android.support.v4.util.Pair;

import java.util.Iterator;
import java.util.Vector;

public class ComputerLogic {

    public ComputerLogic(int row, int col, int planeno) {

        m_row = row;
        m_col = col;
        maxChoiceNo = row * col * 4;
        m_planeNo = planeno;

        //creates the tables of choices
        m_choices = new Vector<Integer>(maxChoiceNo);
        for (int i = 0; i < maxChoiceNo; i++) {
            m_choices.add(new Integer(-1));
        }

        m_zero_choices = new Vector<Integer>(maxChoiceNo / 4);
        for (int i = 0; i < maxChoiceNo / 4; i++) {
            m_zero_choices.add(new Integer(-1));
        }

        m_guessedPlaneList = new Vector<Plane>();
        m_headDataList = new Vector<HeadData>();
        m_guessesList = new Vector<GuessPoint>();
        m_extendedGuessesList = new Vector<GuessPoint>();

        //initializes the table of choices and the head data
        reset();
        //initializes the iterator that generates all the planes that pass
        //through (0,0)
        m_pipi.reset();

    }
    //restores the list of choices
    public void reset() {
        //initializes -1 for impossible choice (invalid plane position)
        //with 0 for possible choice
        for(int i = 0; i < maxChoiceNo; i++)
        {
            Plane pl = mapIndexToPlane(i);
            if (pl.isPositionValid(m_row, m_col))
                m_choices.set(i, new Integer(0));
            else
                m_choices.set(i, new Integer(-1));
        }

        //clears various lists in the computerlogic object
        m_guessedPlaneList.clear();
        //m_guessedHeadList.clear();
        m_guessesList.clear();
        m_extendedGuessesList.clear();
        m_headDataList.clear();
    }
    //returns the plane choice with the highest score and true
    //or false if there are no more valid choices
    //TODO: to test this method
    public Pair<Boolean, Coordinate2D> makeChoice() {
        //based on the 3 strategies of choice choses 3 possible moves
        Coordinate2D qp1 = null;
        Coordinate2D qp2 = null;
        Coordinate2D qp3 = null;

        Pair<Boolean, Coordinate2D> test1 = makeChoiceFindHeadMode();
        Pair<Boolean, Coordinate2D> test2 = makeChoiceFindPositionMode();
        Pair<Boolean, Coordinate2D> test3 = makeChoiceRandomMode();

        //if there are no more choices to be tested return false
        if(!test1.first)
            return test1;

        //generates a random number smaller than 10
        int idx = Plane.generateRandomNumber(10);

        //various random strategies for making a choice
        if(test2.first && test3.first) {
            if(idx < 6) {
                return Pair.create(true, (Coordinate2D)test1.second.clone());
            }

            if(idx < 9) {
                return Pair.create(true, (Coordinate2D)test2.second.clone());
            }

            return Pair.create(true, (Coordinate2D)test3.second.clone());
        }

        if(!test2.first && test3.first) {
            if(idx < 7) {
                return Pair.create(true, (Coordinate2D)test1.second.clone());
            }

            return Pair.create(true, (Coordinate2D)test3.second.clone());
        }

        if(test2.first && !test3.first) {
            if(idx < 7) {
                return Pair.create(true, (Coordinate2D)test1.second.clone());
            }

            return Pair.create(true, (Coordinate2D)test2.second.clone());
        }

        if(!test2.first && !test3.first) {
            return Pair.create(true, (Coordinate2D)test1.second.clone());
        }

        return Pair.create(false, (Coordinate2D)test1.second.clone());
    }
    //new info is added the choices are updated
    public void addData(final GuessPoint gp) {
        //add to list of guesses
        m_guessesList.add((GuessPoint)gp.clone());
        m_extendedGuessesList.add((GuessPoint)gp.clone());

        //updates the info in the array of choices
        updateChoiceMap(gp);

        //updates the head data
        updateHeadData(gp);

        //checks all head data to see if any plane positions were confirmed
        Iterator<HeadData> it = m_headDataList.iterator();


        while (it.hasNext()) {
            //if we decided upon an orientation
            //update the choice map
            //and delete the head data structure
            //append to the list of found planes
            HeadData hd = it.next();
            if (hd.m_correctOrient != -1)
            {
                Plane pl = new Plane(hd.m_headRow, hd.m_headCol, Orientation.values()[hd.m_correctOrient]);
                updateChoiceMapPlaneData(pl);
                m_guessedPlaneList.add(pl);
                it.remove();
            }
        }
    }
    //tests whether all plane positions are guessed
    public boolean areAllGuessed() {
        return (m_guessedPlaneList.size() >= m_planeNo);
    }
    //gets the number of rows
    public int getRowNo()  { return m_row; }
    //get the number of cols
    public int getColNo()  { return m_col; }
    //gets the number of planes
    public int getPlaneNo() { return m_planeNo; }
    //gets the list of guesses: TODO is this correct ?
    public Vector<GuessPoint>  getListGuesses()  { return m_guessesList; }
    public Vector<GuessPoint> getExtendedListGuesses() { return m_extendedGuessesList; }
    //gets the choices
    public Vector<Integer> getChoicesArray()  { return m_choices; }
    //computes the position in the m_choices array of a given plane
    public int mapPlaneToIndex(final Plane pl) {
        int temp = (pl.col() * m_row + pl.row()) * 4 + pl.orientation().getValue();
        return temp;
    }

    //computes the plane corresponding to a given position in the choices array
    private Plane mapIndexToPlane(int idx) {
        Orientation o = Orientation.values()[idx % 4];
        int temp = idx / 4;

        int row = temp % m_row;
        int col = temp / m_row;

        return new Plane(row, col, o);
    }
    //computes the Coordinate2D corresponding to the head of the plane corresponding to the idx
    private Coordinate2D mapIndexToQPoint(int idx) {
        int temp = idx / 4;

        int row = temp % m_row;
        int col = temp / m_row;

        return new Coordinate2D(row, col);
    }
    //make choice in find head mode
    //TODO: test
    private Pair<Boolean, Coordinate2D> makeChoiceFindHeadMode() {
        Vector<Integer> maxPos = new Vector<Integer>();

        //computes the point on the m_choices table
        //which has the highest value
        int maxidx = 0;
        maxPos.clear();

        for(int i = 1;i < maxChoiceNo; i++)
        {
            if(m_choices.get(i).equals(m_choices.get(maxidx)))
                maxPos.add(new Integer(i));

            if(m_choices.get(i) > m_choices.get(maxidx))
            {
                maxidx = i;
                maxPos.clear();
                maxPos.add(new Integer(i));
            }
        }

        //if all the choices are impossible returns false
        if(m_choices.get(maxidx) == -1)
            return Pair.create(false, new Coordinate2D(0, 0));

        //choses randomly a point with the maximum probability
        int idx = Plane.generateRandomNumber(maxPos.size());

        //converts the choice into a plane's head position
        return Pair.create(true, mapIndexToQPoint(maxPos.get(idx)));
    }
    //make choice in find plane position mode
    private Pair<Boolean, Coordinate2D> makeChoiceFindPositionMode() {
        //chose randomly a head data from the list
        //and choose randomly an orientation which is not discarded
        //select a point which was not selected from this orientation

        //if there are no head data structures return false
        if(m_headDataList.isEmpty())
            return Pair.create(false, new Coordinate2D(0, 0));

        //choses a random plane head from the list of heads
        int idx = Plane.generateRandomNumber(m_headDataList.size());
        HeadData hd = m_headDataList.get(idx);

        //find the orientation that has the most not tested points
        //and is not discarded

        int max_not_tested = 0;
        int good_orientation = -1;
        for(int i = 0; i < 4; i++)
        {
            PlaneOrientationData pod = hd.m_options[i];

            if (!pod.m_discarded) {
                if(pod.m_pointsNotTested.size() > max_not_tested) {
                    max_not_tested = pod.m_pointsNotTested.size();
                    good_orientation = i;
                }
            }
        }

        //if there is no not discarded position with more than zero points not tested
        //return false
        if(good_orientation == -1)
            return Pair.create(false, new Coordinate2D(0, 0));

        //choose randomly a point from the points not tested in the chosen orientation
        idx = Plane.generateRandomNumber(hd.m_options[good_orientation].m_pointsNotTested.size());

        return Pair.create(true, (Coordinate2D)hd.m_options[good_orientation].m_pointsNotTested.get(idx).clone());
    }
    //make a random choice
    private Pair<Boolean, Coordinate2D> makeChoiceRandomMode() {
        //find a random point which has zero score in the choice map
        int idx = Plane.generateRandomNumber(maxChoiceNo);

        //starting from the point next to the point selected
        int count = (idx + 1) % maxChoiceNo;

        //if it corresponds to a point with a choice of 0
        //choose this point
        while(count != idx)
        {
            if(m_choices.get(count) == 0) {
                return Pair.create(true, mapIndexToQPoint(count));
            }
            //if the point does not correspond to a zero choice
            //move to the next point
            count = (count + 1) % maxChoiceNo;
        }
        //loop until all the points in the m_choices table have been tested

        return Pair.create(false, new Coordinate2D(0, 0));
    }

    //updates the head data
    private void updateHeadData(final GuessPoint gp) {
        //build a list iterator that allows the modification of data
        Iterator<HeadData> it = m_headDataList.iterator();

        //updates the head data with the found guess point
        while(it.hasNext()) {
            it.next().update(gp);
        }

        //if the guess point is a head  add a new head data
        //which contains all the knowledge gathered until now
        if(gp.isDead())
        {
            //create a new head data structure
            HeadData hd = new HeadData(m_row, m_col, gp.m_row, gp.m_col);

            //update the head data with all the history of guesses
            for(int i = 0; i < m_extendedGuessesList.size(); i++)
            hd.update(m_extendedGuessesList.get(i));

            //append the head data in the list of heads
            m_headDataList.add(hd);
        }
    }

    //update the map of choices
    private void updateChoiceMap(final GuessPoint gp) {
        //marks all the 4 positions in the choice map as guessed -2
        for(int i = 0; i < 4; i++) {
            Plane plane = new Plane(gp.m_row, gp.m_col, Orientation.values()[i]);
            int idx = mapPlaneToIndex(plane);
            m_choices.set(idx, new Integer(-2));
        }

        if(gp.m_type == Type.Dead)
            updateChoiceMapDeadInfo(gp.m_row, gp.m_col);

        if(gp.m_type == Type.Hit)
            updateChoiceMapHitInfo(gp.m_row, gp.m_col);

        if(gp.m_type == Type.Miss)
            updateChoiceMapMissInfo(gp.m_row, gp.m_col);
    }
    //updates the choices with info about a dead guess
    private void updateChoiceMapDeadInfo(int row, int col) {
        //do nothing as everything is done in the updateHeadData function
        //the decision to chose a plane is made in the
        //updateHeadData function
        updateChoiceMapMissInfo(row, col);
    }
    //updates the choices with info about a hit guess
    private void updateChoiceMapHitInfo(int row, int col) {
        //for all the plane positions that are valid and that contain the
        //current position increment their score

        m_pipi.reset();

        while (m_pipi.hasNext()) {
            //obtain index for position that includes Coordinate2D(row,col)
            Plane pl = m_pipi.next();
            Coordinate2D qp = new Coordinate2D(row, col);
            //add current position to the index to obtain a plane option
            //TODO: is this correct
            pl = pl.add(qp);

            //if choice is not valid continue to the next position
            if (!pl.isPositionValid(m_row, m_col))
                continue;

            //position is valid; check first that it has not
            //being marked as invalid and that increase its score

            int idx = mapPlaneToIndex(pl);
            if (m_choices.get(idx) >= 0)
                m_choices.set(idx, m_choices.get(idx) + 1);
        }
    }
    //updates the choices with info about a miss guess
    private void updateChoiceMapMissInfo(int row, int col) {
        //discard all plane positions that contain this point
        m_pipi.reset();

        while (m_pipi.hasNext())
        {
            //obtain index for position that includes Coordinate(row,col)
            Plane pl = m_pipi.next();
            Coordinate2D qp = new Coordinate2D(row, col);
            //add current position to the index to obtain a plane option
            pl = pl.add(qp);

            //if choice is not valid continue to the next position
            if (!pl.isPositionValid(m_row, m_col))
                continue;

            //position is valid; because it includes a miss
            //it must be taken out from the list of choice

            int idx = mapPlaneToIndex(pl);
            if(m_choices.get(idx) >= 0)
                m_choices.set(idx, -1);
        }
    }
    //updates the choices with the info about a found plane
    private void updateChoiceMapPlaneData(final Plane pl) {
        //interprets a plane as a list of miss guesses
        //updates the choice map with this list of guesses
        //and appends the guesses to the list of guesses
        PlanePointIterator ppi = new PlanePointIterator(pl);

        //not to treat the head of the plane
        ppi.next();

        while (ppi.hasNext()) {
            Coordinate2D qp = ppi.next();
            GuessPoint gp = new GuessPoint(qp.x(), qp.y(), Type.Miss);
            updateChoiceMap(gp);

            int idx  = m_extendedGuessesList.indexOf(gp);
            if (idx >= 0)
                m_extendedGuessesList.remove(idx);
            m_extendedGuessesList.add(gp);
        }
    }

    //Calculate the number of choice points influenced by a point
    private int noPointsInfluenced(final Coordinate2D qp) {
        //checks to see if the point belongs already to a guess
        //or if it cannot be a viable choice
        //when this happens returns -1
        boolean point_not_good = true;

        for(int i = 0;i < 4; i++)
        {
            Plane pl = new Plane(qp, Orientation.values()[i]);
            int idx = mapPlaneToIndex(pl);
            if(m_choices.get(idx) >= 0) {
                point_not_good = false;
                break;
            }
        }

        if (point_not_good)
            return -1;

        int count = 0;

        //get the planes intersecting the point
        PlaneIntersectingPointIterator pipi = new PlaneIntersectingPointIterator(qp);

        while (pipi.hasNext()) {
            //for each plane
            Plane pl = pipi.next();
            //ignore if it's head is in the initial point
            if (pl.head().equals(qp))
                continue;

            //find the index of the point
            int idx = mapPlaneToIndex(pl);

            if (m_choices.get(idx) >= 0)
                count++;
        }

        return count;
    }


    //defines the grid size
    protected int m_row, m_col;
    //maximum number of choices
    protected int maxChoiceNo;

    //number of planes that need to be guessed
    protected int m_planeNo;

    //list of already guessed planes
    protected Vector<Plane> m_guessedPlaneList;

    //list of available data for each head in m_guessHeadList
    protected Vector<HeadData> m_headDataList;

    //list of guesses made until this moment
    protected Vector<GuessPoint> m_guessesList;
    //list of extended guesses; when the position of a plane is decided
    //all the points on this plane are considered as misses
    protected Vector<GuessPoint> m_extendedGuessesList;

    //the list of choices
    //choice -2 means that a guess has already been made
    //choice is -1 means that plane position is there impossible
    //choice 0 means no data about the choice is available
    //choice = k means that k data exist that support this choice
    protected Vector<Integer> m_choices;

    //array keeping the number of points with positive m_choice influenced by a given point
    //contains:
    //-1 when a guess has been made at this point, the position is impossible, or there is already data about this point
    //a positive number showing how many points are influenced by this point
    protected Vector<Integer> m_zero_choices;

    //put here so that we calculate the list of indices only once
    //this iterator gives all the planes that intersect the
    //point (0,0) on the grid
    protected PlaneIntersectingPointIterator m_pipi;
}

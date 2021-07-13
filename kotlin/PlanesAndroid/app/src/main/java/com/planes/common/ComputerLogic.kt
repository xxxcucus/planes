package com.planes.common

import androidx.core.util.Pair
import com.planes.common.Plane.PlaneStatic.generateRandomNumber
import java.util.*

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
class ComputerLogic(//gets the number of rows
        //defines the grid size
        var rowNo: Int, //get the number of cols
        var colNo: Int, planeno: Int) {

    //maximum number of choices
    protected var maxChoiceNo: Int

    //gets the number of planes
    //number of planes that need to be guessed
    protected var planeNo: Int


    //list of already guessed planes
    protected var m_guessedPlaneList: Vector<Plane>

    //list of available data for each head in m_guessHeadList
    protected var m_headDataList: Vector<HeadData>

    //gets the list of guesses
    //list of guesses made until this moment
    protected var listGuesses: Vector<GuessPoint>


    //list of extended guesses; when the position of a plane is decided
    //all the points on this plane are considered as misses
    protected var extendedListGuesses: Vector<GuessPoint>


    //gets the choices
    //the list of choices
    //choice -2 means that a guess has already been made
    //choice is -1 means that plane position is there impossible
    //choice 0 means no data about the choice is available
    //choice = k means that k data exist that support this choice
    protected var choicesArray: Vector<Int>

    //array keeping the number of points with positive m_choice influenced by a given point
    //contains:
    //-1 when a guess has been made at this point, the position is impossible, or there is already data about this point
    //a positive number showing how many points are influenced by this point
    protected var m_zero_choices: Vector<Int>

    //put here so that we calculate the list of indices only once
    //this iterator gives all the planes that intersect the
    //point (0,0) on the grid
    protected var m_pipi: PlaneIntersectingPointIterator

    init {
        maxChoiceNo = rowNo * colNo * 4
        planeNo = planeno

        //creates the tables of choices
        choicesArray = Vector(maxChoiceNo)
        for (i in 0 until maxChoiceNo) {
            choicesArray.add(-1)
        }
        m_zero_choices = Vector(maxChoiceNo / 4)
        for (i in 0 until maxChoiceNo / 4) {
            m_zero_choices.add(-1)
        }
        m_guessedPlaneList = Vector()
        m_headDataList = Vector()
        listGuesses = Vector()
        extendedListGuesses = Vector()
        m_pipi = PlaneIntersectingPointIterator(Coordinate2D(0, 0))

        //initializes the table of choices and the head data
        reset()
        //initializes the iterator that generates all the planes that pass
        //through (0,0)
        m_pipi.reset()
    }

    //restores the list of choices
    fun reset() {
        //initializes -1 for impossible choice (invalid plane position)
        //with 0 for possible choice
        for (i in 0 until maxChoiceNo) {
            val pl = mapIndexToPlane(i)
            if (pl.isPositionValid(rowNo, colNo)) choicesArray[i] = 0 else choicesArray[i] = -1
        }

        //clears various lists in the computerlogic object
        m_guessedPlaneList.clear()
        //m_guessedHeadList.clear();
        listGuesses.clear()
        extendedListGuesses.clear()
        m_headDataList.clear()
    }

    //returns the plane choice with the highest score and true
    //or false if there are no more valid choices
    fun makeChoice(computerSkillLevel: Int): Pair<Boolean, Coordinate2D> {
        //based on the 3 strategies of choice choses 3 possible moves

        val test1 = makeChoiceFindHeadMode()
        val test2 = makeChoiceFindPositionMode()
        val test3 = makeChoiceRandomMode()
        val skill = if (computerSkillLevel < 0 || computerSkillLevel > 2) 0 else computerSkillLevel
        val thresholds = arrayOf(intArrayOf(2, 4), intArrayOf(4, 6), intArrayOf(6, 8))

        //if there are no more choices to be tested return false
        if (!test1.first) return test1

        //generates a random number smaller than 10
        val idx = generateRandomNumber(10)

        //various random strategies for making a choice
        if (test2.first && test3.first) {
            if (idx < thresholds[skill][0]) {
                return Pair.create(true, test1.second.clone() as Coordinate2D)
            }
            return if (idx < thresholds[skill][1]) {
                Pair.create(true, test2.second.clone() as Coordinate2D)
            } else Pair.create(true, test3.second.clone() as Coordinate2D)
        }
        if (!test2.first && test3.first) {
            return if (idx < thresholds[skill][0]) {
                Pair.create(true, test1.second.clone() as Coordinate2D)
            } else Pair.create(true, test3.second.clone() as Coordinate2D)
        }
        if (test2.first && !test3.first) {
            return if (idx < thresholds[skill][0]) {
                Pair.create(true, test1.second.clone() as Coordinate2D)
            } else Pair.create(true, test2.second.clone() as Coordinate2D)
        }
        return if (!test2.first && !test3.first) {
            Pair.create(true, test1.second.clone() as Coordinate2D)
        } else Pair.create(false, test1.second.clone() as Coordinate2D)
    }

    //new info is added the choices are updated
    fun addData(gp: GuessPoint) {
        //add to list of guesses
        listGuesses.add(gp.clone() as GuessPoint)
        extendedListGuesses.add(gp.clone() as GuessPoint)

        //updates the info in the array of choices
        updateChoiceMap(gp)

        //updates the head data
        updateHeadData(gp)

        //checks all head data to see if any plane positions were confirmed
        val it = m_headDataList.iterator()
        while (it.hasNext()) {
            //if we decided upon an orientation
            //update the choice map
            //and delete the head data structure
            //append to the list of found planes
            val hd = it.next()
            if (hd.m_correctOrient != -1) {
                val pl = Plane(hd.m_headRow, hd.m_headCol, Orientation.values()[hd.m_correctOrient])
                updateChoiceMapPlaneData(pl)
                m_guessedPlaneList.add(pl)
                it.remove()
            }
        }
    }

    //tests whether all plane positions are guessed
    fun areAllGuessed(): Boolean {
        return m_guessedPlaneList.size >= planeNo
    }

    //computes the position in the m_choices array of a given plane
    fun mapPlaneToIndex(pl: Plane): Int {
        return (pl.col() * rowNo + pl.row()) * 4 + pl.orientation().value
    }

    //computes the plane corresponding to a given position in the choices array
    fun mapIndexToPlane(idx: Int): Plane {
        val o = Orientation.values()[idx % 4]
        val temp = idx / 4
        val row = temp % rowNo
        val col = temp / rowNo
        return Plane(row, col, o)
    }

    //computes the Coordinate2D corresponding to the head of the plane corresponding to the idx
    fun mapIndexToQPoint(idx: Int): Coordinate2D {
        val temp = idx / 4
        val row = temp % rowNo
        val col = temp / rowNo
        return Coordinate2D(row, col)
    }

    //make choice in find head mode
    //TODO: test
    private fun makeChoiceFindHeadMode(): Pair<Boolean, Coordinate2D> {
        val maxPos = Vector<Int>()

        //computes the point on the m_choices table
        //which has the highest value
        var maxidx = 0
        maxPos.clear()
        for (i in 1 until maxChoiceNo) {
            if (choicesArray[i] == choicesArray[maxidx]) maxPos.add(i)
            if (choicesArray[i] > choicesArray[maxidx]) {
                maxidx = i
                maxPos.clear()
                maxPos.add(i)
            }
        }

        //if all the choices are impossible returns false
        if (choicesArray[maxidx] == -1) return Pair.create(false, Coordinate2D(0, 0))

        //choses randomly a point with the maximum probability
        val idx = generateRandomNumber(maxPos.size)

        //converts the choice into a plane's head position
        return Pair.create(true, mapIndexToQPoint(maxPos[idx]))
    }

    //make choice in find plane position mode
    private fun makeChoiceFindPositionMode(): Pair<Boolean, Coordinate2D> {
        //chose randomly a head data from the list
        //and choose randomly an orientation which is not discarded
        //select a point which was not selected from this orientation

        //if there are no head data structures return false
        if (m_headDataList.isEmpty()) return Pair.create(false, Coordinate2D(0, 0))

        //choses a random plane head from the list of heads
        var idx = generateRandomNumber(m_headDataList.size)
        val hd = m_headDataList[idx]

        //find the orientation that has the most not tested points
        //and is not discarded
        var max_not_tested = 0
        var good_orientation = -1
        for (i in 0..3) {
            val pod = hd.m_options[i]
            if (!pod.m_discarded) {
                if (pod.m_pointsNotTested.size > max_not_tested) {
                    max_not_tested = pod.m_pointsNotTested.size
                    good_orientation = i
                }
            }
        }

        //if there is no not discarded position with more than zero points not tested
        //return false
        if (good_orientation == -1) return Pair.create(false, Coordinate2D(0, 0))

        //choose randomly a point from the points not tested in the chosen orientation
        idx = generateRandomNumber(hd.m_options[good_orientation].m_pointsNotTested.size)
        return Pair.create(true, hd.m_options[good_orientation].m_pointsNotTested[idx].clone() as Coordinate2D)
    }

    //make a random choice
    private fun makeChoiceRandomMode(): Pair<Boolean, Coordinate2D> {
        //find a random point which has zero score in the choice map
        val idx = generateRandomNumber(maxChoiceNo)

        //starting from the point next to the point selected
        var count = (idx + 1) % maxChoiceNo

        //if it corresponds to a point with a choice of 0
        //choose this point
        while (count != idx) {
            if (choicesArray[count] == 0) {
                return Pair.create(true, mapIndexToQPoint(count))
            }
            //if the point does not correspond to a zero choice
            //move to the next point
            count = (count + 1) % maxChoiceNo
        }
        //loop until all the points in the m_choices table have been tested
        return Pair.create(false, Coordinate2D(0, 0))
    }

    //updates the head data
    private fun updateHeadData(gp: GuessPoint) {
        //build a list iterator that allows the modification of data
        val it: Iterator<HeadData> = m_headDataList.iterator()

        //updates the head data with the found guess point
        while (it.hasNext()) {
            it.next().update(gp)
        }

        //if the guess point is a head  add a new head data
        //which contains all the knowledge gathered until now
        if (gp.isDead) {
            //create a new head data structure
            val hd = HeadData(rowNo, colNo, gp.row(), gp.col())

            //update the head data with all the history of guesses
            for (i in extendedListGuesses.indices) hd.update(extendedListGuesses[i])

            //append the head data in the list of heads
            m_headDataList.add(hd)
        }
    }

    //update the map of choices
    private fun updateChoiceMap(gp: GuessPoint) {
        //marks all the 4 positions in the choice map as guessed -2
        for (i in 0..3) {
            val plane = Plane(gp.row(), gp.col(), Orientation.values()[i])
            val idx = mapPlaneToIndex(plane)
            choicesArray[idx] = -2
        }
        if (gp.type() === Type.Dead) updateChoiceMapDeadInfo(gp.row(), gp.col())
        if (gp.type() === Type.Hit) updateChoiceMapHitInfo(gp.row(), gp.col())
        if (gp.type() === Type.Miss) updateChoiceMapMissInfo(gp.row(), gp.col())
    }

    //updates the choices with info about a dead guess
    private fun updateChoiceMapDeadInfo(row: Int, col: Int) {
        //do nothing as everything is done in the updateHeadData function
        //the decision to chose a plane is made in the
        //updateHeadData function
        updateChoiceMapMissInfo(row, col)
    }

    //updates the choices with info about a hit guess
    private fun updateChoiceMapHitInfo(row: Int, col: Int) {
        //for all the plane positions that are valid and that contain the
        //current position increment their score
        m_pipi.reset()
        while (m_pipi.hasNext()) {
            //obtain index for position that includes Coordinate2D(row,col)
            var pl = m_pipi.next()
            val qp = Coordinate2D(row, col)
            //add current position to the index to obtain a plane option
            pl = pl.add(qp)

            //if choice is not valid continue to the next position
            if (!pl.isPositionValid(rowNo, colNo)) continue

            //position is valid; check first that it has not
            //being marked as invalid and that increase its score
            val idx = mapPlaneToIndex(pl)
            if (choicesArray[idx] >= 0) choicesArray[idx] = choicesArray[idx] + 1
        }
    }

    //updates the choices with info about a miss guess
    private fun updateChoiceMapMissInfo(row: Int, col: Int) {
        //discard all plane positions that contain this point
        m_pipi.reset()
        while (m_pipi.hasNext()) {
            //obtain index for position that includes Coordinate(row,col)
            var pl = m_pipi.next()
            val qp = Coordinate2D(row, col)
            //add current position to the index to obtain a plane option
            pl = pl.add(qp)

            //if choice is not valid continue to the next position
            if (!pl.isPositionValid(rowNo, colNo)) continue

            //position is valid; because it includes a miss
            //it must be taken out from the list of choice
            val idx = mapPlaneToIndex(pl)
            if (choicesArray[idx] >= 0) choicesArray[idx] = -1
        }
    }

    //updates the choices with the info about a found plane
    private fun updateChoiceMapPlaneData(pl: Plane) {
        //interprets a plane as a list of miss guesses
        //updates the choice map with this list of guesses
        //and appends the guesses to the list of guesses
        val ppi = PlanePointIterator(pl)

        //not to treat the head of the plane
        ppi.next()
        while (ppi.hasNext()) {
            val qp = ppi.next()
            val gp = GuessPoint(qp.x(), qp.y(), Type.Miss)
            updateChoiceMap(gp)
            val idx = extendedListGuesses.indexOf(gp)
            if (idx >= 0) extendedListGuesses.removeAt(idx)
            extendedListGuesses.add(gp)
        }
    }
}
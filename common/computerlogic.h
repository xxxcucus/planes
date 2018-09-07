#ifndef COMPUTERLOGIC_H
#define COMPUTERLOGIC_H

#include "plane.h"
#include "guesspoint.h"
#include "planeiterators.h"
#include "coordinate2d.h"
#include <QList>
#include <vector>


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

struct PlaneOrientationData
{

    //the position of the plane
    Plane m_plane;

    //whether this orientation was discarded
    bool m_discarded;
    //points on this plane that were not tested
    //if m_discarded is false it means that all the
    //tested points were hits
    std::vector<PlanesCommonTools::Coordinate2D> m_pointsNotTested;

    //default constructor
    PlaneOrientationData();
    //another constructor
    PlaneOrientationData(const Plane& pl, bool isDiscarded);
    //copy constructor
    PlaneOrientationData(const PlaneOrientationData& pod);
    //equals operator
    void operator =(const PlaneOrientationData& pod);

    //update the info about this plane with another guess point
    //a guess point is a pair (position, guess result)
    void update(const GuessPoint& gp);
    //verifies if all the points in the current orientation were already checked
    bool areAllPointsChecked();
};

//This structure keeps the information about the position of the head of the planes

struct HeadData
{
    //size of the grid
    int m_row, m_col;
    //position of the head
    int m_headRow, m_headCol;
    //the correct plane orientation if decided
    int m_correctOrient;


    //statistics about the 4 positions with this head
    PlaneOrientationData m_options[4];

    HeadData(int row, int col, int headRow, int headCol);
    //update the current data with a guess
    //return true if a plane is confirmed
    bool update(const GuessPoint& gp);
};



//implements the logic of computer's game
class ComputerLogic
{
protected:
    //defines the grid size
    int m_row, m_col;
    //maximum number of choices
    const int maxChoiceNo;

    //number of planes that need to be guessed
    int m_planeNo;

    //list of already guessed planes
    QList<Plane> m_guessedPlaneList;

    //list of available data for each head in m_guessHeadList
    QList<HeadData> m_headDataList;

    //list of guesses made until this moment
    QList<GuessPoint> m_guessesList;
    //list of extended guesses; when the position of a plane is decided
    //all the points on this plane are considered as misses
    QList<GuessPoint> m_extendedGuessesList;

    //the list of choices
    //choice -2 means that a guess has already been made
    //choice is -1 means that plane position is there impossible
    //choice 0 means no data about the choice is available
    //choice = k means that k data exist that support this choice
    int* m_choices;

    //array keeping the number of points with positive m_choice influenced by a given point
    //contains:
    //-1 when a guess has been made at this point, the position is impossible, or there is already data about this point
    //a positive number showing how many points are influenced by this point
    int* m_zero_choices;

    //put here so that we calculate the list of indices only once
    //this iterator gives all the planes that intersect the
    //point (0,0) on the grid
    PlaneIntersectingPointIterator m_pipi;

public:
    ComputerLogic(int row, int col, int planeno);
    ~ComputerLogic();
    //restores the list of choices
    void reset();
    //returns the plane choice with the highest score and true
    //or false if there are no more valid choices
    bool makeChoice(PlanesCommonTools::Coordinate2D& qp) const;
    //new info is added the choices are updated
    void addData(const GuessPoint& gp);
    //tests whether all plane positions are guessed
    bool areAllGuessed() const;
    //gets the number of rows
    int getRowNo() const { return m_row; }
    //get the number of cols
    int getColNo() const { return m_col; }
    //gets the number of planes
    int getPlaneNo() const { return m_planeNo; }
    //gets the list of guesses
    const QList<GuessPoint>&  getListGuesses() const { return m_guessesList; }
    const QList<GuessPoint>& getExtendedListGuesses() const { return m_extendedGuessesList; }
    //gets the choices
    const int* getChoicesArray() const { return m_choices; }
    //computes the position in the m_choices array of a given plane
    int mapPlaneToIndex(const Plane& pl) const;

private:
    //computes the plane corresponding to a given position in the choices array
    Plane mapIndexToPlane(int idx) const;
    //computes the Coordinate2D corresponding to the head of the plane corresponding to the idx
    PlanesCommonTools::Coordinate2D mapIndexToQPoint(int idx) const;
    //make choice in find head mode
    bool makeChoiceFindHeadMode(PlanesCommonTools::Coordinate2D& qp) const;
    //make choice in find plane position mode
    bool makeChoiceFindPositionMode(PlanesCommonTools::Coordinate2D& qp) const;
    //make a random choice
    bool makeChoiceRandomMode(PlanesCommonTools::Coordinate2D& qp) const;

    //updates the head data
    void updateHeadData(const GuessPoint& gp);

    //update the map of choices
    void updateChoiceMap(const GuessPoint& gp);
    //updates the choices with info about a dead guess
    void updateChoiceMapDeadInfo(int row, int col);
    //updates the choices with info about a hit guess
    void updateChoiceMapHitInfo(int row, int col);
    //updates the choices with info about a miss guess
    void updateChoiceMapMissInfo(int row, int col);
    //updates the choices with the info about a found plane
    void updateChoiceMapPlaneData(const Plane& pl);

    //Calculate the number of choice points influenced by a point
    int noPointsInfluenced(const PlanesCommonTools::Coordinate2D& qp);
};


//this is a class that reverts the moves in the computer strategy
class RevertComputerLogic: public ComputerLogic
{
    //the list of guess points
    QList<GuessPoint> m_playList;
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
    bool hasNext() { return m_pos < m_playList.size() - 1; }
};

#endif // COMPUTERLOGIC_H

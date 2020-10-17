#include <algorithm>
#include "computerlogic.h"

//constructs a computer logic object
//m_row, m_col give the size of the grid
//maxChoiceNo is the number of possible plane positions on the grid
//m_PlaneNo are the number of planes that need to be guessed
ComputerLogic::ComputerLogic(int row, int col, int planeno):
    m_row(row),
    m_col(col),
    maxChoiceNo(row * col * 4),
    m_planeNo(planeno)
{
    //creates the tables of choices
    m_choices = new int[maxChoiceNo];
    m_zero_choices = new int[maxChoiceNo / 4];

    //initializes the table of choices and the head data
    reset();

    //initializes the iterator that generates all the planes that pass
    //through (0,0)
    m_pipi.reset();
}

//selects all the possible plane positions that are valid within the given grid
void ComputerLogic::reset()
{
    //initializes -1 for impossible choice (invalid plane position)
    //with 0 for possible choice
    for(int i = 0; i < maxChoiceNo; i++)
    {
        Plane pl = mapIndexToPlane(i);
        if (pl.isPositionValid(m_row, m_col))
            m_choices[i] = 0;
        else
            m_choices[i] = -1;
    }

    //clears various lists in the computerlogic object
    m_guessedPlaneList.clear();
    //m_guessedHeadList.clear();
    m_guessesList.clear();
    m_extendedGuessesList.clear();
    m_headDataList.clear();
}

//destructor
ComputerLogic::~ComputerLogic()
{
    //deletes the object containing the choices
    delete [] m_choices;
    delete [] m_zero_choices;
}

//computes the position in the m_choices array of a given plane
int ComputerLogic::mapPlaneToIndex(const Plane& pl) const
{
    int temp = (pl.col() * m_row + pl.row()) * 4 + (int)pl.orientation();
    return temp;
}

//computes the plane corresponding to a given position in the choices array
Plane ComputerLogic::mapIndexToPlane(int idx) const
{
    Plane::Orientation o = (Plane::Orientation)(idx % 4);
    int temp = idx / 4;

    int row = temp % m_row;
    int col = temp / m_row;

    return Plane(row, col, o);
}

//computes the plane head position corresponding for a given position in the choices array
PlanesCommonTools::Coordinate2D ComputerLogic::mapIndexToQPoint(int idx) const
{
    int temp = idx / 4;

    int row = temp % m_row;
    int col = temp / m_row;

    return PlanesCommonTools::Coordinate2D(row, col);
}

//chooses the next point
bool ComputerLogic::makeChoice(PlanesCommonTools::Coordinate2D& qp, int computerSkillLevel) const
{
    //based on the 3 strategies of choice choses 3 possible moves
    PlanesCommonTools::Coordinate2D qp1, qp2, qp3;

	int skill = computerSkillLevel < 0 || computerSkillLevel > 2 ? 0 : computerSkillLevel;
	int thresholds[3][2] = { { 2, 4 }, { 4, 6 }, { 6, 8 } };

    bool test1 = makeChoiceFindHeadMode(qp1);
    bool test2 = makeChoiceFindPositionMode(qp2);
    bool test3 = makeChoiceRandomMode(qp3);

    //if there are no more choices to be tested return false
    if(!test1)
        return false;

    //generates a random number smaller than 10
    int idx = Plane::generateRandomNumber(10);

    //various random strategies for making a choice
    if(test2 && test3) {
        if(idx < thresholds[skill][0]) {
            qp = qp1;
            return true;
        }

        if(idx < thresholds[skill][1]) {
            qp = qp2;
            return true;
        }

        qp = qp3;
        return true;
    }

    if(!test2 && test3) {
        if(idx < thresholds[skill][0]) {
            qp = qp1;
            return true;
        }

        qp = qp3;
        return true;
    }

    if(test2 && !test3) {
        if(idx < thresholds[skill][0]) {
            qp = qp1;
            return true;
        }

        qp = qp2;
        return true;
    }

    if(!test2 && !test3) {
        qp = qp1;
        return true;
    }

    return false;
}


//choses the most likely point to be a head's plane on the players grid

bool ComputerLogic::makeChoiceFindHeadMode(PlanesCommonTools::Coordinate2D& qp) const
{
    std::vector<int> maxPos;

    //computes the point on the m_choices table
    //which has the highest value
    int maxidx = 0;
    maxPos.clear();

    for(int i = 1;i < maxChoiceNo; i++)
    {
        if(m_choices[i] == m_choices[maxidx])
            maxPos.push_back(i);

        if(m_choices[i] > m_choices[maxidx])
        {
            maxidx = i;
            maxPos.clear();
            maxPos.push_back(i);
        }
    }

    //if all the choices are impossible returns false
    if(m_choices[maxidx] == -1)
        return false;

    //choses randomly a point with the maximum probability
    int idx = Plane::generateRandomNumber(static_cast<int>(maxPos.size()));

    //converts the choice into a plane's head position
     qp = mapIndexToQPoint(maxPos[idx]);
    return true;
}


//after finding one or more heads the computer tries to
//determine the real position of the found plane
bool ComputerLogic::makeChoiceFindPositionMode(PlanesCommonTools::Coordinate2D& qp) const
{
    //chose randomly a head data from the list
    //and choose randomly an orientation which is not discarded
    //select a point which was not selected from this orientation

    //if there are no head data structures return false
    if(m_headDataList.empty())
        return false;

    //choses a random plane head from the list of heads
    int idx = Plane::generateRandomNumber(static_cast<int>(m_headDataList.size()));
    HeadData hd = m_headDataList.at(idx);

    //find the orientation that has the most not tested points
    //and is not discarded

    int max_not_tested = 0;
    int good_orientation = -1;
    for(int i = 0; i < 4; i++)
    {
        PlaneOrientationData pod = hd.m_options[i];

        if (!pod.m_discarded) {
            if(static_cast<int>(pod.m_pointsNotTested.size()) > max_not_tested) {
                max_not_tested = static_cast<int>(pod.m_pointsNotTested.size());
                good_orientation = i;
            }
        }
    }

    //if there is no not discarded position with more than zero points not tested
    //return false
    if(good_orientation == -1)
        return false;

    //choose randomly a point from the points not tested in the chosen orientation
    idx = Plane::generateRandomNumber(static_cast<int>(hd.m_options[good_orientation].m_pointsNotTested.size()));

    qp = hd.m_options[good_orientation].m_pointsNotTested.at(idx);

    return true;
}

//computer choses a point about which has no
//positive or negative data
bool ComputerLogic::makeChoiceRandomMode(PlanesCommonTools::Coordinate2D& qp) const
{
    //find a random point which has zero score in the choice map
    int idx = Plane::generateRandomNumber(maxChoiceNo);

    //starting from the point next to the point selected
    int count = (idx + 1) % maxChoiceNo;

   //if it corresponds to a point with a choice of 0
    //choose this point
    while(count != idx)
    {
        if(m_choices[count] == 0) {
            qp = mapIndexToQPoint(count);
            return true;
        }
        //if the point does not correspond to a zero choice
        //move to the next point
        count = (count + 1) % maxChoiceNo;
    }
    //loop until all the points in the m_choices table have been tested

    return false;
}

//checks whether if the computer has guessed everything
bool ComputerLogic::areAllGuessed() const
{
    return (static_cast<int>(m_guessedPlaneList.size()) >= m_planeNo);
}

//new info is added the choices are updated
//we assume that choices do not repeat themselves
//for safety we should keep a list of guesses and keep
//checking if they repeat
void ComputerLogic::addData(const GuessPoint& gp)
{
    //add to list of guesses
    m_guessesList.push_back(gp);
    m_extendedGuessesList.push_back(gp);

    //updates the info in the array of choices
    updateChoiceMap(gp);

    //updates the head data
    updateHeadData(gp);

    //checks all head data to see if any plane positions were confirmed
    auto it = m_headDataList.begin();


    while (it != m_headDataList.end()) {
        //if we decided upon an orientation
        //update the choice map
        //and delete the head data structure
        //append to the list of found planes
        if (it->m_correctOrient != -1)
        {
            Plane pl(it->m_headRow, it->m_headCol, (Plane::Orientation)it->m_correctOrient);
            updateChoiceMapPlaneData(pl);
            m_guessedPlaneList.push_back(pl);
            it = m_headDataList.erase(it);
        } else {
            ++it;
        }
    }
}

//updates the computer choices
void ComputerLogic::updateChoiceMap(const GuessPoint& gp) {

    //marks all the 4 positions in the choice map as guessed -2
    for(int i = 0; i < 4; i++) {
        Plane plane(gp.m_row, gp.m_col, (Plane::Orientation)i);
        int idx = mapPlaneToIndex(plane);
        m_choices[idx] = -2;
    }

    if(gp.m_type == GuessPoint::Dead)
        updateChoiceMapDeadInfo(gp.m_row, gp.m_col);

    if(gp.m_type == GuessPoint::Hit)
        updateChoiceMapHitInfo(gp.m_row, gp.m_col);

    if(gp.m_type == GuessPoint::Miss)
        updateChoiceMapMissInfo(gp.m_row, gp.m_col);
}

void ComputerLogic::updateChoiceMapPlaneData(const Plane& pl)
{
    //interprets a plane as a list of miss guesses
    //updates the choice map with this list of guesses
    //and appends the guesses to the list of guesses
    PlanePointIterator ppi(pl);

    //not to treat the head of the plane
    ppi.next();

    while (ppi.hasNext()) {
        PlanesCommonTools::Coordinate2D qp = ppi.next();
        GuessPoint gp(qp.x(), qp.y(), GuessPoint::Miss);
        updateChoiceMap(gp);

        auto it = std::find(m_extendedGuessesList.begin(), m_extendedGuessesList.end(), gp);
        if (it != m_extendedGuessesList.end())
			m_extendedGuessesList.erase(it);
        m_extendedGuessesList.push_back(gp);
    }
}

//updates the choices with info about a dead guess
void ComputerLogic::updateChoiceMapDeadInfo(int row, int col)
{
    //do nothing as everything is done in the updateHeadData function
    //the decision to chose a plane is made in the
    //updateHeadData function
    updateChoiceMapMissInfo(row, col);
}

//updates the choices with info about a hit guess
void ComputerLogic::updateChoiceMapHitInfo(int row, int col)
{
    //for all the plane positions that are valid and that contain the
    //current position increment their score

    m_pipi.reset();

    while (m_pipi.hasNext()) {
        //obtain index for position that includes Coordinate2D(row,col)
        Plane pl = m_pipi.next();
        PlanesCommonTools::Coordinate2D qp(row, col);
        //add current position to the index to obtain a plane option
        pl = pl + qp;

        //if choice is not valid continue to the next position
        if (!pl.isPositionValid(m_row, m_col))
            continue;

        //position is valid; check first that it has not
        //being marked as invalid and that increase its score

        int idx = mapPlaneToIndex(pl);
        if (m_choices[idx] >= 0)
            m_choices[idx]++;
    }
}

//updates the choices with info about a miss guess
void ComputerLogic::updateChoiceMapMissInfo(int row, int col)
{

    //discard all plane positions that contain this point
    m_pipi.reset();

    while (m_pipi.hasNext())
    {
        //obtain index for position that includes Coordinate(row,col)
        Plane pl = m_pipi.next();
        PlanesCommonTools::Coordinate2D qp(row, col);
        //add current position to the index to obtain a plane option
        pl = pl + qp;

        //if choice is not valid continue to the next position
        if (!pl.isPositionValid(m_row, m_col))
            continue;

        //position is valid; because it includes a miss
        //it must be taken out from the list of choice

        int idx = mapPlaneToIndex(pl);
        if(m_choices[idx] >= 0)
            m_choices[idx] = -1;
    }
}

//updates the head data with a new guess
void ComputerLogic::updateHeadData(const GuessPoint& gp)
{
    //build a list iterator that allows the modification of data
    auto it = m_headDataList.begin();

    //updates the head data with the found guess point
    while(it != m_headDataList.end()) {
        it->update(gp);
        ++it;
    }

    //if the guess point is a head  add a new head data
    //which contains all the knowledge gathered until now
    if(gp.isDead())
    {
        //create a new head data structure
        HeadData hd(m_row, m_col, gp.m_row, gp.m_col);

        //update the head data with all the history of guesses
        for(unsigned int i = 0; i < m_extendedGuessesList.size(); i++)
            hd.update(m_extendedGuessesList.at(i));

        //append the head data in the list of heads
        m_headDataList.push_back(hd);
    }
}




#include "planeorientationdata.h"
#include "planeiterators.h"

#include <algorithm>

//default constructor
PlaneOrientationData::PlaneOrientationData()
{
	m_plane = Plane(0, 0, (Plane::Orientation)0);
	m_discarded = true;
	m_pointsNotTested.clear();
}

//useful constructor
PlaneOrientationData::PlaneOrientationData(const Plane& pl, bool isDiscarded) :
	m_plane(pl),
	m_discarded(isDiscarded)
{
	PlanePointIterator ppi(m_plane);

	//all points of the plane besides the head are not tested yet
	ppi.next();

	while (ppi.hasNext())
	{
		m_pointsNotTested.push_back(ppi.next());
	}
}

//copy constructor
PlaneOrientationData::PlaneOrientationData(const PlaneOrientationData& pod) :
	m_plane(pod.m_plane),
	m_discarded(pod.m_discarded),
	m_pointsNotTested(pod.m_pointsNotTested) {
}
//assignment operator
void PlaneOrientationData::operator=(const PlaneOrientationData &pod)
{
	m_plane = pod.m_plane;
	m_discarded = pod.m_discarded;
	m_pointsNotTested = pod.m_pointsNotTested;
}

void PlaneOrientationData::update(const GuessPoint &gp)
{
	//if plane is discarded return
	if (m_discarded)
		return;

	//find the guess point in the list of points not tested
	auto it = std::find(m_pointsNotTested.begin(), m_pointsNotTested.end(), PlanesCommonTools::Coordinate2D(gp.m_row, gp.m_col));

	//if point not found return
	if (it == m_pointsNotTested.end())
		return;

	//if point found
	//if dead and idx = 0 remove the head from the list of untested points
	//TODO: this is not needed. The head is not in the list
	if (gp.m_type == GuessPoint::Dead && m_plane.isHead(PlanesCommonTools::Coordinate2D(gp.m_row, gp.m_col)))
	{
		m_pointsNotTested.erase(it);
		return;
	}

	//if miss or dead discard plane
	if (gp.m_type == GuessPoint::Miss || gp.m_type == GuessPoint::Dead)
		m_discarded = true;

	//if hit take point out of the list of points not tested
	if (gp.m_type == GuessPoint::Hit)
		m_pointsNotTested.erase(it);
}

//checks to see that all points on the plane were tested
bool PlaneOrientationData::areAllPointsChecked()
{
	return (m_pointsNotTested.empty());
}

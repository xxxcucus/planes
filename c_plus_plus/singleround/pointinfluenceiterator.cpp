#include "pointinfluenceiterator.h"
#include "planepointiterator.h"
#include "coordinate2d.h"
#include <algorithm>


PointInfluenceIterator::PointInfluenceIterator(const PlanesCommonTools::Coordinate2D& qp):
    PlanesCommonTools::VectorIterator<PlanesCommonTools::Coordinate2D>(),
    m_point(qp)
{
    generateList();
}

void PointInfluenceIterator::generateList()
{
    m_internalList.clear();

	for (int i = 0; i < 4; i++) {
		Plane pl(m_point.x(), m_point.y(), (Plane::Orientation)i);
		PlanePointIterator ppi(pl);

		while (ppi.hasNext()) {
			PlanesCommonTools::Coordinate2D point = ppi.next();
			if (std::find(m_internalList.begin(), m_internalList.end(), point) == m_internalList.end())
				m_internalList.push_back(point);
		}
	}
}

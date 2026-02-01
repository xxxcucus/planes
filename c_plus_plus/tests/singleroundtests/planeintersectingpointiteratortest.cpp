#include "planeintersectingpointiteratortest.h"
#include <QTest>
#include "coordinate2d.h"
#include "planeintersectingpointiterator.h"



void PlaneIntersectingPointIteratorTest::initTestCase()
{
    qDebug("PlaneIntersectingPointIteratorTest starts ..");
}

void PlaneIntersectingPointIteratorTest::PlaneIntersectingPointIterator_Values() {
    PlanesCommonTools::Coordinate2D point = PlanesCommonTools::Coordinate2D(0, 0);
    PlaneIntersectingPointIterator pipi = PlaneIntersectingPointIterator(point);
    int count = 0;
    while (pipi.hasNext()) {
        Plane pl = pipi.next();
        QVERIFY(pl.containsPoint(point));
        count++;
    }
    QVERIFY(count > 0);
}

void PlaneIntersectingPointIteratorTest::cleanupTestCase()
{
    qDebug("PlaneIntersectingPointIteratorTest ends ..");
}
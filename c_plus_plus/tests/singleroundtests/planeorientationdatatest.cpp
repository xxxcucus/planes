#include "planeorientationdatatest.h"
#include <QTest>
#include "planeorientationdata.h"
#include "planeiterators.h"

void PlaneOrientationDataTest::initTestCase()
{
    qDebug("PlaneOrientationDataTest starts ..");
}

void PlaneOrientationDataTest::PlaneOrientationData_AreAllPointsTested() {
    PlaneOrientationData pod = PlaneOrientationData();
    QVERIFY(pod.areAllPointsChecked());
}

void PlaneOrientationDataTest::PlaneOrientationData_Discard() {
    Plane pl = Plane(0, 0, Plane::Orientation::NorthSouth);
    PlaneOrientationData pod = PlaneOrientationData(pl, false);

    PlanePointIterator ppi(pl);
    ppi.next();
    PlanesCommonTools::Coordinate2D pointOnPlane = ppi.next(); //first point on plane after the head

    pod.update(GuessPoint(pointOnPlane.x(), pointOnPlane.y(), GuessPoint::Type::Miss));
    QVERIFY(pod.m_discarded);
}

void PlaneOrientationDataTest::PlaneOrientationData_HitNotTestedList() {
    Plane pl = Plane(0, 0, Plane::Orientation::NorthSouth);
    PlaneOrientationData pod = PlaneOrientationData(pl, false);

    PlanePointIterator ppi(pl);
    ppi.next();
    PlanesCommonTools::Coordinate2D pointOnPlane = ppi.next(); //first point on plane after the head

    int notTestedBeforeCount = (int)pod.m_pointsNotTested.size();

    pod.update(GuessPoint(pointOnPlane.x(), pointOnPlane.y(), GuessPoint::Type::Hit));
    QVERIFY(!pod.m_discarded);
    int notTestedAfterCount = (int)pod.m_pointsNotTested.size();
    QVERIFY(notTestedAfterCount + 1 == notTestedBeforeCount);
}

void PlaneOrientationDataTest::cleanupTestCase() {
    qDebug("PlaneOrientationDataTest ends ..");
}
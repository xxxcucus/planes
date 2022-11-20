#include "orientationtest.h"
#include <QTest>
#include "plane.h"



void OrientationTest::initTestCase()
{
    qDebug("OrientationTest starts ..");
}

void OrientationTest::Orientation_Values() {
    QVERIFY(0 == (int)Plane::Orientation::NorthSouth);
    QVERIFY(1 == (int)Plane::Orientation::SouthNorth);
    QVERIFY(2 == (int)Plane::Orientation::WestEast);
    QVERIFY(3 == (int)Plane::Orientation::EastWest);
}


void OrientationTest::cleanupTestCase()
{
    qDebug("OrientationTest ends ..");
}
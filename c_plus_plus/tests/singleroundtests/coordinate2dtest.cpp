#include "coordinate2dtest.h"
#include <QTest>
#include "coordinate2d.h"



void Coordinate2DTest::initTestCase()
{
    qDebug("Coordinate2DTest starts ..");
}


void Coordinate2DTest::Coordinate2D_x() {
    PlanesCommonTools::Coordinate2D c1 = PlanesCommonTools::Coordinate2D(1, 1);
    QVERIFY(c1.x() == 1);
}


void Coordinate2DTest::Coordinate2D_y() {
    PlanesCommonTools::Coordinate2D c2 = PlanesCommonTools::Coordinate2D(1, 1);
    QVERIFY(c2.y() == 1);
}


void Coordinate2DTest::Coordinate2D_add() {
    PlanesCommonTools::Coordinate2D c1 = PlanesCommonTools::Coordinate2D(1, 0);
    PlanesCommonTools::Coordinate2D c2 = PlanesCommonTools::Coordinate2D(2, 4);
    PlanesCommonTools::Coordinate2D c3 = PlanesCommonTools::Coordinate2D(3, 4);
    QVERIFY(c1 + c2 == c3);
}

void Coordinate2DTest::cleanupTestCase()
{
    qDebug("Coordinate2DTest ends ..");
}
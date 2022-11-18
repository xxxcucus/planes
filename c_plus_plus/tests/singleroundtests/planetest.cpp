#include "planetest.h"
#include <QTest>
#include "plane.h"
#include "coordinate2d.h"
#include "planeiterators.h"


void PlaneTest::initTestCase()
{
    qDebug("PlaneTest starts ..");
}

void  PlaneTest::PlaneAdd() {
    Plane pl = Plane(0, 0, Plane::Orientation::EastWest);
    Plane pl_copy = Plane(pl);
    Plane pl_add = pl + PlanesCommonTools::Coordinate2D(1, 2);
    QCOMPARE(1, pl_add.head().x());
    QCOMPARE(2, pl_add.head().y());
    QVERIFY(pl.orientation() == pl_copy.orientation());
}


void PlaneTest::PlaneRotate() {
    Plane pl = Plane(0, 0, Plane::Orientation::EastWest);
    Plane pl_copy = Plane(pl);
    for (int i = 0; i < 4; i++) {
        pl.rotate();
        QVERIFY(pl.head() == pl_copy.head());
    }
    QVERIFY(pl == pl_copy);
}


void PlaneTest::PlaneTranslateWhenHeadPosValid() {
    int rows = 10;
    int cols = 10;
    Plane pl = Plane(rows / 2, 0, Plane::Orientation::NorthSouth);
    pl.translateWhenHeadPosValid(-1, -1, rows, cols);
    QVERIFY(pl.head() == PlanesCommonTools::Coordinate2D(rows / 2, 0));
}


void PlaneTest::PlaneContainsPoint() {
    Plane pl = Plane(0, 0, Plane::Orientation::EastWest);
    PlanePointIterator ppi = PlanePointIterator(pl);
    while (ppi.hasNext()) {
        PlanesCommonTools::Coordinate2D point = ppi.next();
        QVERIFY(pl.containsPoint(point));
    }
    Plane pl1 = Plane(0, 0, Plane::Orientation::WestEast);
    PlanePointIterator ppi1 = PlanePointIterator(pl1);
    while (ppi1.hasNext()) {
        PlanesCommonTools::Coordinate2D point = ppi1.next();
        if (point == pl1.head())
            continue;
        QVERIFY(!pl.containsPoint(point));
    }
}


void PlaneTest::PlaneIsPositionValid() {
    int rows = 10;
    int cols = 10;
    Plane pl = Plane(rows / 2, cols / 2, Plane::Orientation::WestEast);
    QVERIFY(pl.isPositionValid(rows, cols));
    Plane pl1 = Plane(1, cols / 2, Plane::Orientation::NorthSouth);
    QVERIFY(!pl1.isPositionValid(rows, cols));
}

void PlaneTest::cleanupTestCase()
{
    qDebug("PlaneTest ends ..");
}
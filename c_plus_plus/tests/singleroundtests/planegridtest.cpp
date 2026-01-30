#include "planegridtest.h"
#include <QTest>
#include "planegrid.h"
#include "plane.h"
#include "coordinate2d.h"
#include "planeiterators.h"
#include "planepointiterator.h"



void PlaneGridTest::initTestCase()
{
    qDebug("OrientationTest starts ..");
}

void PlaneGridTest::PlaneGridTest_SaveSearchPlane() {
    int rows = 10;
    int cols = 10;
    PlaneGrid grid = PlaneGrid(rows, cols, 3, false);
    grid.clearPlanes();
    Plane pl1 = Plane(rows / 2, 0, Plane::Orientation::NorthSouth);
    QVERIFY(pl1.isPositionValid(rows, cols));
    Plane pl2 = Plane(0, 6, Plane::Orientation::EastWest);
    QVERIFY(pl2.isPositionValid(rows, cols));
    Plane pl3 = Plane(6, 6, Plane::Orientation::EastWest);
    QVERIFY(pl3.isPositionValid(rows, cols));
    grid.savePlane(pl1);
    grid.savePlane(pl2);
    grid.savePlane(pl3);
    QVERIFY(grid.searchPlane(pl1) == 0);
    QVERIFY(grid.searchPlane(pl2) == 1);
    QVERIFY(grid.searchPlane(pl3) == 2);
    QVERIFY(grid.searchPlane(0, 0) < 0);
    QVERIFY(grid.searchPlane(pl1.row(), pl1.col()) == 0);
    QVERIFY(grid.searchPlane(pl2.row(), pl2.col()) == 1);
    QVERIFY(grid.searchPlane(pl3.row(), pl3.col()) == 2);
}


void PlaneGridTest::PlaneGridTest_RemoveSearchPlane() {
    int rows = 10;
    int cols = 10;
    PlaneGrid grid = PlaneGrid(rows, cols, 3, false);
    grid.clearPlanes();
    Plane pl1 = Plane(rows / 2, 0, Plane::Orientation::NorthSouth);
    QVERIFY(pl1.isPositionValid(rows, cols));
    Plane pl2 = Plane(0, 6, Plane::Orientation::EastWest);
    QVERIFY(pl2.isPositionValid(rows, cols));
    Plane pl3 = Plane(6, 6, Plane::Orientation::EastWest);
    QVERIFY(pl3.isPositionValid(rows, cols));
    grid.savePlane(pl1);
    grid.savePlane(pl2);
    grid.savePlane(pl3);
    Plane pl;
    bool result = grid.removePlane(0, pl);
    QVERIFY(result);
    QVERIFY(pl == pl1);
    QVERIFY(grid.searchPlane(pl2) == 0);
    QVERIFY(grid.searchPlane(pl3) == 1);
    QVERIFY(grid.searchPlane(pl1) < 0);
    grid.removePlane(pl2);
    QVERIFY(grid.searchPlane(pl2) < 0);
    QVERIFY(grid.searchPlane(pl3) == 0);
}


void PlaneGridTest::PlaneGridTest_isPointOnPlane() {
    int rows = 10;
    int cols = 10;
    PlaneGrid grid = PlaneGrid(rows, cols, 3, false);
    grid.clearPlanes();
    grid.setPlanePoints({ PlanesCommonTools::Coordinate2D(0, 0), PlanesCommonTools::Coordinate2D(0, 1) });
    int idx0 = 0;
    bool res0 = grid.isPointOnPlane(0, 0, idx0);
    QVERIFY(res0);
    QVERIFY(idx0 == 0);
    int idx1 = 0;
    bool res1 = grid.isPointOnPlane(0, 1, idx1);
    QVERIFY(res1);
    QVERIFY(idx1 == 1);
    int idx2 = 0;
    bool res2 = grid.isPointOnPlane(0, 2, idx2);
    QVERIFY(!res2);
}


void PlaneGridTest::PlaneGridTest_computePlanePointListNoOverlapAllInsideGrid() {
    int rows = 10;
    int cols = 10;
    PlaneGrid grid = PlaneGrid(rows, cols, 3, false);
    grid.clearPlanes();
    std::vector<Plane> pl_list = std::vector<Plane>({ Plane(rows / 2, 0, Plane::Orientation::NorthSouth),
            Plane(0, 6, Plane::Orientation::EastWest), Plane(6, 6, Plane::Orientation::EastWest) });
    grid.savePlane(pl_list[0]);
    grid.savePlane(pl_list[1]);
    grid.savePlane(pl_list[2]);
    grid.computePlanePointsList(false);
    for (int i = 0; i < 3; i++) {
        PlanePointIterator ppi = PlanePointIterator(pl_list[i]);
        while (ppi.hasNext()) {
            PlanesCommonTools::Coordinate2D point = ppi.next();
            int idx = 0;
            QVERIFY(grid.isPointOnPlane(point.x(), point.y(), idx));
        }
    }
}

void PlaneGridTest::PlaneGridTest_computePlanePointListOutsideGrid() {
    int rows = 10;
    int cols = 10;
    PlaneGrid grid = PlaneGrid(rows, cols, 3, false);
    grid.clearPlanes();
    std::vector<Plane> pl_list = std::vector<Plane>({ Plane(rows / 2, 0, Plane::Orientation::NorthSouth),
            Plane(0, 6, Plane::Orientation::WestEast), Plane(6, 6, Plane::Orientation::EastWest) });
    grid.savePlane(pl_list[0]);
    grid.savePlane(pl_list[1]);
    grid.savePlane(pl_list[2]);
    grid.computePlanePointsList(false);
    QVERIFY(grid.isPlaneOutsideGrid());
}


void PlaneGridTest::PlaneGridTest_computePlanePointListPlanesOverlap() {
    int rows = 10;
    int cols = 10;
    PlaneGrid grid = PlaneGrid(rows, cols, 3, false);
    grid.clearPlanes();
    std::vector<Plane> pl_list = std::vector<Plane>({ Plane(rows / 2, 0, Plane::Orientation::NorthSouth),
            Plane(0, 6, Plane::Orientation::EastWest), Plane(6, 6, Plane::Orientation::WestEast) });
    grid.savePlane(pl_list[0]);
    grid.savePlane(pl_list[1]);
    grid.savePlane(pl_list[2]);
    QVERIFY(!grid.computePlanePointsList(false));
}


void PlaneGridTest::PlaneGridTest_generateAnnotation() {
    int rows = 10;
    int cols = 10;
    PlaneGrid grid = PlaneGrid(rows, cols, 3, false);
    grid.clearPlanes();
    QVERIFY(grid.generateAnnotation(0, false) == 1);
    QVERIFY(grid.generateAnnotation(0, true) == 2);
}


void PlaneGridTest::PlaneGridTest_getGuessResult() {
    int rows = 10;
    int cols = 10;
    PlaneGrid grid = PlaneGrid(rows, cols, 3, false);
    grid.clearPlanes();
    std::vector<Plane> pl_list = std::vector<Plane>({ Plane(rows / 2, 0, Plane::Orientation::NorthSouth),
            Plane(0, 6, Plane::Orientation::EastWest), Plane(6, 6, Plane::Orientation::EastWest) });
    grid.savePlane(pl_list[0]);
    grid.savePlane(pl_list[1]);
    grid.savePlane(pl_list[2]);
    grid.computePlanePointsList(false);
    QVERIFY(grid.getGuessResult(PlanesCommonTools::Coordinate2D(rows / 2, 0)) == GuessPoint::Type::Dead);
    QVERIFY(grid.getGuessResult(PlanesCommonTools::Coordinate2D(rows / 2, 1)) == GuessPoint::Type::Hit);
    QVERIFY(grid.getGuessResult(PlanesCommonTools::Coordinate2D(rows / 2 + 1, 0)) == GuessPoint::Type::Miss);
}


void PlaneGridTest::PlaneGridTest_decodeAnnotation() {
    int rows = 10;
    int cols = 10;
    PlaneGrid grid = PlaneGrid(rows, cols, 3, false);
    grid.clearPlanes();
    QVERIFY(grid.decodeAnnotation(1) == std::vector({0}));
    QVERIFY(grid.decodeAnnotation(2) == std::vector({ -1 }));
    QVERIFY(grid.decodeAnnotation(5) == std::vector({ 0, 1 }));
    QVERIFY(grid.decodeAnnotation(9) == std::vector({ 0, -2 }));
}

void PlaneGridTest::PlaneGridTest_addGuessPoints() {
    int rows = 10;
    int cols = 10;
    PlaneGrid grid = PlaneGrid(rows, cols, 3, false);
    grid.clearPlanes();
    grid.addGuess(GuessPoint(0, 0, GuessPoint::Type::Hit));
    std::vector<GuessPoint> guesses = grid.getGuesses();
    QVERIFY(guesses.size() == 1);
    QVERIFY(guesses[0] == GuessPoint(0, 0, GuessPoint::Type::Hit));
}


void PlaneGridTest::PlaneGridTest_getPlanePoints() {
    int rows = 10;
    int cols = 10;
    PlaneGrid grid = PlaneGrid(rows, cols, 3, false);
    grid.clearPlanes();
    std::vector<Plane> pl_list = std::vector<Plane>({ Plane(rows / 2, 0, Plane::Orientation::NorthSouth),
            Plane(0, 6, Plane::Orientation::EastWest), Plane(6, 6, Plane::Orientation::EastWest) });
    grid.savePlane(pl_list[0]);
    grid.savePlane(pl_list[1]);
    grid.savePlane(pl_list[2]);
    std::vector<PlanesCommonTools::Coordinate2D> points;
    bool planePointsResult = grid.getPlanePoints(-1, points);
    QVERIFY(points.size() == 0);
    QVERIFY(!planePointsResult);
    bool planePointsResult1 = grid.getPlanePoints(0, points);
    QVERIFY(points.size() == 9);
    QVERIFY(planePointsResult1);
}


void PlaneGridTest::cleanupTestCase()
{
    qDebug("OrientationTest ends ..");
}
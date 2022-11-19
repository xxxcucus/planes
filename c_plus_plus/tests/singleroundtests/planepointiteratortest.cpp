#include "planepointiteratortest.h"
#include <QTest>
#include "planeiterators.h"
#include "plane.h"



void PlanePointIteratorTest::initTestCase()
{
    qDebug("PlanePointIteratorTest starts ..");
}

void PlanePointIteratorTest::PlanePointIteratorTest::PlanePointIterator_GenerateListNorthSouth() {
    Plane pl = Plane(0, 0, Plane::Orientation::NorthSouth);
    PlanePointIterator ppi = PlanePointIterator(pl);
    std::vector<PlanesCommonTools::Coordinate2D> planePoints = std::vector<PlanesCommonTools::Coordinate2D>();
    std::vector<int> yCoords = std::vector<int>();
    while (ppi.hasNext()) {
        PlanesCommonTools::Coordinate2D c = ppi.next();
        planePoints.push_back(c);
        yCoords.push_back(c.y());
    }
    QVERIFY(planePoints.size() == 10);
    std::map<int, int> histoY = computeHisto(yCoords);
    QVERIFY(histoY[0] == 1);
    QVERIFY(histoY[1] == 5);
    QVERIFY(histoY[2] == 1);
    QVERIFY(histoY[3] == 3);
}


void PlanePointIteratorTest::PlanePointIterator_GenerateListSouthNorth() {
    Plane pl = Plane(0, 0, Plane::Orientation::SouthNorth);
    PlanePointIterator ppi = PlanePointIterator(pl);
    std::vector<PlanesCommonTools::Coordinate2D> planePoints = std::vector<PlanesCommonTools::Coordinate2D>();
    std::vector<int> yCoords = std::vector<int>();
    while (ppi.hasNext()) {
        PlanesCommonTools::Coordinate2D c = ppi.next();
        planePoints.push_back(c);
        yCoords.push_back(c.y());
    }
    QVERIFY(planePoints.size() == 10);
    std::map<int, int> histoY = computeHisto(yCoords);
    QVERIFY(histoY[0] == 1);
    QVERIFY(histoY[-1] == 5);
    QVERIFY(histoY[-2] == 1);
    QVERIFY(histoY[-3] == 3);
}


void PlanePointIteratorTest::PlanePointIterator_GenerateListEastWest() {
    Plane pl = Plane(0, 0, Plane::Orientation::EastWest);
    PlanePointIterator ppi = PlanePointIterator(pl);
    std::vector<PlanesCommonTools::Coordinate2D> planePoints = std::vector<PlanesCommonTools::Coordinate2D>();
    std::vector<int> xCoords = std::vector<int>();
    while (ppi.hasNext()) {
        PlanesCommonTools::Coordinate2D c = ppi.next();
        planePoints.push_back(c);
        xCoords.push_back(c.x());
    }
    QVERIFY(planePoints.size() == 10);
    std::map<int, int> histoX = computeHisto(xCoords);
    QVERIFY(histoX[0] == 1);
    QVERIFY(histoX[1] == 5);
    QVERIFY(histoX[2] == 1);
    QVERIFY(histoX[3] == 3);
}


void PlanePointIteratorTest::PlanePointIterator_GenerateListWestEast() {
    Plane pl = Plane(0, 0, Plane::Orientation::WestEast);
    PlanePointIterator ppi = PlanePointIterator(pl);
    std::vector<PlanesCommonTools::Coordinate2D> planePoints = std::vector<PlanesCommonTools::Coordinate2D>();
    std::vector<int> xCoords = std::vector<int>();
    while (ppi.hasNext()) {
        PlanesCommonTools::Coordinate2D c = ppi.next();
        planePoints.push_back(c);
        xCoords.push_back(c.x());
    }
    QVERIFY(planePoints.size() == 10);
    std::map<int, int> histoX = computeHisto(xCoords);
    QVERIFY(histoX[0] == 1);
    QVERIFY(histoX[-1] == 5);
    QVERIFY(histoX[-2] == 1);
    QVERIFY(histoX[-3] == 3);
}

std::map<int, int> PlanePointIteratorTest::computeHisto(const std::vector<int> values) {
    std::map<int, int> retVal = std::map<int, int>();
    for (int value : values) {
        if (retVal.find(value) != retVal.end()) {
            retVal[value] = retVal[value] + 1;
        } else {
            retVal[value] = 1;
        }
    }
    return retVal;
}

void PlanePointIteratorTest::cleanupTestCase()
{
    qDebug("PlanePointIteratorTest ends ..");
}
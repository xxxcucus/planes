#include "computerlogictest.h"
#include <QTest>
#include "computerlogic.h"
#include "planegrid.h"

void ComputerLogicTest::initTestCase() {
    qDebug("ComputerLogicTest starts ..");
}


void ComputerLogicTest::ComputerLogic_MapIndexToPlane() {
    ComputerLogic cl = ComputerLogic(10, 10, 3);
    QVERIFY(cl.mapPlaneToIndex(cl.mapIndexToPlane(50)) == 50);
}

void ComputerLogicTest::ComputerLogic_UpdateChoiceMapMissInfo() {

    //find a plane that passes through (5, 5)
    PlaneGrid grid = PlaneGrid(10, 10, 3, false);
    Plane pl = Plane(4, 5, Plane::Orientation::EastWest);
    grid.savePlane(pl);
    grid.computePlanePointsList(false);
    int idx = 0;
    QVERIFY(grid.isPointOnPlane(5, 5, idx));

    ComputerLogic cl = ComputerLogic(10, 10, 3);
    cl.updateChoiceMapMissInfo(5, 5);
    int index = cl.mapPlaneToIndex(pl);
    QVERIFY(cl.m_choices[index] == -1);

}

void ComputerLogicTest::ComputerLogic_UpdateChoiceMapHitInfo() {
    //find a plane that passes through (5, 5)
    PlaneGrid grid = PlaneGrid(10, 10, 3, false);
    Plane pl = Plane(4, 5, Plane::Orientation::EastWest);
    grid.savePlane(pl);
    grid.computePlanePointsList(false);
    int idx = 0;
    QVERIFY(grid.isPointOnPlane(5, 5, idx));

    ComputerLogic cl = ComputerLogic(10, 10, 3);
    int index = cl.mapPlaneToIndex(pl);
    cl.m_choices[index] = 1;
    cl.updateChoiceMapHitInfo(5, 5);
    QVERIFY(cl.m_choices[index] == 2);
}

void ComputerLogicTest::ComputerLogic_UpdateChoiceMapPlaneData() {
    ComputerLogic cl = ComputerLogic(10, 10, 3);
    Plane pl = Plane(4, 5, Plane::Orientation::EastWest);
    cl.updateChoiceMapPlaneData(pl);

    PlaneGrid grid = PlaneGrid(10, 10, 3, false);
    grid.savePlane(pl);
    grid.computePlanePointsList(false);

    for (GuessPoint gp : cl.m_extendedGuessesList) {
        int idx = 0;
        QVERIFY(grid.isPointOnPlane(gp.m_row, gp.m_col, idx));
    }
}

void ComputerLogicTest::ComputerLogic_MakeChoiceFindHeadModeMaxExists() {

    ComputerLogic cl = ComputerLogic(10, 10, 3);
    Plane pl = Plane(4, 5, Plane::Orientation::EastWest);
    int index = cl.mapPlaneToIndex(pl);

    cl.m_choices[index] = 1;

    PlanesCommonTools::Coordinate2D coords;
    cl.makeChoiceFindHeadMode(coords);

    QVERIFY(PlanesCommonTools::Coordinate2D(4, 5) == coords);
}

void ComputerLogicTest::ComputerLogic_MakeChoiceFindHeadModeMaxDoesNotExist() {
    ComputerLogic cl = ComputerLogic(10, 10, 3);
    
    for (int i = 0; i < (int)cl.m_choices.size(); i++) {
        cl.m_choices[i] = -1;
    }

    PlanesCommonTools::Coordinate2D coords;
    QVERIFY(!cl.makeChoiceFindHeadMode(coords));
}

void ComputerLogicTest::ComputerLogic_MakeChoiceFindPositionModeChoiceDoesNotExist() {
    ComputerLogic cl = ComputerLogic(10, 10, 3);
    HeadData hd(10, 10, 5, 5);
    for (int i = 0; i < 4; i++)
        hd.m_options[i].m_discarded = true;
    cl.m_headDataList.push_back(hd);

    PlanesCommonTools::Coordinate2D coords;
    QVERIFY(!cl.makeChoiceFindPositionMode(coords));
}

void ComputerLogicTest::ComputerLogic_MakeChoiceFindPositionModeChoiceDoesExist() {
    ComputerLogic cl = ComputerLogic(10, 10, 3);
    HeadData hd(10, 10, 5, 5);
    for (int i = 0; i < 3; i++)
        hd.m_options[i].m_discarded = true;
    cl.m_headDataList.push_back(hd);

    PlanesCommonTools::Coordinate2D coords;
    cl.makeChoiceFindPositionMode(coords);

    std::vector<PlanesCommonTools::Coordinate2D> notTested = hd.m_options[3].m_pointsNotTested;
    auto it = std::find(begin(notTested), end(notTested), coords);
    QVERIFY(it != std::end(notTested));
}

void ComputerLogicTest::ComputerLogic_MakeChoiceRandomModeChoiceDoesExist() {
    ComputerLogic cl = ComputerLogic(10, 10, 3);
    PlanesCommonTools::Coordinate2D coords;
    cl.makeChoiceRandomMode(coords);

    bool found = false;

    for (int i = 0; i < 4; i++) {
        Plane pl = Plane(coords.x(), coords.y(), (Plane::Orientation)i);
        int index = cl.mapPlaneToIndex(pl);
        if (cl.m_choices[index] == 0)
            found = true;
    }

    QVERIFY(found);
}

void ComputerLogicTest::ComputerLogic_MakeChoiceRandomModeChoiceDoesNotExist() {
    ComputerLogic cl = ComputerLogic(10, 10, 3);
    for (int i = 0; i < (int)cl.m_choices.size(); i++) {
        cl.m_choices[i] = -1;
    }

    PlanesCommonTools::Coordinate2D coords;
    QVERIFY(!cl.makeChoiceRandomMode(coords));
}

void ComputerLogicTest::cleanupTestCase() {
    qDebug("ComputerLogicTest ends ..");
}
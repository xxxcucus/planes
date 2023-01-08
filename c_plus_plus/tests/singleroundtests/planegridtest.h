#ifndef __PLANE_GRID_TEST__
#define __PLANE_GRID_TEST__


#include <QObject>
#include <QTest>
#include <QDebug>
#include "planegrid.h"


class PlaneGridTest : public QObject {
    Q_OBJECT

private slots:
    void initTestCase();
    void PlaneGridTest_SaveSearchPlane();
    void PlaneGridTest_RemoveSearchPlane();
    void PlaneGridTest_isPointOnPlane();
    void PlaneGridTest_computePlanePointListNoOverlapAllInsideGrid();
    void PlaneGridTest_computePlanePointListOutsideGrid();
    void PlaneGridTest_computePlanePointListPlanesOverlap();
    void PlaneGridTest_generateAnnotation();
    void PlaneGridTest_getGuessResult();
    void PlaneGridTest_decodeAnnotation();
    void PlaneGridTest_addGuessPoints();
    void PlaneGridTest_getPlanePoints();
    void cleanupTestCase();
};

#endif
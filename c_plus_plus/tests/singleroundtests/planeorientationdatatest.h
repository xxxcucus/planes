#ifndef __PLANE_ORIENTATION_DATA_TEST__
#define __PLANE_ORIENTATION_DATA_TEST__


#include <QObject>
#include <QTest>


class PlaneOrientationDataTest : public QObject {
    Q_OBJECT


private slots:
    void initTestCase();
    void PlaneOrientationData_AreAllPointsTested();
    void PlaneOrientationData_Discard();
    void PlaneOrientationData_HitNotTestedList();
    void cleanupTestCase();
};

#endif
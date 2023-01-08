#ifndef __PLANE_TEST__
#define __PLANE_TEST__


#include <QObject>
#include <QTest>


class PlaneTest : public QObject {
    Q_OBJECT


private slots:
    void initTestCase();
    void PlaneAdd();
    void PlaneRotate();
    void PlaneTranslateWhenHeadPosValid();
    void PlaneContainsPoint();
    void PlaneIsPositionValid();
    void cleanupTestCase();
};

#endif
#ifndef __COORDINATE2D_TEST__
#define __COORDINATE2D_TEST__


#include <QObject>
#include <QTest>


class Coordinate2DTest : public QObject {
    Q_OBJECT


private slots:
    void initTestCase();
    void Coordinate2D_x();
    void Coordinate2D_y();
    void Coordinate2D_add();
    void cleanupTestCase();
};

#endif
#ifndef __PLANE_INTERSECTING_POINT_ITERATOR_TEST__
#define __PLANE_INTERSECTING_POINT_ITERATOR_TEST__

#include <QObject>
#include <QTest>

class PlaneIntersectingPointIteratorTest : public QObject {
    Q_OBJECT


private slots:
    void initTestCase();
    void PlaneIntersectingPointIterator_Values();
    void cleanupTestCase();
};

#endif
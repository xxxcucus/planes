#ifndef __PLANEPOINT_ITERATOR_TEST__
#define __PLANEPOINT_ITERATOR_TEST__

#include <QObject>
#include <QTest>

class PlanePointIteratorTest : public QObject {
    Q_OBJECT

private:
    std::map<int, int> computeHisto(const std::vector<int> values);

private slots:
    void initTestCase();
    void PlanePointIterator_GenerateListNorthSouth();
    void PlanePointIterator_GenerateListSouthNorth();
    void PlanePointIterator_GenerateListEastWest();
    void PlanePointIterator_GenerateListWestEast();
    void cleanupTestCase();
};

#endif
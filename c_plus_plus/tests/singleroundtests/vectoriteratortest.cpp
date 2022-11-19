#include "vectoriteratortest.h"
#include <QTest>
#include "vectoriterator.h"



void VectorIteratorTest::initTestCase()
{
    qDebug("VectorIteratorTest starts ..");
}


void VectorIteratorTest::VectorIterator_Iterate() {
    PlanesCommonTools::VectorIterator<int> testVectorIterator = PlanesCommonTools::VectorIterator<int>();
    std::vector<int> testVector = { 1, 2, 3 };  
    testVectorIterator.setInternalList(testVector);
    int count = 0;
    while (testVectorIterator.hasNext()) {
        count++;
        QVERIFY(testVectorIterator.next() == count);
    }
}

void VectorIteratorTest::cleanupTestCase()
{
    qDebug("VectorIteratorTest ends ..");
}
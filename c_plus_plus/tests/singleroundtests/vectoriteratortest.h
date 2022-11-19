#ifndef __VECTOR_ITERATOR_TEST__
#define __VECTOR_ITERATOR_TEST__

#include <QObject>
#include <QTest>

class VectorIteratorTest : public QObject {
    Q_OBJECT


private slots:
    void initTestCase();
    void VectorIterator_Iterate();
    void cleanupTestCase();
};

#endif
#ifndef __ORIENTATION_TEST__
#define __ORIENTATION_TEST__


#include <QObject>
#include <QTest>


class OrientationTest : public QObject {
    Q_OBJECT


private slots:
    void initTestCase();
    void Orientation_Values();
    void cleanupTestCase();
};

#endif
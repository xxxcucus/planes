#ifndef __GUESSPOINT_TYPE_TEST__
#define __GUESSPOINT_TYPE_TEST__


#include <QObject>
#include <QTest>


class GuessPointTypeTest : public QObject {
    Q_OBJECT


private slots:
    void initTestCase();
    void Type_Values();
    void cleanupTestCase();
};

#endif
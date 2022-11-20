#ifndef __GUESSPOINT_TYPE_TEST__
#define __GUESSPOINT_TYPE_TEST__


#include <QObject>
#include <QTest>


class GuessPointTest : public QObject {
    Q_OBJECT


private slots:
    void initTestCase();
    void Type_Values();
    void GuessPoint_SetType();
    void cleanupTestCase();
};

#endif
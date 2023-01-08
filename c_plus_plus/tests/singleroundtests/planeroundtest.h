#ifndef __PLANE_ROUND_TEST__
#define __PLANE_ROUND_TEST__


#include <QObject>
#include <QTest>


class PlaneRoundTest : public QObject {
    Q_OBJECT


private slots:
    void initTestCase();
    void PlaneRound_playerGuessAlreadyMade();
    void cleanupTestCase();
};

#endif
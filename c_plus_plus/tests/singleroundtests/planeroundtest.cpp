#include "planeroundtest.h"
#include <QTest>
#include "planeround.h"

void PlaneRoundTest::initTestCase()
{
    qDebug("PlaneRoundTest starts ..");
}

void PlaneRoundTest::PlaneRound_playerGuessAlreadyMade() {
    PlaneRound pr = PlaneRound(10, 10, 3);
    QVERIFY(pr.playerGuessAlreadyMade(0, 0) == 0);
}

void PlaneRoundTest::cleanupTestCase()
{
    qDebug("PlaneRoundTest ends ..");
}
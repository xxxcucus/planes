#include "gamestagestest.h"
#include <QTest>
#include "abstractplaneround.h"



void GameStagesTest::initTestCase()
{
    qDebug("GameStagesTest starts ..");
}

void GameStagesTest::GameStages_Values() {
    QVERIFY(0 == (int)AbstractPlaneRound::GameStages::GameNotStarted);
    QVERIFY(1 == (int)AbstractPlaneRound::GameStages::BoardEditing);
    QVERIFY(2 == (int)AbstractPlaneRound::GameStages::Game);
    QVERIFY(3 == (int)AbstractPlaneRound::GameStages::WaitForOpponentPlanesPositions);
}


void GameStagesTest::cleanupTestCase()
{
    qDebug("GameStagesTest ends ..");
}
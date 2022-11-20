#include "guesspointtest.h"
#include <QTest>
#include "guesspoint.h"



void GuessPointTest::initTestCase()
{
    qDebug("GuessPointTest starts ..");
}

void GuessPointTest::Type_Values() {
    QVERIFY(0 == (int)GuessPoint::Type::Miss);
    QVERIFY(1 == (int)GuessPoint::Type::Hit);
    QVERIFY(2 == (int)GuessPoint::Type::Dead);
}

void GuessPointTest::GuessPoint_SetType() {
    GuessPoint gp1 = GuessPoint(1, 1);
    GuessPoint gp2 = GuessPoint(1, 1, GuessPoint::Type::Dead);
    gp1.setType(GuessPoint::Type::Dead);
    QVERIFY(gp1 == gp2);
}

void GuessPointTest::cleanupTestCase()
{
    qDebug("GuessPointTypeTest ends ..");
}
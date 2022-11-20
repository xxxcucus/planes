#include "guesspointtypetest.h"
#include <QTest>
#include "guesspoint.h"



void GuessPointTypeTest::initTestCase()
{
    qDebug("GuessPointTypeTest starts ..");
}

void GuessPointTypeTest::Type_Values() {
    QVERIFY(0 == (int)GuessPoint::Type::Miss);
    QVERIFY(1 == (int)GuessPoint::Type::Hit);
    QVERIFY(2 == (int)GuessPoint::Type::Dead);
}

void GuessPointTypeTest::cleanupTestCase()
{
    qDebug("GuessPointTypeTest ends ..");
}
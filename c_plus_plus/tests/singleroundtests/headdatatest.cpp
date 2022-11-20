#include "headdatatest.h"
#include <QTest>
#include "headdata.h"
#include "guesspoint.h"

void HeadDataTest::initTestCase()
{
    qDebug("HeadDataTest starts ..");
}

void HeadDataTest::HeadData_Init() {
    HeadData hd = HeadData(10, 10, 5, 5);
    hd.update(GuessPoint(5, 5, GuessPoint::Type::Dead));
}

void HeadDataTest::HeadData_UpdateCorrectOrientSet() {
    HeadData hd = HeadData(10, 10, 5, 5);
    hd.m_correctOrient = 0;
    QVERIFY(hd.update(GuessPoint(0, 0)));
}

void HeadDataTest::HeadData_UpdateOneOrientationCompletAndNotDiscarded() {
    HeadData hd = HeadData(10, 10, 5, 5);
    PlaneOrientationData pod = PlaneOrientationData();  //already correct orientation found
    pod.m_discarded = false;
    hd.m_options[0] = pod;
    QVERIFY(hd.update(GuessPoint(0, 0)));
    QVERIFY(0 == hd.m_correctOrient);
}

void HeadDataTest::HeadData_UpdateThreeOrientationsDiscardedAndOneNotDiscarded() {
    HeadData hd = HeadData(10, 10, 5, 5);
    hd.m_options[0].m_discarded = true;
    hd.m_options[1].m_discarded = true;
    hd.m_options[2].m_discarded = true;
    hd.m_options[3].m_discarded = false;
    QVERIFY(hd.update(GuessPoint(0, 0)));
    QVERIFY(3 == hd.m_correctOrient);
}

void HeadDataTest::HeadData_UpdateFourOrientationsDiscarded() {
    HeadData hd = HeadData(10, 10, 5, 5);
    hd.m_options[0].m_discarded = true;
    hd.m_options[1].m_discarded = true;
    hd.m_options[2].m_discarded = true;
    hd.m_options[3].m_discarded = true;
    QVERIFY(!hd.update(GuessPoint(0, 0)));
}

void HeadDataTest::HeadData_UpdateTwoOrientationsDiscardedAndTwoNotDiscarded() {
    HeadData hd = HeadData(10, 10, 5, 5);
    hd.m_options[0].m_discarded = true;
    hd.m_options[1].m_discarded = true;
    hd.m_options[2].m_discarded = false;
    hd.m_options[3].m_discarded = false;
    QVERIFY(!hd.update(GuessPoint(0, 0)));
}

void HeadDataTest::cleanupTestCase() {
    qDebug("HeadDataTest ends ..");
}
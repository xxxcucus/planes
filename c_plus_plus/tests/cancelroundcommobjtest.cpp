#include "cancelroundcommobjtest.h"
#include <QTest>

QTEST_MAIN(CancelRoundCommObjTest)

void CancelRoundCommObjTest::initTestCase()
{
    qDebug("Called before everything else.");
}

void CancelRoundCommObjTest::SinglePlayerTest()
{
    m_CommObj.m_IsSinglePlayer = true;
    QVERIFY(m_CommObj.makeRequest() == false, "CancelRoundCommObj should abort if single player game"); 
}

void CancelRoundCommObjTest::NoUserLoggedInTest()
{
    m_CommObj.m_IsSinglePlayer = false;
    GlobalData* gd = new GlobalData();
    gd->m_UserData.m_UserName = "";
    m_CommObj.m_GlobalData = gd;
    QVERIFY(m_CommObj.makeRequest() == false, "Cannot cancel round without being logged in");
}

void CancelRoundCommObjTest::cleanupTestCase()
{
    qDebug("Called after myFirstTest and mySecondTest.");
}
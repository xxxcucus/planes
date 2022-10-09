#include "cancelroundcommobjtest.h"
#include <QTest>

QTEST_MAIN(CancelRoundCommObjTest)

void CancelRoundCommObjTest::initTestCase()
{
    qDebug("CancelRoundCommObjTest starts ..");
}

void CancelRoundCommObjTest::SinglePlayerTest()
{
    m_CommObj.m_IsSinglePlayer = true;
    QVERIFY(m_CommObj.makeRequest() == false, "CancelRoundCommObj should abort if single player game"); 
}

void CancelRoundCommObjTest::NoUserLoggedInTest()
{
    m_CommObj.m_IsSinglePlayer = false;
    m_CommObj.m_ParentWidget = nullptr;
    GlobalData* gd = new GlobalData();
    gd->m_UserData.m_UserName = "";
    m_CommObj.m_GlobalData = gd;
    QVERIFY(m_CommObj.makeRequest() == false, "Cannot cancel round without being logged in");
}

void CancelRoundCommObjTest::PrepareViewModelTest()
{
    GlobalData* gd = new GlobalData();
    gd->m_UserData.m_UserName = "testUserName";
    gd->m_GameData.m_RoundId = 123L;
    gd->m_GameData.m_GameId = 234L;
    m_CommObj.m_GlobalData = gd;
    
    CancelRoundViewModel viewModel = m_CommObj.prepareViewModel();

    QVERIFY(viewModel.m_GameId == 234L, "GameId was not copied to the view model");
    QVERIFY(viewModel.m_RoundId == 123L, "RoundId was not copied to the view model");
}



void CancelRoundCommObjTest::cleanupTestCase()
{
    qDebug("CancelRoundCommObjTest ends ..");
}
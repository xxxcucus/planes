#include "sendwinnercommobjtest.h"
#include "viewmodels/sendwinnerviewmodel.h"
#include <QTest>


void SendWinnerCommObjTest::initTestCase()
{
    qDebug("SendWinnerCommObjTest starts ..");
}

void SendWinnerCommObjTest::SinglePlayerTest()
{
    m_CommObj.m_IsSinglePlayer = true;
    QVERIFY2(m_CommObj.makeRequest(false, 0L) == false, "SendWinnerCommObj should abort if single player game");
}

void SendWinnerCommObjTest::NoUserLoggedInTest()
{
    m_CommObj.m_IsSinglePlayer = false;
    m_CommObj.m_ParentWidget = nullptr;
    GlobalData* gd = new GlobalData();
    gd->m_UserData.m_UserName = "";
    m_CommObj.m_GlobalData = gd;
    QVERIFY2(m_CommObj.makeRequest(false, 0L) == false, "Cannot send winner without being logged in");
}

void SendWinnerCommObjTest::PrepareViewModelTest()
{   
    GlobalData* gd = new GlobalData();
    gd->m_GameData.m_GameId = 1L;
    gd->m_GameData.m_RoundId = 2L;
    m_CommObj.m_GlobalData = gd;
    SendWinnerViewModel viewModel = m_CommObj.prepareViewModel(false, 0L);
    
    QVERIFY2(viewModel.m_GameId == 1L, "GameId was not set to zero");
    QVERIFY2(viewModel.m_RoundId == 2L, "RoundId was not set to zero");
    QVERIFY2(viewModel.m_Draw == false, "Draw was not set");
    QVERIFY2(viewModel.m_WinnerId == 0L, "WinnerId was not set");
}

void SendWinnerCommObjTest::cleanupTestCase()
{
    qDebug("SendWinnerCommObjTest ends ..");

}

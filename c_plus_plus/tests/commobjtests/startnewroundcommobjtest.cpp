#include "startnewroundcommobjtest.h"
#include "viewmodels/startnewroundviewmodel.h"
#include <QTest>
#include <QSignalSpy>


void StartNewRoundCommObjTest::initTestCase()
{
    qDebug("StartNewRoundCommObjTest starts ..");
}

void StartNewRoundCommObjTest::SinglePlayerTest()
{
    m_CommObj.m_IsSinglePlayer = true;
    QVERIFY2(m_CommObj.makeRequest() == false, "StartNewRoundCommObj should abort if single player game");
}

void StartNewRoundCommObjTest::NoUserLoggedInTest()
{
    m_CommObj.m_IsSinglePlayer = false;
    m_CommObj.m_ParentWidget = nullptr;
    GlobalData* gd = new GlobalData();
    gd->m_UserData.m_UserName = "";
    m_CommObj.m_GlobalData = gd;
    QVERIFY2(m_CommObj.makeRequest() == false, "Cannot start new round without being logged in");
}

void StartNewRoundCommObjTest::PrepareViewModelTest()
{   
    GlobalData* gd = new GlobalData();
    gd->m_GameData.m_GameId = 1L;
    gd->m_GameData.m_UserId = 2L;
    gd->m_GameData.m_OtherUserId = 3L;
    m_CommObj.m_GlobalData = gd;
    StartNewRoundViewModel viewModel = m_CommObj.prepareViewModel();
    
    QVERIFY2(viewModel.m_GameId == 1L, "GameId was not set to zero");
    QVERIFY2(viewModel.m_UserId == 2L, "UserId was not set to zero");
    QVERIFY2(viewModel.m_OpponentUserId == 3L, "OpponentUserId was not set to zero");
}

void StartNewRoundCommObjTest::ProcessResponseTest() {
    GlobalData* gd = new GlobalData();
    m_CommObj.m_GlobalData = gd;

    QJsonObject jsonObject;
    jsonObject.insert("roundId", QJsonValue("1"));

    MultiplayerRoundMock* multiRoundMock = new MultiplayerRoundMock();
    m_CommObj.m_MultiRound = multiRoundMock;
    QSignalSpy spy(&m_CommObj, SIGNAL(startNewRound()));

    m_CommObj.processResponse(jsonObject);
    QVERIFY(1L == m_CommObj.m_GlobalData->m_GameData.m_RoundId);
    QCOMPARE(spy.count(), 1);
}

void StartNewRoundCommObjTest::cleanupTestCase()
{
    qDebug("StartNewRoundCommObjTest ends ..");

}

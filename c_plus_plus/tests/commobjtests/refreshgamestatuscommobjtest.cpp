#include "refreshgamestatuscommobjtest.h"
#include "viewmodels/gameviewmodel.h"
#include <QTest>
#include <QSignalSpy>


void RefreshGameStatusCommObjTest::initTestCase()
{
    qDebug("RefreshGameStatusCommObjTest starts ..");
}

void RefreshGameStatusCommObjTest::SinglePlayerTest()
{
    m_CommObj.m_IsSinglePlayer = true;
    QVERIFY2(m_CommObj.makeRequest("testName") == false, "RefreshGameStatusCommObj should abort if single player game");
}

void RefreshGameStatusCommObjTest::NoUserLoggedInTest()
{
    m_CommObj.m_IsSinglePlayer = false;
    m_CommObj.m_ParentWidget = nullptr;
    GlobalData* gd = new GlobalData();
    gd->m_UserData.m_UserName = "";
    m_CommObj.m_GlobalData = gd;
    QVERIFY2(m_CommObj.makeRequest("testName") == false, "Cannot cancel round without being logged in");
}

void RefreshGameStatusCommObjTest::PrepareViewModelTest()
{   
    GlobalData* gd = new GlobalData();
    gd->m_UserData.m_UserName = "testUserName";
    m_CommObj.m_GlobalData = gd;
    GameViewModel viewModel = m_CommObj.prepareViewModel("testName");
    
    QVERIFY2(viewModel.m_GameId == 0L, "GameId was not set to zero");
    QVERIFY2(viewModel.m_UserId == 0L, "UserId was not set to zero");
    QVERIFY2(viewModel.m_GameName == "testName", "GameName was not set");
    QVERIFY2(viewModel.m_Username == "testUserName", "Username was not set");
}

void RefreshGameStatusCommObjTest::ProcessResponseTest() {
    GlobalData* gd = new GlobalData();
    m_CommObj.m_GlobalData = gd;
    m_CommObj.m_GlobalData->m_UserData.m_UserId = 3L;

    QJsonObject jsonObject;
    jsonObject.insert("id", QJsonValue("1"));
    jsonObject.insert("gameName", QJsonValue("testGameName"));
    jsonObject.insert("currentRoundId", QJsonValue("2"));
    jsonObject.insert("firstPlayerId", QJsonValue("3"));
    jsonObject.insert("secondPlayerId", QJsonValue("4"));
    jsonObject.insert("firstPlayerName", QJsonValue("testFirstPlayerName"));
    jsonObject.insert("secondPlayerName", QJsonValue("testSecondPlayerName"));
    jsonObject.insert("exists", QJsonValue(true));

    QSignalSpy spy(&m_CommObj, SIGNAL(refreshStatus(bool, const QString&, const QString&, const QString&, const QString&)));

    m_CommObj.processResponse(jsonObject);
    QVERIFY(1L == m_CommObj.m_GlobalData->m_GameData.m_GameId);
    QVERIFY("testGameName" == m_CommObj.m_GlobalData->m_GameData.m_GameName);
    QVERIFY(2L == m_CommObj.m_GlobalData->m_GameData.m_RoundId);
    QVERIFY(4L == m_CommObj.m_GlobalData->m_GameData.m_OtherUserId);
    QVERIFY("testSecondPlayerName" == m_CommObj.m_GlobalData->m_GameData.m_OtherUsername);
    QVERIFY(3L == m_CommObj.m_GlobalData->m_GameData.m_UserId);
    QVERIFY(3L == m_CommObj.m_GlobalData->m_UserData.m_UserId);

    QCOMPARE(spy.count(), 1);
    QList<QVariant> arguments = spy.takeFirst();
    QCOMPARE(true, arguments.at(0).toBool());
    QCOMPARE("testGameName", arguments.at(1).toString());
    QCOMPARE("testFirstPlayerName", arguments.at(2).toString());
    QCOMPARE("testSecondPlayerName", arguments.at(3).toString());
    QCOMPARE("2", arguments.at(4).toString());
}

void RefreshGameStatusCommObjTest::cleanupTestCase()
{
    qDebug("RefreshGameStatusCommObjTest ends ..");

}

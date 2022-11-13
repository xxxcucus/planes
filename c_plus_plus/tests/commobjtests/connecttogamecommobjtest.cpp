#include "connecttogamecommobjtest.h"
#include "viewmodels/gameviewmodel.h"
#include <QTest>
#include <QSignalSpy>


void ConnectToGameCommObjTest::initTestCase()
{
    qDebug("ConnectToGameCommObjTest starts ..");
}

void ConnectToGameCommObjTest::SinglePlayerTest()
{
    m_CommObj.m_IsSinglePlayer = true;
    QVERIFY2(m_CommObj.makeRequest("testName") == false, "ConnectToGameCommObj should abort if single player game");
}

void ConnectToGameCommObjTest::NoUserLoggedInTest()
{
    m_CommObj.m_IsSinglePlayer = false;
    m_CommObj.m_ParentWidget = nullptr;
    GlobalData* gd = new GlobalData();
    gd->m_UserData.m_UserName = "";
    m_CommObj.m_GlobalData = gd;
    QVERIFY2(m_CommObj.makeRequest("testName") == false, "Cannot cancel round without being logged in");
}

void ConnectToGameCommObjTest::PrepareViewModelTest()
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

void ConnectToGameCommObjTest::ProcessResponseTest() {
    GlobalData* gd = new GlobalData();
    gd->m_UserData.m_UserName = "testUserName";
    gd->m_GameData.m_GameName = "testGameName1";
    m_CommObj.m_GlobalData = gd;
    m_CommObj.m_GameName = "testGameName1";

    QJsonObject jsonObject;
    jsonObject.insert("id", QJsonValue("1"));
    jsonObject.insert("gameName", QJsonValue("testGameName"));
    jsonObject.insert("currentRoundId", QJsonValue("2"));
    jsonObject.insert("firstPlayerId", QJsonValue("3"));
    jsonObject.insert("secondPlayerId", QJsonValue("4"));
    jsonObject.insert("firstPlayerName", QJsonValue("testFirstPlayerName"));

    QSignalSpy spy(&m_CommObj, SIGNAL(gameConnectedTo(const QString&, const QString&, const QString&, const QString&, bool)));

    m_CommObj.processResponse(jsonObject);
    QVERIFY(1L == m_CommObj.m_GlobalData->m_GameData.m_GameId);
    QVERIFY("testGameName" == m_CommObj.m_GlobalData->m_GameData.m_GameName);
    QVERIFY(2L == m_CommObj.m_GlobalData->m_GameData.m_RoundId);
    QVERIFY(3L == m_CommObj.m_GlobalData->m_GameData.m_OtherUserId);
    QVERIFY("testFirstPlayerName" == m_CommObj.m_GlobalData->m_GameData.m_OtherUsername);
    QVERIFY(4L == m_CommObj.m_GlobalData->m_GameData.m_UserId);
    QVERIFY(4L == m_CommObj.m_GlobalData->m_UserData.m_UserId);

    QCOMPARE(spy.count(), 1);
    QList<QVariant> arguments = spy.takeFirst(); 
    QCOMPARE("testGameName1", arguments.at(0).toString());
    QCOMPARE("testFirstPlayerName", arguments.at(1).toString());
    QCOMPARE("testUserName", arguments.at(2).toString());
    QCOMPARE("2", arguments.at(3).toString());
    QCOMPARE(true, arguments.at(4).toBool());
}

void ConnectToGameCommObjTest::cleanupTestCase()
{
    qDebug("ConnectToGameCommObjTest ends ..");

}

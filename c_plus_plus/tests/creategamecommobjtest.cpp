#include "creategamecommobjtest.h"
#include "viewmodels/gameviewmodel.h"
#include <QTest>


void CreateGameCommObjTest::initTestCase()
{
    qDebug("CreateGameCommObjTestTest starts ..");
}

void CreateGameCommObjTest::SinglePlayerTest()
{
    m_CommObj.m_IsSinglePlayer = true;
    QVERIFY2(m_CommObj.makeRequest("testName") == false, "CreateGameCommObjTest should abort if single player game");
}

void CreateGameCommObjTest::NoUserLoggedInTest()
{
    m_CommObj.m_IsSinglePlayer = false;
    m_CommObj.m_ParentWidget = nullptr;
    GlobalData* gd = new GlobalData();
    gd->m_UserData.m_UserName = "";
    m_CommObj.m_GlobalData = gd;
    QVERIFY2(m_CommObj.makeRequest("testName") == false, "Cannot cancel round without being logged in");
}

void CreateGameCommObjTest::PrepareViewModelTest()
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

void CreateGameCommObjTest::ProcessResponseTest() {
    GlobalData* gd = new GlobalData();
    m_CommObj.m_GlobalData = gd;

    QJsonObject jsonObject;
    jsonObject.insert("id", QJsonValue("1"));
    jsonObject.insert("gameName", QJsonValue("testGameName"));
    jsonObject.insert("currentRoundId", QJsonValue("2"));
    jsonObject.insert("firstPlayerId", QJsonValue("3"));
    jsonObject.insert("secondPlayerId", QJsonValue("4"));
    jsonObject.insert("firstPlayerName", QJsonValue("testFirstPlayerName"));

    m_CommObj.processResponse(jsonObject);
    QVERIFY(1L == m_CommObj.m_GlobalData->m_GameData.m_GameId);
    QVERIFY("testGameName" == m_CommObj.m_GlobalData->m_GameData.m_GameName);
    QVERIFY(0L == m_CommObj.m_GlobalData->m_GameData.m_RoundId);
    QVERIFY(0L == m_CommObj.m_GlobalData->m_GameData.m_OtherUserId);
    QVERIFY(0L == m_CommObj.m_GlobalData->m_GameData.m_UserId);
    QVERIFY(4L == m_CommObj.m_GlobalData->m_UserData.m_UserId);
}

void CreateGameCommObjTest::cleanupTestCase()
{
    qDebug("CreateGameCommObjTest ends ..");

}

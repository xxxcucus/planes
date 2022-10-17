#include "logincommobjtest.h"

#include "viewmodels/loginviewmodel.h"
#include <QTest>
#include <QPair>


void LoginCommObjTest::initTestCase()
{
    qDebug("CreateGameCommObjTestTest starts ..");
}

void LoginCommObjTest::SinglePlayerTest()
{
    m_CommObj.m_IsSinglePlayer = true;
    QVERIFY2(m_CommObj.makeRequest("testUsername", "testPassword") == false, "LoginCommObjTest should abort if single player game");
}

void LoginCommObjTest::PrepareViewModelTest()
{
    LoginViewModel viewModel = m_CommObj.prepareViewModel("testUsername", "testPassword");
    QVERIFY2(viewModel.m_UserName == "testUsername", "Username was not set");
    QVERIFY2(viewModel.m_Password == "testPassword", "Password was not set");
}

void LoginCommObjTest::VerifyHeadersTest_Successfull() {

    QByteArray hdrName = QString("Authorization").toUtf8();
    QByteArray token = QString("TestToken").toUtf8();
    QPair<QByteArray, QByteArray> headerPair;
    headerPair.first = hdrName;
    headerPair.second = token;
    QList<QNetworkReply::RawHeaderPair> hdrList({ headerPair });

    GlobalData* gd = new GlobalData();
    m_CommObj.m_GlobalData = gd;

    bool successfull = m_CommObj.searchAuthorizationInHeaders(hdrList);
    QVERIFY2(successfull == true, "Authorization not found in header");
    QVERIFY2(m_CommObj.m_GlobalData->m_UserData.m_AuthToken == "TestToken", "Authorization token was not saved");
}

void LoginCommObjTest::VerifyHeadersTest_Error() {
    QByteArray hdrName = QString("Authorition").toUtf8();
    QByteArray token = QString("TestToken").toUtf8();
    QPair<QByteArray, QByteArray> headerPair;
    headerPair.first = hdrName;
    headerPair.second = token;
    QList<QNetworkReply::RawHeaderPair> hdrList({ headerPair });

    GlobalData* gd = new GlobalData();
    m_CommObj.m_GlobalData = gd;

    bool successfull = m_CommObj.searchAuthorizationInHeaders(hdrList);
    QVERIFY2(successfull == false, "Authorization found in header");
}

void LoginCommObjTest::ProcessResponseTest() {
    GlobalData* gd = new GlobalData();
    m_CommObj.m_GlobalData = gd;
    m_CommObj.m_GlobalData->m_GameData.m_GameId = 1L;
    m_CommObj.m_GlobalData->m_GameData.m_RoundId = 1L;
    m_CommObj.m_GlobalData->m_GameData.m_UserId = 1L;
    m_CommObj.m_GlobalData->m_GameData.m_OtherUserId = 1L;
    m_CommObj.m_GlobalData->m_GameData.m_GameName = "testGameName";
    m_CommObj.m_GlobalData->m_GameData.m_OtherUsername = "testUserName";


    QJsonObject jsonObject;
    jsonObject.insert("id", QJsonValue("1"));
    jsonObject.insert("username", QJsonValue("testUsername"));


    m_CommObj.processResponse(true, jsonObject);
    QVERIFY(0L == m_CommObj.m_GlobalData->m_GameData.m_GameId);
    QVERIFY(0L == m_CommObj.m_GlobalData->m_GameData.m_RoundId);
    QVERIFY(0L == m_CommObj.m_GlobalData->m_GameData.m_UserId);
    QVERIFY(0L == m_CommObj.m_GlobalData->m_GameData.m_OtherUserId);
    QVERIFY("" == m_CommObj.m_GlobalData->m_GameData.m_GameName);
    QVERIFY("" == m_CommObj.m_GlobalData->m_GameData.m_OtherUsername);
    QVERIFY("" == m_CommObj.m_GlobalData->m_UserData.m_UserPassword);
    QVERIFY(1L == m_CommObj.m_GlobalData->m_UserData.m_UserId);
    QVERIFY("testUsername" == m_CommObj.m_GlobalData->m_UserData.m_UserName);
}

void LoginCommObjTest::cleanupTestCase()
{
    qDebug("CreateGameCommObjTest ends ..");

}

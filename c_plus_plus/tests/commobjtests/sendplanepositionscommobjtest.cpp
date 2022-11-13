#include "sendplanepositionscommobjtest.h"
#include "viewmodels/gameviewmodel.h"
#include <QTest>
#include <QSignalSpy>


void SendPlanePositionsCommObjTest::initTestCase()
{
    qDebug("SendPlanePositionsCommObjTest starts ..");
}

void SendPlanePositionsCommObjTest::SinglePlayerTest()
{
    m_CommObj.m_IsSinglePlayer = true;
    QVERIFY2(m_CommObj.makeRequest() == false, "SendPlanePositionsCommObj should abort if single player game");
}

void SendPlanePositionsCommObjTest::NoUserLoggedInTest()
{
    m_CommObj.m_IsSinglePlayer = false;
    m_CommObj.m_ParentWidget = nullptr;
    GlobalData* gd = new GlobalData();
    gd->m_UserData.m_UserName = "";
    m_CommObj.m_GlobalData = gd;
    QVERIFY2(m_CommObj.makeRequest() == false, "Cannot send plane positions without being logged in");
}

void SendPlanePositionsCommObjTest::PrepareViewModelTest()
{   
    GlobalData* gd = new GlobalData();
    gd->m_GameData.m_GameId = 1L;
    gd->m_GameData.m_RoundId = 2L;
    gd->m_UserData.m_UserId = 3L;
    gd->m_GameData.m_OtherUserId = 4L;
    m_CommObj.m_GlobalData = gd;

    MultiplayerRoundMock* multiRoundMock = new MultiplayerRoundMock();
    m_CommObj.m_MultiRound = multiRoundMock;

    PlanesPositionsViewModel viewModel = m_CommObj.prepareViewModel();
    
    QVERIFY2(viewModel.m_GameId == 1L, "GameId was not set");
    QVERIFY2(viewModel.m_RoundId == 2L, "RoundId was not set");
    QVERIFY2(viewModel.m_OwnUserId == 3L, "USerId was not set");
    QVERIFY2(viewModel.m_OpponentUserId == 4L, "OpponentUserId was not set");

    QVERIFY2(viewModel.m_Plane1X == 2, "Plane1X was not set");
    QVERIFY2(viewModel.m_Plane1Y == 1, "Plane1Y was not set");
    QVERIFY2(viewModel.m_Plane1Orient == Plane::Orientation::NorthSouth, "Plane1Orient was not set");

    QVERIFY2(viewModel.m_Plane2X == 2, "Plane2X was not set");
    QVERIFY2(viewModel.m_Plane2Y == 1, "Plane2Y was not set");
    QVERIFY2(viewModel.m_Plane2Orient == Plane::Orientation::NorthSouth, "Plane2Orient was not set");

    QVERIFY2(viewModel.m_Plane3X == 2, "Plane3X was not set");
    QVERIFY2(viewModel.m_Plane3Y == 1, "Plane3Y was not set");
    QVERIFY2(viewModel.m_Plane3Orient == Plane::Orientation::NorthSouth, "Plane3Orient was not set");
}

void SendPlanePositionsCommObjTest::ProcessResponseTestOtherExistTrue() {

    QJsonObject jsonObject;
    jsonObject.insert("otherExist", QJsonValue(true));
    jsonObject.insert("plane1_x", QJsonValue(1));
    jsonObject.insert("plane1_y", QJsonValue(2));
    jsonObject.insert("plane1_orient", QJsonValue(0));
    jsonObject.insert("plane2_x", QJsonValue(3));
    jsonObject.insert("plane2_y", QJsonValue(4));
    jsonObject.insert("plane2_orient", QJsonValue(1));
    jsonObject.insert("plane3_x", QJsonValue(5));
    jsonObject.insert("plane3_y", QJsonValue(6));
    jsonObject.insert("plane3_orient", QJsonValue(2));

    MultiplayerRoundMock* multiRoundMock = new MultiplayerRoundMock();
    m_CommObj.m_MultiRound = multiRoundMock;

    QSignalSpy spy(&m_CommObj, SIGNAL(opponentPlanePositionsReceived()));
    m_CommObj.processResponse(jsonObject);
    QCOMPARE(spy.count(), 1);
}

void SendPlanePositionsCommObjTest::ProcessResponseTestOtherExistFalse() {
    QJsonObject jsonObject;
    jsonObject.insert("otherExist", QJsonValue(false));

    MultiplayerRoundMock* multiRoundMock = new MultiplayerRoundMock();
    m_CommObj.m_MultiRound = multiRoundMock;

    QSignalSpy spy(&m_CommObj, SIGNAL(waitForOpponentPlanePositions()));
    m_CommObj.processResponse(jsonObject);
    QCOMPARE(spy.count(), 1);
}

void SendPlanePositionsCommObjTest::cleanupTestCase()
{
    qDebug("SendPlanePositionsCommObjTest ends ..");
}

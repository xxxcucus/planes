#include "sendmovecommobjtest.h"
#include "viewmodels/unsentmovesviewmodel.h"
#include "guesspoint.h"
#include <QTest>
#include <QSignalSpy>


void SendMoveCommObjTest::initTestCase()
{
    qDebug("SendMoveCommObjTest starts ..");
}

void SendMoveCommObjTest::SinglePlayerTest()
{
    m_CommObj.m_IsSinglePlayer = true;
    QVERIFY2(m_CommObj.makeRequest({}, {}, {}) == false, "SendMoveCommObj should abort if single player game");
}

void SendMoveCommObjTest::NoUserLoggedInTest()
{
    m_CommObj.m_IsSinglePlayer = false;
    m_CommObj.m_ParentWidget = nullptr;
    GlobalData* gd = new GlobalData();
    gd->m_UserData.m_UserName = "";
    m_CommObj.m_GlobalData = gd;
    QVERIFY2(m_CommObj.makeRequest({}, {}, {}) == false, "Cannot send move without being logged in");
}

void SendMoveCommObjTest::PrepareViewModelTest()
{   
    GlobalData* gd = new GlobalData();
    gd->m_GameData.m_GameId = 1L;
    gd->m_GameData.m_RoundId = 2L;
    gd->m_GameData.m_UserId = 3L;
    gd->m_GameData.m_OtherUserId = 4L;
    m_CommObj.m_GlobalData = gd;
    UnsentMovesViewModel viewModel = m_CommObj.prepareViewModel(4, { 1 }, { 1 }, { GuessPoint(10, 11)});

    QVERIFY2(viewModel.m_GameId == 1L, "GameId was not set");
    QVERIFY2(viewModel.m_RoundId == 2L, "RoundId was not set");
    QVERIFY2(viewModel.m_OwnUserId == 3L, "USerId was not set");
    QVERIFY2(viewModel.m_OpponentUserId == 4L, "OpponentUserId was not set");
    QVERIFY2(viewModel.m_OpponentMoveIndex == 4, "OpponentMoveIndex was not set");
    QVERIFY2(viewModel.m_NotReceivedMoveIndex[0] == 1, "NotReceivedMoveIndex was not set");
    QVERIFY2(viewModel.m_NotSentMovesIndex[0].m_MoveX == 10, "NotSentMoveIndex was not set");
    QVERIFY2(viewModel.m_NotSentMovesIndex[0].m_MoveY == 11, "NotSentMoveIndex was not set");
}

void SendMoveCommObjTest::ProcessResponseTest() {
    GlobalData* gd = new GlobalData();
    m_CommObj.m_GlobalData = gd;

    MultiplayerRoundMock* multiRoundMock = new MultiplayerRoundMock();
    m_CommObj.m_MultiRound = multiRoundMock;

    QJsonObject jsonMove;
    jsonMove.insert("moveX", QJsonValue(1));
    jsonMove.insert("moveY", QJsonValue(2));
    jsonMove.insert("moveIndex", QJsonValue(3));
    QJsonArray jsonMoveArray;
    jsonMoveArray.push_back(jsonMove);

    QJsonObject jsonObject;
    jsonObject.insert("listMoves", jsonMoveArray);


    qRegisterMetaType<GuessPoint>();
    QSignalSpy spy(&m_CommObj, SIGNAL(opponentMoveGenerated(GuessPoint)));
    m_CommObj.processResponse(jsonObject);

    QCOMPARE(spy.count(), 1);
    GuessPoint gp = qvariant_cast<GuessPoint>(spy.at(0).at(0));
    QVERIFY2(gp.m_row == 1, "Row was not read");
    QVERIFY2(gp.m_col == 2, "Col was not read");
}

void SendMoveCommObjTest::cleanupTestCase()
{
    qDebug("SendMoveCommObjTest ends ..");

}

#ifndef __SEND_MOVE_COMMOBJ_TEST__
#define __SEND_MOVE_COMMOBJ_TEST__

#include <QObject>
#include <QTest>
#include "sendmovecommobj.h"
#include "multiplayerround.h"

class SendMoveCommObjTest : public QObject {
    Q_OBJECT

private:
    class MultiplayerRoundMock : public MultiplayerRound {
        void addOpponentMove(GuessPoint& gp, int moveIndex) override {
            QVERIFY2(moveIndex == 3, "MoveIndex was not read");
        }
    };

private:
    SendMoveCommObj m_CommObj;

private slots:
    void initTestCase();
    void SinglePlayerTest();
    void NoUserLoggedInTest();
    void PrepareViewModelTest();
    void ProcessResponseTest();
    void cleanupTestCase();
};

#endif
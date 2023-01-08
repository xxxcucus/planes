#ifndef __REQUEST_OPPONENT_MOVES_COMMOBJ_TEST__
#define __REQUEST_OPPONENT_MOVES_COMMOBJ_TEST__

#include <QObject>
#include <QTest>
#include "requestopponentmovescommobj.h"
#include "multiplayerround.h"

class RequestOpponentMovesCommObjTest : public QObject {
    Q_OBJECT

private:
    class MultiplayerRoundMock : public MultiplayerRound {
        void addOpponentMove(GuessPoint& gp, int moveIndex) override {
            QVERIFY2(moveIndex == 3, "MoveIndex was not read");
        }
    };

private:
    RequestOpponentMovesCommObj m_CommObj;

private slots:
    void initTestCase();
    void SinglePlayerTest();
    void NoUserLoggedInTest();
    void PrepareViewModelTest();
    void ProcessResponseTest();
    void cleanupTestCase();
};

#endif
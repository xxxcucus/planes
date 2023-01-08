#ifndef __START_NEW_ROUND_COMMOBJ_TEST__
#define __START_NEW_ROUND_COMMOBJ_TEST__

#include <QObject>
#include <QTest>
#include "startnewroundcommobj.h"
#include "multiplayerround.h"

class StartNewRoundCommObjTest : public QObject {
    Q_OBJECT

private:
    class MultiplayerRoundMock : public MultiplayerRound {
        void initRound() override {
        }
    };

private:
    StartNewRoundCommObj m_CommObj;

private slots:
    void initTestCase();
    void SinglePlayerTest();
    void NoUserLoggedInTest();
    void PrepareViewModelTest();
    void ProcessResponseTest();
    void cleanupTestCase();
};

#endif
#ifndef __CANCEL_ROUND_COMMOBJ_TEST__
#define __CANCEL_ROUND_COMMOBJ_TEST__


#include <QObject>
#include <QTest>
#include "cancelroundcommobj.h"
#include "multiplayerround.h"

class CancelRoundCommObjTest : public QObject {
    Q_OBJECT

private:
    class MultiplayerRoundMock : public MultiplayerRound {
        public:
            void setRoundCancelled() override {
                m_CallCount++;
            }

            int m_CallCount = 0;
    };

private:
    CancelRoundCommObj m_CommObj;

private slots:
    void initTestCase();
    void SinglePlayerTest();
    void NoUserLoggedInTest();
    void PrepareViewModelTest();
    void ProcessResponseTest();
    void cleanupTestCase();
};

#endif
#ifndef __SEND_WINNER_COMMOBJ_TEST__
#define __SEND_WINNER_COMMOBJ_TEST__

#include <QObject>
#include <QTest>
#include "sendwinnercommobj.h"

class SendWinnerCommObjTest : public QObject {
    Q_OBJECT
private:
    SendWinnerCommObj m_CommObj;

private slots:
    void initTestCase();
    void SinglePlayerTest();
    void NoUserLoggedInTest();
    void PrepareViewModelTest();
    void cleanupTestCase();
};

#endif
#ifndef __REFRESH_GAME_STATUS_COMMOBJ_TEST__
#define __REFRESH_GAME_STATUS_COMMOBJ_TEST__

#include <QObject>
#include <QTest>
#include "refreshgamestatuscommobj.h"

class RefreshGameStatusCommObjTest : public QObject {
    Q_OBJECT
private:
    RefreshGameStatusCommObj m_CommObj;

private slots:
    void initTestCase();
    void SinglePlayerTest();
    void NoUserLoggedInTest();
    void PrepareViewModelTest();
    void ProcessResponseTest();
    void cleanupTestCase();
};

#endif
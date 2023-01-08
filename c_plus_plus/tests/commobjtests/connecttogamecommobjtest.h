#ifndef __CONNECT_TO_GAME_COMMOBJ_TEST__
#define __CONNECT_TO_GAME_COMMOBJ_TEST__

#include <QObject>
#include <QTest>
#include "connecttogamecommobj.h"

class ConnectToGameCommObjTest : public QObject {
    Q_OBJECT
private:
    ConnectToGameCommObj m_CommObj;

private slots:
    void initTestCase();
    void SinglePlayerTest();
    void NoUserLoggedInTest();
    void PrepareViewModelTest();
    void ProcessResponseTest();
    void cleanupTestCase();
};

#endif
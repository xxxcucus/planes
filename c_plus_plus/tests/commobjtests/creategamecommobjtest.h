#ifndef __CREATE_GAME_COMMOBJ_TEST__
#define __CREATE_GAME_COMMOBJ_TEST__


#include <QObject>
#include <QTest>
#include "creategamecommobj.h"

class CreateGameCommObjTest : public QObject {
    Q_OBJECT
private:
    CreateGameCommObj m_CommObj;

private slots:
    void initTestCase();
    void SinglePlayerTest();
    void NoUserLoggedInTest();
    void PrepareViewModelTest();
	void ProcessResponseTest();
    void cleanupTestCase();
};

#endif
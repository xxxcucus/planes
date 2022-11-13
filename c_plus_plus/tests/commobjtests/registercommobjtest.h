#ifndef __REGISTER_COMMOBJ_TEST__
#define __REGISTER_COMMOBJ_TEST__

#include <QObject>
#include <QTest>
#include "registercommobj.h"

class RegisterCommObjTest : public QObject {
    Q_OBJECT
private:
    RegisterCommObj m_CommObj;

private slots:
    void initTestCase();
    void SinglePlayerTest();
    void PrepareViewModelTest();
    void ProcessResponseTest();
    void cleanupTestCase();
};

#endif
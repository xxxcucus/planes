#ifndef __NOROBOT_COMMOBJ_TEST__
#define __NOROBOT_COMMOBJ_TEST__

#include <QObject>
#include <QTest>
#include "norobotcommobj.h"

class NoRobotCommObjTest : public QObject {
    Q_OBJECT
private:
    NoRobotCommObj m_CommObj;

private slots:
    void initTestCase();
    void SinglePlayerTest();
    void PrepareViewModelTest();
    void ProcessResponseTest();
    void cleanupTestCase();
};

#endif
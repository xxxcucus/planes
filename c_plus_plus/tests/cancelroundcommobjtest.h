#include <QObject>
#include <QTest>
#include "cancelroundcommobj.h"

class CancelRoundCommObjTest : public QObject {
    Q_OBJECT
private:
    CancelRoundCommObj m_CommObj;

private slots:
    void initTestCase();
    void SinglePlayerTest();
    void NoUserLoggedInTest();
    void cleanupTestCase();
};

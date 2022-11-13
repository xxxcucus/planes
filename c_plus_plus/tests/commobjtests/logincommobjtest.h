#ifndef __LOGIN_COMMOBJ_TEST__
#define __LOGIN_COMMOBJ_TEST__


#include <QObject>
#include <QTest>
#include <QNetworkReply>
#include "logincommobj.h"

class LoginCommObjTest : public QObject {
    Q_OBJECT
private:
    LoginCommObj m_CommObj;

private slots:
    void initTestCase();
    void SinglePlayerTest();
    void PrepareViewModelTest();
	void VerifyHeadersTest_Successfull();
    void VerifyHeadersTest_Error();
	void ProcessResponseTest();
    void cleanupTestCase();
};

#endif
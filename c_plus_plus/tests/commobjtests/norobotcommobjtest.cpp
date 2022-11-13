#include "norobotcommobjtest.h"
#include "viewmodels/norobotviewmodel.h"
#include <QTest>
#include <QSignalSpy>


void NoRobotCommObjTest::initTestCase()
{
    qDebug("NoRobotCommObjTest starts ..");
}

void NoRobotCommObjTest::SinglePlayerTest()
{
    m_CommObj.m_IsSinglePlayer = true;
    QVERIFY2(m_CommObj.makeRequest("1", "111111111") == false, "NoRobotCommObj should abort if single player game");
}

void NoRobotCommObjTest::PrepareViewModelTest()
{   
    NoRobotViewModel viewModel = m_CommObj.prepareViewModel("1", "111111111");
    
    QVERIFY2(viewModel.m_requestId == "1", "RequestId was not saved in the viewmodel");
    QVERIFY2(viewModel.m_answer == "111111111", "Answer was not saved in the viewmodel");
}

void NoRobotCommObjTest::ProcessResponseTest() {
    QSignalSpy spy(&m_CommObj, SIGNAL(registrationComplete()));
    QJsonObject jsonObject;
    m_CommObj.processResponse(jsonObject);
    QCOMPARE(spy.count(), 1);
}


void NoRobotCommObjTest::cleanupTestCase()
{
    qDebug("NoRobotCommObjTest ends ..");
}

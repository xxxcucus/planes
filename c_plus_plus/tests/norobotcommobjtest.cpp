#include "norobotcommobjtest.h"
#include "viewmodels/norobotviewmodel.h"
#include <QTest>


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

void NoRobotCommObjTest::cleanupTestCase()
{
    qDebug("NoRobotCommObjTest ends ..");
}

#include "registercommobjtest.h"
#include "viewmodels/gameviewmodel.h"
#include <QTest>
#include <QSignalSpy>


void RegisterCommObjTest::initTestCase()
{
    qDebug("RegisterCommObjTest starts ..");
}

void RegisterCommObjTest::SinglePlayerTest()
{
    m_CommObj.m_IsSinglePlayer = true;
    QVERIFY2(m_CommObj.makeRequest("testName", "testPassword") == false, "RegisterCommObj should abort if single player game");
}

void RegisterCommObjTest::PrepareViewModelTest()
{   
    LoginViewModel viewModel = m_CommObj.prepareViewModel("testUserName", "testPassword");
    
    QVERIFY2(viewModel.m_UserName == "testUserName", "Username not set");
    QVERIFY2(viewModel.m_Password == "testPassword", "Password not set");
}

void RegisterCommObjTest::ProcessResponseTest() {
    GlobalData* gd = new GlobalData();
    m_CommObj.m_GlobalData = gd;
    m_CommObj.m_GlobalData->m_UserData.m_UserId = 3L;

    QJsonObject jsonObject;
    jsonObject.insert("image_id_1", QJsonValue("test1"));
    jsonObject.insert("image_id_2", QJsonValue("test2"));
    jsonObject.insert("image_id_3", QJsonValue("test3"));
    jsonObject.insert("image_id_4", QJsonValue("test4"));
    jsonObject.insert("image_id_5", QJsonValue("test5"));
    jsonObject.insert("image_id_6", QJsonValue("test6"));
    jsonObject.insert("image_id_7", QJsonValue("test7"));
    jsonObject.insert("image_id_8", QJsonValue("test8"));
    jsonObject.insert("image_id_9", QJsonValue("test9"));
    jsonObject.insert("question", QJsonValue("111000111"));
    jsonObject.insert("id", QJsonValue("1"));

    QSignalSpy spy(&m_CommObj, SIGNAL(noRobotRegistration(const std::vector<QString>&, const QString&, long int)));

    m_CommObj.processResponse(jsonObject);
    QVERIFY(m_CommObj.m_Images[0] == "test1");
    QVERIFY(m_CommObj.m_Images[1] == "test2");
    QVERIFY(m_CommObj.m_Images[2] == "test3");
    QVERIFY(m_CommObj.m_Images[3] == "test4");
    QVERIFY(m_CommObj.m_Images[4] == "test5");
    QVERIFY(m_CommObj.m_Images[5] == "test6");
    QVERIFY(m_CommObj.m_Images[6] == "test7");
    QVERIFY(m_CommObj.m_Images[7] == "test8");
    QVERIFY(m_CommObj.m_Images[8] == "test9");

    QCOMPARE(spy.count(), 1);
    QList<QVariant> arguments = spy.takeFirst();
    QCOMPARE("111000111", arguments.at(1).toString());
    QCOMPARE(1, arguments.at(2).toInt());

}

void RegisterCommObjTest::cleanupTestCase()
{
    qDebug("RegisterCommObjTest ends ..");

}

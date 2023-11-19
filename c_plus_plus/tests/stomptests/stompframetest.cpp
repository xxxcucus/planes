#include "stompframetest.h"

#include <QTest>
#include "stompframe.h"

void StompFrameTest::initTestCase()
{
    qDebug("StompFrameTest starts ..");
}

void StompFrameTest::setCommandTest() {
    StompFrame frame;

    std::vector<StompFrame::HeaderTypes> testValues = {StompFrame::HeaderTypes::CONNECT, StompFrame::HeaderTypes::STOMP, StompFrame::HeaderTypes::CONNECTED, StompFrame::HeaderTypes::SEND, StompFrame::HeaderTypes::SUBSCRIBE, StompFrame::HeaderTypes::UNSUBSCRIBE, StompFrame::StompFrame::HeaderTypes::ACK, StompFrame::HeaderTypes::NACK, StompFrame::HeaderTypes::BEGIN, StompFrame::HeaderTypes::COMMIT, StompFrame::HeaderTypes::ABORT, StompFrame::HeaderTypes::DISCONNECT, StompFrame::HeaderTypes::MESSAGE, StompFrame::HeaderTypes::RECEIPT, StompFrame::HeaderTypes::ERROR};

    for (StompFrame::HeaderTypes comm : testValues) {
        frame.setCommand(comm);
        QString errorMsg = QString("Test failed for ") + frame.convertCommandToQString(comm);
        QVERIFY2(frame.getCommand() == comm, errorMsg.toUtf8().data());
    }
}

void StompFrameTest::escapeSpecialSymbolsTestColon() {
    StompFrame frame;
    QByteArray ba = frame.escapeSpecialSymbols("test:test");
    QString errorMsg = QString("Encoding of : failed ") + QString(ba);
    QVERIFY2(QString(ba) == "test\\ctest", errorMsg.toUtf8().data());
}

void StompFrameTest::escapeSpecialSymbolsTestSlash() {
    StompFrame frame;
    QByteArray ba = frame.escapeSpecialSymbols("test\\test");
    QString errorMsg = QString("Encoding of \\ failed ") + QString(ba);
    QVERIFY2(QString(ba) == "test\\\\test", errorMsg.toUtf8().data());
}

void StompFrameTest::escapeSpecialSymbolsTestCarriageReturn() {
    StompFrame frame;
    QByteArray ba = frame.escapeSpecialSymbols("test\rtest");
    QString errorMsg = QString("Encoding of \\r failed ") + QString(ba);
    QVERIFY2(QString(ba) == "test\\rtest", errorMsg.toUtf8().data());
}

void StompFrameTest::escapeSpecialSymbolsTestLineFeed() {
    StompFrame frame;
    QByteArray ba = frame.escapeSpecialSymbols("test\ntest");
    QString errorMsg = QString("Encoding of \\n failed ") + QString(ba);
    QVERIFY2(QString(ba) == "test\\ntest", errorMsg.toUtf8().data());
}

void StompFrameTest::escapeSpecialSymbolsTestWorks() {
    StompFrame frame;
    QByteArray ba = frame.escapeSpecialSymbols("testtest");
    QString errorMsg = QString("Encoding of 'testtest' failed ") + QString(ba);
    QVERIFY2(QString(ba) == "testtest", errorMsg.toUtf8().data());
}

void StompFrameTest::unescapeSpecialSymbolsTestColon() {
    StompFrame frame;
    auto res = frame.unescapeSpecialSymbols("test\\ctest");
    QVERIFY2(res.first, "Decoding of : failed");
    QString errorMsg = QString("Decoding of : failed ") + QString(res.second);
    QVERIFY2(QString(res.second) == "test:test", errorMsg.toUtf8().data());
}

void StompFrameTest::unescapeSpecialSymbolsTestSlash() {
    StompFrame frame;
    auto res = frame.unescapeSpecialSymbols("test\\\\test");
    QVERIFY2(res.first, "Decoding of \\ failed ");
    QString errorMsg = QString("Decoding of \\ failed ") + QString(res.second);
    QVERIFY2(QString(res.second) == "test\\test", errorMsg.toUtf8().data());
}

void StompFrameTest::unescapeSpecialSymbolsTestCarriageReturn() {
    StompFrame frame;
    auto res = frame.unescapeSpecialSymbols("test\\rtest");
    QVERIFY2(res.first, "Decoding of \\r failed ");
    QString errorMsg = QString("Decoding of \\r failed ") + QString(res.second);
    QVERIFY2(QString(res.second) == "test\rtest", errorMsg.toUtf8().data());
}

void StompFrameTest::unescapeSpecialSymbolsTestLineFeed() {
    StompFrame frame;
    auto res = frame.unescapeSpecialSymbols("test\\ntest");
    QVERIFY2(res.first, "Decoding of \\n failed");
    QString errorMsg = QString("Decoding of \\n failed ") + QString(res.second);
    QVERIFY2(QString(res.second) == "test\ntest", errorMsg.toUtf8().data());
}

void StompFrameTest::unescapeSpecialSymbolsTestWorks() {
    StompFrame frame;
    auto res = frame.unescapeSpecialSymbols("testtest");
    QVERIFY2(res.first, "Decoding of testtest failed");
    QString errorMsg = QString("Decoding of test failed ") + QString(res.second);
    QVERIFY2(QString(res.second) == "testtest", errorMsg.toUtf8().data());
}

void StompFrameTest::unescapeSpecialSymbolsTestIllegalSymbols() {
    StompFrame frame;
    auto res = frame.unescapeSpecialSymbols("test\\ftest");
    QVERIFY2(!res.first, "Illegal symbol not recognized");
}

void StompFrameTest::cleanupTestCase()
{
    qDebug("StompFrameTest ends ..");
}

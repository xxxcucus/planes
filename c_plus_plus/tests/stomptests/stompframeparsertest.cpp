#include "stompframeparsertest.h"
#include <QTest>
#include "stompframeparser.h"

void StompFrameParserTest::initTestCase() {
    qDebug("StompFrameParserTest starts ..");
}

void StompFrameParserTest::parseBodyDelimiterFromMessageDoesNotExist() {
    StompFrameParser parser;
    int pos = 0;
    QString delim;
    bool error = false;

    std::tie(pos, delim, error) = parser.findBodyDelimiterWithRegex("abcdefghtifs\\n\n");
    QVERIFY2(-1 == pos, "Found position is not correct");
    QVERIFY2(delim.isEmpty(), "Found delimiter is not correct");
    QVERIFY2(error == true, "An error is not found");
}

void StompFrameParserTest::parseBodyDelimiterFromMessageLongExists() {
    StompFrameParser parser;
    int pos = 0;
    QString delim;
    bool error = false;

    std::tie(pos, delim, error) = parser.findBodyDelimiterWithRegex("abcdefghtifs\r\n\r\n");
    QVERIFY2(12 == pos, "Found position is not correct");
    QVERIFY2("\r\n\r\n" == delim, "Found delimiter is not correct");
    QVERIFY2(error == false, "An error was found");
}

void StompFrameParserTest::parseBodyDelimiterFromMessageExists() {
    StompFrameParser parser;
    int pos = 0;
    QString delim;
    bool error = false;

    std::tie(pos, delim, error) = parser.findBodyDelimiterWithRegex("ab\\pcdefg\n\npkfhd");
    QVERIFY2(9 == pos, "Found position is not correct");
    QVERIFY2("\n\n" == delim, "Found delimiter is not correct");
    QVERIFY2(error == false, "An error was found");
}

void StompFrameParserTest::parseBodyDelimiterFromMessageDoubleExist() {
    StompFrameParser parser;
    int pos = 0;
    QString delim;
    bool error = false;

    std::tie(pos, delim, error) = parser.findBodyDelimiterWithRegex("ab\\pcdefg\r\n\r\n\npkfhd");
    QVERIFY2(9 == pos, "Found position is not correct");
    QVERIFY2("\r\n\r\n" == delim, "Found delimiter is not correct");
    QVERIFY2(error == false, "An error was not found");
}

void StompFrameParserTest::unescapeSpecialSymbolsTestColon() {
    StompFrameParser parser;
    auto res = parser.unescapeSpecialSymbols("test\\ctest");
    QVERIFY2(res.first, "Decoding of : failed");
    QString errorMsg = QString("Decoding of : failed ") + QString(res.second);
    QVERIFY2(QString(res.second) == "test:test", errorMsg.toUtf8().data());
}

void StompFrameParserTest::unescapeSpecialSymbolsTestSlash() {
    StompFrameParser parser;
    auto res = parser.unescapeSpecialSymbols("test\\\\test");
    QVERIFY2(res.first, "Decoding of \\ failed ");
    QString errorMsg = QString("Decoding of \\ failed ") + QString(res.second);
    QVERIFY2(QString(res.second) == "test\\test", errorMsg.toUtf8().data());
}

void StompFrameParserTest::unescapeSpecialSymbolsTestCarriageReturn() {
    StompFrameParser parser;
    auto res = parser.unescapeSpecialSymbols("test\\rtest");
    QVERIFY2(res.first, "Decoding of \\r failed ");
    QString errorMsg = QString("Decoding of \\r failed ") + QString(res.second);
    QVERIFY2(QString(res.second) == "test\rtest", errorMsg.toUtf8().data());
}

void StompFrameParserTest::unescapeSpecialSymbolsTestLineFeed() {
    StompFrameParser parser;
    auto res = parser.unescapeSpecialSymbols("test\\ntest");
    QVERIFY2(res.first, "Decoding of \\n failed");
    QString errorMsg = QString("Decoding of \\n failed ") + QString(res.second);
    QVERIFY2(QString(res.second) == "test\ntest", errorMsg.toUtf8().data());
}

void StompFrameParserTest::unescapeSpecialSymbolsTestWorks() {
    StompFrameParser parser;
    auto res = parser.unescapeSpecialSymbols("testtest");
    QVERIFY2(res.first, "Decoding of testtest failed");
    QString errorMsg = QString("Decoding of test failed ") + QString(res.second);
    QVERIFY2(QString(res.second) == "testtest", errorMsg.toUtf8().data());
}

void StompFrameParserTest::unescapeSpecialSymbolsTestIllegalSymbols() {
    StompFrameParser parser;
    auto res = parser.unescapeSpecialSymbols("test\\ftest");
    QVERIFY2(!res.first, "Illegal symbol not recognized");
}

void StompFrameParserTest::cleanupTestCase() {
    qDebug("StompFrameParserTest ends ..");
}

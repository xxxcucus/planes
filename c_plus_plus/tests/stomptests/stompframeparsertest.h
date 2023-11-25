#ifndef __STOMPFRAMEPARSER_TEST__
#define __STOMPFRAMEPARSER_TEST__

#include <QObject>

class StompFrameParserTest : public QObject {
    Q_OBJECT

private slots:
    void initTestCase();
    void parseBodyDelimiterFromMessageDoesNotExist();
    void parseBodyDelimiterFromMessageLongExists();
    void parseBodyDelimiterFromMessageExists();
    void parseBodyDelimiterFromMessageDoubleExist();
    void unescapeSpecialSymbolsTestColon();
    void unescapeSpecialSymbolsTestSlash();
    void unescapeSpecialSymbolsTestCarriageReturn();
    void unescapeSpecialSymbolsTestLineFeed();
    void unescapeSpecialSymbolsTestWorks();
    void unescapeSpecialSymbolsTestIllegalSymbols();
    void cleanupTestCase();
};

#endif

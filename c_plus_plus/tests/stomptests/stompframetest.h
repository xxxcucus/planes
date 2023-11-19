#ifndef __STOMPFRAME_TEST__
#define __STOMPFRAME_TEST__

#include <QObject>

class StompFrameTest : public QObject {
    Q_OBJECT

private slots:
    void initTestCase();
    void setCommandTest();
    void escapeSpecialSymbolsTestColon();
    void escapeSpecialSymbolsTestSlash();
    void escapeSpecialSymbolsTestCarriageReturn();
    void escapeSpecialSymbolsTestLineFeed();
    void escapeSpecialSymbolsTestWorks();
    void unescapeSpecialSymbolsTestColon();
    void unescapeSpecialSymbolsTestSlash();
    void unescapeSpecialSymbolsTestCarriageReturn();
    void unescapeSpecialSymbolsTestLineFeed();
    void unescapeSpecialSymbolsTestWorks();
    void unescapeSpecialSymbolsTestIllegalSymbols();
    void cleanupTestCase();
};

#endif

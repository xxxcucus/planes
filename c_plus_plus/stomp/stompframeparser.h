#ifndef __STOMPFRAMEPARSER_
#define __STOMPFRAMEPARSER_

#include <tuple>
#include <memory>
#include <QString>
#include "stompframe.h"

class StompFrameParser {

public:
    std::shared_ptr<StompFrame> parseFromTextMessage(const QString& message, bool& error);

private:
    std::tuple<int, QString, bool> findBodyDelimiterWithRegex(const QString& message);
    std::pair<bool, QByteArray> unescapeSpecialSymbols(const QByteArray& ba);

    friend class StompFrameParserTest;
};

#endif

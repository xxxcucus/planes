#include "stompframeparser.h"
#include <QRegularExpression>

std::shared_ptr<StompFrame> StompFrameParser::parseFromTextMessage(const QString& message, bool& error) {

    error = true;

    int posBodyDelimiter = -1;
    QString bodyDelimiter;
    bool errorParseBodyDelimiter = false;

    std::tie(posBodyDelimiter, bodyDelimiter, errorParseBodyDelimiter) = findBodyDelimiterWithRegex(message);

    if (errorParseBodyDelimiter) {
        error = false; //no body
        qDebug() << "Stomp frame parser error : No body delimiter found";
        return std::make_shared<StompFrame>();
    }

    QString header = message.left(posBodyDelimiter);
    QString body = message.last(message.size() - header.size() - bodyDelimiter.size());

    QStringList headerParts = header.split(QRegularExpression("\r?\n"));

    if (headerParts.empty()) {
        error = false; //no header
        qDebug() << "Stomp frame parser error : No header found";
        return std::make_shared<StompFrame>();
    }

    auto stompFrame = std::make_shared<StompFrame>();

    if (headerParts[0] == "UNKNOWN" || stompFrame->getCommandQStringList().indexOf(headerParts[0]) < 0) {
        error = false; //unknown command
        qDebug() << "Stomp frame parser error : Unknown command" << headerParts[0];
        return stompFrame;
    }

    bool errSetCommand = stompFrame->setCommand(headerParts[0]);

    if (!errSetCommand) {
        error = false; //unknown command
        qDebug() << "Stomp frame parser error : Unknown command" << headerParts[0];
        return stompFrame;
    }

    for (int i = 1; i < headerParts.size(); i++) {
        QString curHeader = headerParts[i];
        QStringList curHeaderKeyAndValue = curHeader.split(":");
        if (curHeaderKeyAndValue.size() != 2) {
            error = false; //falsely formatted key value pair
            qDebug() << "Stomp frame parser error : Falsely formatted header line " << curHeader;
            return stompFrame;
        }

        QByteArray unescapedKeyBA;
        QByteArray unescapedValueBA;

        bool errorUnescapeKey = true;
        bool errorUnescapeValue = true;

        std::tie(errorUnescapeKey, unescapedKeyBA) = unescapeSpecialSymbols(curHeaderKeyAndValue[0].toUtf8());
        std::tie(errorUnescapeValue, unescapedValueBA) = unescapeSpecialSymbols(curHeaderKeyAndValue[1].toUtf8());

        if (!errorUnescapeKey || !errorUnescapeValue) {
            error = false; //falsely escape key or value
            qDebug() << "Stomp frame parser error : Falsely escaped header or key " << curHeader;
            return stompFrame;
        }

        stompFrame->addHeader(unescapedKeyBA, unescapedValueBA);
    }

    if (body.isEmpty() || body.back() != QChar(0)) {
        error = false; //body empty
        qDebug() << "Stomp frame parser error : False formatted body " << body;
        return stompFrame;
    }

    stompFrame->addTextBody(body.left(body.size() - 1));
    error = true;

    return stompFrame;
}

std::tuple<int, QString, bool> StompFrameParser::findBodyDelimiterWithRegex(const QString& message) {

    QRegularExpression regEx("\r?\n\r?\n");
    QRegularExpressionMatch match = regEx.match(message);
    if (match.hasMatch()) {
        QString matched = match.captured(0);
        int startOffset = match.capturedStart(0);
        return std::make_tuple(startOffset, matched, false);
    } else {
        return std::make_tuple(-1, QString(), true);
    }
}

std::pair<bool, QByteArray> StompFrameParser::unescapeSpecialSymbols(const QByteArray& ba) {
    QByteArray::const_iterator it = ba.begin();
    QByteArray retVal;

    bool found92 = false;
    while (it != ba.end()) {
        if (*it == 92) {
            if (found92) {
                retVal.append(92);
                found92 = false;
            } else {
                found92 = true;
            }
        } else if (*it == 110) {
            if (found92)
                retVal.append(10);
            else
                retVal.append(110);
            found92 = false;
        } else if (*it == 114) {
            if (found92)
                retVal.append(13);
            else
                retVal.append(114);
            found92 = false;
        } else if (*it == 99) {
            if (found92)
                retVal.append(58);
            else
                retVal.append(99);
            found92 = false;
        } else {
            if (found92)
                return std::make_pair(false, QByteArray(1, 0));
            retVal.append(*it);
        }
        it++;
    }

    return std::make_pair(true, retVal);
}



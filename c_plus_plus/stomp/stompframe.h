#ifndef _STOMPFRAME_H
#define _STOMPFRAME_H

#include <QString>
#include <vector>

class StompFrame {

public:
    enum class HeaderTypes {CONNECT, STOMP, CONNECTED, SEND, SUBSCRIBE, UNSUBSCRIBE, ACK, NACK, BEGIN, COMMIT, ABORT,
        DISCONNECT, MESSAGE, RECEIPT, ERROR, UNKNOWN };

    StompFrame() {}

    bool setCommand(HeaderTypes command);
    bool addHeader(const QString& key, const QString& value);
    HeaderTypes getCommand() const;
    std::tuple<bool, QString, QString> getHeader(int i) const;
    bool addTextBody(const QString& body);
    bool addByteArrayBody(const QByteArray& body);

    QByteArray convertToByteArray();
    QString convertToQString();

    bool isBinaryBody();

private:
    QString convertCommandToQString(HeaderTypes command) const;
    QByteArray convertCommandToQByteArray(HeaderTypes command) const;
    HeaderTypes convertCommandFromString(const QString& stringCommand) const;
    QByteArray escapeSpecialSymbols(const QByteArray& ba);
    std::pair<bool, QByteArray> unescapeSpecialSymbols(const QByteArray& ba);

private:
    //values as they are written in the frame
    QByteArray m_CommandBA;
    HeaderTypes m_Command;
    QString m_CommandString;
    std::vector<QByteArray> m_HeadersBA;
    std::vector<QByteArray> m_ValuesBA;
    std::vector<QString> m_HeadersString;
    std::vector<QString> m_ValuesString;
    QByteArray m_BodyBA;
    QString m_BodyString;

    bool m_BinaryBody = false;

    friend class StompFrameTest;
};












#endif

#ifndef _STOMPFRAME_H
#define _STOMPFRAME_H

#include <QString>
#include <QStringList>
#include <vector>

class StompFrame {

public:
    enum class HeaderTypes {CONNECT, STOMP, CONNECTED, SEND, SUBSCRIBE, UNSUBSCRIBE, ACK, NACK, BEGIN, COMMIT, ABORT,
        DISCONNECT, MESSAGE, RECEIPT, ERROR, UNKNOWN };

    StompFrame() {}

    bool setCommand(HeaderTypes command);
    bool setCommand(const QString& commannd);
    bool addHeader(const QString& key, const QString& value);
    bool addHeader(const QByteArray& key, const QByteArray& value);
    HeaderTypes getCommand() const;
    std::tuple<bool, QString, QString> getHeader(int i) const;
    bool addTextBody(const QString& body);
    bool addByteArrayBody(const QByteArray& body);
    QString getTextBody() const;

    QByteArray convertToByteArray();
    QString convertToQString();
    bool isBinaryBody();

    QStringList getCommandQStringList();

private:
    QString convertCommandToQString(HeaderTypes command) const;
    QByteArray convertCommandToQByteArray(HeaderTypes command) const;
    HeaderTypes convertCommandFromString(const QString& stringCommand) const;
    QByteArray escapeSpecialSymbols(const QByteArray& ba);

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
    QStringList m_CommandTypeStringList;

    const std::vector<HeaderTypes> m_HeaderTypesList { HeaderTypes::CONNECT, HeaderTypes::STOMP, HeaderTypes::CONNECTED, HeaderTypes::SEND, HeaderTypes::SUBSCRIBE, HeaderTypes::UNSUBSCRIBE, HeaderTypes::ACK, HeaderTypes::NACK, HeaderTypes::BEGIN, HeaderTypes::COMMIT, HeaderTypes::ABORT, HeaderTypes::DISCONNECT, HeaderTypes::MESSAGE, HeaderTypes::RECEIPT, HeaderTypes::ERROR, HeaderTypes::UNKNOWN };

    friend class StompFrameTest;
};

#endif

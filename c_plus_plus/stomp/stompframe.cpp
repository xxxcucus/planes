#include "stompframe.h"

bool StompFrame::setCommand(HeaderTypes command) {
    m_Command = command;
    m_CommandString = convertCommandToQString(command);
    QByteArray convertedArray = convertCommandToQByteArray(command);
    m_CommandBA = convertedArray;

    if (convertedArray == QByteArray(1, 0))
        return false;

    return true;
}

bool StompFrame::setCommand(const QString& command) {
    m_Command = convertCommandFromString(command);
    m_CommandString = command;
    QByteArray convertedArray = convertCommandToQByteArray(m_Command);
    m_CommandBA = convertedArray;

    if (convertedArray == QByteArray(1, 0))
        return false;

    return true;
}

QString StompFrame::convertCommandToQString(HeaderTypes command) const {
    switch(command) {
        case HeaderTypes::CONNECT:
            return QString("CONNECT");
        case HeaderTypes::STOMP:
            return QString("STOMP");
        case HeaderTypes::CONNECTED:
            return QString("CONNECTED");
        case HeaderTypes::SEND:
            return QString("SEND");
        case HeaderTypes::SUBSCRIBE:
            return QString("SUBSCRIBE");
        case HeaderTypes::UNSUBSCRIBE:
            return QString("UNSUBSCRIBE");
        case HeaderTypes::ACK:
            return QString("ACK");
        case HeaderTypes::NACK:
            return QString("NACK");
        case HeaderTypes::BEGIN:
            return QString("BEGIN");
        case HeaderTypes::COMMIT:
            return QString("COMMIT");
        case HeaderTypes::ABORT:
            return QString("ABORT");
        case HeaderTypes::DISCONNECT:
            return QString("DISCONNECT");
        case HeaderTypes::MESSAGE:
            return QString("MESSAGE");
        case HeaderTypes::RECEIPT:
            return QString("RECEIPT");
        case HeaderTypes::ERROR:
            return QString("ERROR");
        default:
            return QString();
    }
    return QString();
}

QByteArray StompFrame::convertCommandToQByteArray(HeaderTypes command) const {
    QString stringCommand = convertCommandToQString(command);
    if (stringCommand.isEmpty())
        return QByteArray(1, 0);
    return stringCommand.toUtf8();
}

StompFrame::HeaderTypes StompFrame::getCommand() const {
    return m_Command;

    /*QString stringCommand = QString(m_CommandBA);
    return convertCommandFromString(stringCommand);*/
}

StompFrame::HeaderTypes StompFrame::convertCommandFromString(const QString& stringCommand) const {
    HeaderTypes retVal = HeaderTypes::UNKNOWN;

    if (stringCommand == "CONNECT")
        retVal = HeaderTypes::CONNECT;
    else if (stringCommand == "STOMP")
        retVal = HeaderTypes::STOMP;
    else if (stringCommand == "CONNECTED")
        retVal = HeaderTypes::CONNECTED;
    else if (stringCommand == "SEND")
        retVal = HeaderTypes::SEND;
    else if (stringCommand == "SUBSCRIBE")
        retVal = HeaderTypes::SUBSCRIBE;
    else if (stringCommand == "UNSUBSCRIBE")
        retVal = HeaderTypes::UNSUBSCRIBE;
    else if (stringCommand == "ACK")
        retVal = HeaderTypes::ACK;
    else if (stringCommand == "NACK")
        retVal = HeaderTypes::NACK;
    else if (stringCommand == "BEGIN")
        retVal = HeaderTypes::BEGIN;
    else if (stringCommand == "COMMIT")
        retVal = HeaderTypes::COMMIT;
    else if (stringCommand == "ABORT")
        retVal = HeaderTypes::ABORT;
    else if (stringCommand == "DISCONNECT")
        retVal = HeaderTypes::DISCONNECT;
    else if (stringCommand == "MESSAGE")
        retVal = HeaderTypes::MESSAGE;
    else if (stringCommand == "RECEIPT")
        retVal = HeaderTypes::RECEIPT;
    else if (stringCommand == "ERROR")
        retVal = HeaderTypes::ERROR;
    else
        retVal = HeaderTypes::UNKNOWN;
    return retVal;
}

bool StompFrame::addHeader(const QString& key, const QString& value) {
    //escape the strings and create headers as QString
    assert(m_HeadersBA.size() == m_ValuesBA.size());
    assert(m_HeadersString.size() == m_ValuesString.size());
    QByteArray baKey = escapeSpecialSymbols(key.toUtf8());
    QByteArray baValue = escapeSpecialSymbols(value.toUtf8());
    m_HeadersBA.push_back(baKey);
    m_ValuesBA.push_back(baValue);
    m_HeadersString.push_back(QString(baKey));
    m_ValuesString.push_back(QString(baValue));
    return true;
}

bool StompFrame::addHeader(const QByteArray& key, const QByteArray& value) {
    assert(m_HeadersBA.size() == m_ValuesBA.size());
    assert(m_HeadersString.size() == m_ValuesString.size());
    QByteArray baKey = escapeSpecialSymbols(key);
    QByteArray baValue = escapeSpecialSymbols(value);
    m_HeadersBA.push_back(baKey);
    m_ValuesBA.push_back(baValue);
    m_HeadersString.push_back(QString(baKey));
    m_ValuesString.push_back(QString(baValue));
    return true;
}

QByteArray StompFrame::escapeSpecialSymbols(const QByteArray& ba) {
    QByteArray::const_iterator it = ba.begin();
    QByteArray retVal;

    while (it != ba.end()) {
        if (*it == 13) {
            retVal.append(92);
            retVal.append(114);
        } else if (*it == 10) {
            retVal.append(92);
            retVal.append(110);
        } else if (*it == 58) {
            retVal.append(92);
            retVal.append(99);
        } else if (*it == 92) {
            retVal.append(92);
            retVal.append(92);
        } else {
            retVal.append(*it);
        }
        it++;
    }

    return retVal;
}

std::tuple<bool, QString, QString> StompFrame::getHeader(int i) const {
    if (i < 0 || i >= m_HeadersBA.size() || i >= m_ValuesBA.size())
        return std::tuple(false, QString(), QString());

    return std::tuple(true, m_HeadersBA[i], m_ValuesBA[i]);
}

QString  StompFrame::convertToQString() {
    assert(m_HeadersString.size() == m_ValuesString.size());

    QString retVal;
    retVal += m_CommandString + "\n";
    for (int i = 0; i < m_HeadersString.size(); i++) {
        QString hdr = QString("%1:%2\n").arg(m_HeadersString[i]).arg(m_ValuesString[i]);
        retVal += hdr;
    }
    retVal += "\n";
    retVal += m_BodyString;
    retVal.append(QChar(0));
    return retVal;
}

bool StompFrame::isBinaryBody() {
    return m_BinaryBody;
}

QByteArray StompFrame::convertToByteArray() {
    assert(m_HeadersBA.size() == m_ValuesBA.size());

    int bsize = m_BodyBA.size();
    if (bsize > 0) {
        addHeader("content-length", QString::number(bsize));
    }

    QByteArray ba;
    ba.append(m_CommandBA);
    ba.append(10);
    for (int i = 0; i < m_HeadersBA.size(); i++) {
        ba.append(m_HeadersBA[i]);
        ba.append(58);
        ba.append(m_ValuesBA[i]);
        ba.append(10);
    }
    ba.append(10);
    ba.append(m_BodyBA);
    ba.append(char(0));
    return ba;
}


bool StompFrame::addTextBody(const QString& body) {
    if (body.isEmpty())
        return true;

    if (m_BodyBA.size() > 0)
        return false;
    m_BodyString = body;
    m_BinaryBody = false;
    return true;
}

bool StompFrame::addByteArrayBody(const QByteArray& body) {
    if (body.size() == 0)
        return true;
    if (m_BodyString.size() > 0)
        return false;
    m_BodyBA = body;
    m_BinaryBody = true;
    return true;
}

QStringList StompFrame::getCommandQStringList() {
    if(!m_CommandTypeStringList.isEmpty())
        return m_CommandTypeStringList;
    for (auto headerType : m_HeaderTypesList) {
        m_CommandTypeStringList.push_back(convertCommandToQString(headerType));
    }
    return m_CommandTypeStringList;
}

QString StompFrame::getTextBody() const {
    return m_BodyString;
}

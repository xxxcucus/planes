#include "stompclient.h"
#include "stompframeparser.h"
#include "stompframe.h"

#include <memory>

StompClient::StompClient() {
    QObject::connect(&m_Socket, &QWebSocket::connected,  this, &StompClient::socketConnected);
    QObject::connect(&m_Socket, &QWebSocket::disconnected,  this, &StompClient::socketDisconnected);
    QObject::connect(&m_Socket, &QWebSocket::binaryMessageReceived, this, &StompClient::binaryMessageReceived);
    QObject::connect(&m_Socket, &QWebSocket::textMessageReceived, this, &StompClient::textMessageReceived);
    QObject::connect(&m_Socket, &QWebSocket::binaryFrameReceived, this, &StompClient::binaryFrameReceived);
    QObject::connect(&m_Socket, &QWebSocket::textFrameReceived, this, &StompClient::textFrameReceived);
    QObject::connect(&m_Socket, &QWebSocket::errorOccurred, this, &StompClient::errorOccurred);
    QObject::connect(&m_Socket, &QWebSocket::alertReceived, this, &StompClient::alertReceived);
    QObject::connect(&m_Socket, &QWebSocket::authenticationRequired, this, &StompClient::authenticationRequired);
    QObject::connect(&m_Socket, &QWebSocket::handshakeInterruptedOnError, this, &StompClient::handshakeInterruptedOnError);
    QObject::connect(&m_Socket, &QWebSocket::peerVerifyError, this, &StompClient::peerVerifyError);
    QObject::connect(&m_Socket, &QWebSocket::proxyAuthenticationRequired, this, &StompClient::proxyAuthenticationRequired);
    QObject::connect(&m_Socket, &QWebSocket::sslErrors, this, &StompClient::sslErrors);
    QObject::connect(&m_Socket, &QWebSocket::stateChanged, this, &StompClient::stateChanged);
}

StompClient::~StompClient() {
    m_Socket.close();
}

int StompClient::getState() {
    return m_Socket.state();
}

void StompClient::setUrl(const QString& url) {
    m_Url = url;
}

void StompClient::socketConnected() {
    qDebug() << "Connected to socket";
    emit clientConnected();
}

void StompClient::socketDisconnected() {
    qDebug() << "Disconnected from socket";
    emit clientDisconnected();
}


void StompClient::connectToServer() {
    if (m_Url.isEmpty())
        return;

    qDebug() << "Connect to " << m_Url;
    m_Socket.open(m_Url);
}

bool StompClient::sendFrame(std::shared_ptr<StompFrame> stompFrame) {
    if (!stompFrame->isBinaryBody()) {
        QString str = stompFrame->convertToQString();
        m_Socket.sendTextMessage(str);
        qDebug() << "Send stomp frame " << str;
        return true;
    }

    QByteArray ba = stompFrame->convertToByteArray();
    m_Socket.sendBinaryMessage(ba);
    return true;
}

void StompClient::binaryMessageReceived(const QByteArray& ba) {
    qDebug() << "Received stomp binary message " << ba.size() << " bytes ";
}

void StompClient::textMessageReceived(const QString& message) {
    qDebug() << "Received stomp text message " << message;
    StompFrameParser parser;

    bool error = true;
    std::shared_ptr<StompFrame> stompFrame = parser.parseFromTextMessage(message, error);
    if (error && stompFrame->getCommand() == StompFrame::HeaderTypes::MESSAGE) {
        emit stompMessageReceived(stompFrame->getTextBody());
        return;
    }

    if (error && stompFrame->getCommand() == StompFrame::HeaderTypes::CONNECTED) {
        emit connectedToChat();
        return;
    }

    if (error && stompFrame->getCommand() == StompFrame::HeaderTypes::ERROR) {
        emit communicationError(stompFrame->getTextBody());
        return;
    }

}

void StompClient::textFrameReceived(const QString &frame, bool isLastFrame) {
    qDebug() << "Received stomp text frame";
}

void StompClient::binaryFrameReceived(const QByteArray &frame, bool isLastFrame) {
    qDebug() << "Received stomp binary frame";
}

void StompClient::errorOccurred(QAbstractSocket::SocketError error) {
    qDebug() << "Error occured " << error;
}

void StompClient::alertReceived(QSsl::AlertLevel level, QSsl::AlertType type, const QString &description) {
    qDebug() << "Alert reveived";
}

void StompClient::authenticationRequired(QAuthenticator* authenticator) {
    qDebug() << "Authentication required";
}

void StompClient::handshakeInterruptedOnError(const QSslError &error) {
    qDebug() << "Handshkage Interrupted on Error";
}

void StompClient::peerVerifyError(const QSslError& error) {
    qDebug() << "Peer verify error";
}

void StompClient::proxyAuthenticationRequired(const QNetworkProxy &proxy, QAuthenticator *authenticator) {
    qDebug() << "proxyAuthenticationRequired";
}

void StompClient::sslErrors(const QList<QSslError> &errors) {
    qDebug() << "sslErrors";
}

void StompClient::stateChanged(QAbstractSocket::SocketState state) {
    qDebug() << "Socket state: " << state;
}

bool StompClient::isClientConnectedToServer() {
    return m_Socket.state() == QAbstractSocket::ConnectedState;
}

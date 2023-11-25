#ifndef __STOMPCLIENT_H_
#define __STOMPCLIENT_H_

#include <QWebSocket>
#include <QString>
#include <QObject>

#include "stompframe.h"

class StompClient : public QObject {
    Q_OBJECT

public:
    StompClient();
    void connectToServer();
    void setUrl(const QString& url);

    bool sendFrame(std::shared_ptr<StompFrame>);

signals:
    void clientConnected();
    void stompMessageReceived(const QString& message);

private slots:
    void socketConnected();
    void socketDisconnected();
    void binaryMessageReceived(const QByteArray& ba);
    void textMessageReceived(const QString& message);
    void textFrameReceived(const QString &frame, bool isLastFrame);
    void binaryFrameReceived(const QByteArray &frame, bool isLastFrame);
    void errorOccurred(QAbstractSocket::SocketError error);
    void alertReceived(QSsl::AlertLevel level, QSsl::AlertType type, const QString &description);
    void authenticationRequired(QAuthenticator *authenticator);
    void handshakeInterruptedOnError(const QSslError &error);
    void peerVerifyError(const QSslError &error);
    void proxyAuthenticationRequired(const QNetworkProxy &proxy, QAuthenticator *authenticator);
    void sslErrors(const QList<QSslError> &errors);
    void stateChanged(QAbstractSocket::SocketState state);

private:
    int getState();

private:
    QWebSocket m_Socket;
    QString m_Url;

};















#endif

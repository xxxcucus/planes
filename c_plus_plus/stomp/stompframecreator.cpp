#include "stompframecreator.h"

std::shared_ptr<StompFrame> StompFrameCreator::createConnectFrame(const QString& acceptVersion, const QString& host, const QString& user, const QString& password, int headerBeatSelf, int heartBeatServer) {
    auto stompFrame = std::make_shared<StompFrame>();
    stompFrame->setCommand(StompFrame::HeaderTypes::CONNECT);
    configureStompConnectFrame(stompFrame, acceptVersion, host);
    return stompFrame;
}

std::shared_ptr<StompFrame> StompFrameCreator::createStompFrame(const QString& acceptVersion, const QString& host, const QString& user, const QString& password, int headerBeatSelf, int heartBeatServer) {
    auto stompFrame = std::make_shared<StompFrame>();
    stompFrame->setCommand(StompFrame::HeaderTypes::STOMP);
    configureStompConnectFrame(stompFrame, acceptVersion, host);
    return stompFrame;
}


void StompFrameCreator::configureStompConnectFrame(std::shared_ptr<StompFrame>  stompFrame, const QString& acceptVersion, const QString& host, const QString& user, const QString& password, int heartBeatSelf, int heartBeatServer) {
    stompFrame->addHeader("accept-version", acceptVersion);
    if (!host.isEmpty())
        stompFrame->addHeader("host", host);
    if (!user.isEmpty())
        stompFrame->addHeader("login", user);
    if (!password.isEmpty())
        stompFrame->addHeader("passcode", password);
    if (heartBeatSelf != 0 || heartBeatServer != 0)
        stompFrame->addHeader("heart-beat", QString("%1,%2").arg(heartBeatSelf).arg(heartBeatServer));
}


std::shared_ptr<StompFrame> StompFrameCreator::createSubscribeFrame(int id, const QString& destination, const QString& ack) {
    auto stompFrame = std::make_shared<StompFrame>();
    stompFrame->setCommand(StompFrame::HeaderTypes::SUBSCRIBE);
    stompFrame->addHeader("id", QString::number(id));
    //stompFrame->addHeader("id", "sub-1");
    stompFrame->addHeader("destination", destination);
    stompFrame->addHeader("ack", ack);
    return stompFrame;
}

std::shared_ptr<StompFrame> StompFrameCreator::createSubscribeFrame(const QString& destination) {
    auto stompFrame = std::make_shared<StompFrame>();
    stompFrame->setCommand(StompFrame::HeaderTypes::SUBSCRIBE);
    stompFrame->addHeader("destination", destination);
    return stompFrame;
}

std::shared_ptr<StompFrame> StompFrameCreator::createUnsubscribeFrame(int id) {
    auto stompFrame = std::make_shared<StompFrame>();
    stompFrame->setCommand(StompFrame::HeaderTypes::UNSUBSCRIBE);
    stompFrame->addHeader("id", QString::number(id));
    return stompFrame;
}

std::shared_ptr<StompFrame> StompFrameCreator::createSendTextFrame(const QString& destination, const QString& message) {
    auto stompFrame = std::make_shared<StompFrame>();
    stompFrame->setCommand(StompFrame::HeaderTypes::SEND);
    stompFrame->addHeader("destination", destination);
    stompFrame->addHeader("content-type", QString("application/json"));
    stompFrame->addTextBody(message);
    return stompFrame;
}

std::shared_ptr<StompFrame> StompFrameCreator::createDisconnectFrame(int id) {
    auto stompFrame = std::make_shared<StompFrame>();
    stompFrame->setCommand(StompFrame::HeaderTypes::DISCONNECT);
    stompFrame->addHeader("receipt", QString::number(id));
    return stompFrame;
}

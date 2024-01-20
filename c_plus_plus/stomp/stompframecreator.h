#ifndef __STOMPFRAMECREATOR_
#define __STOMPFRAMECREATOR_

#include <memory>
#include "stompframe.h"

class StompFrameCreator {

public:
    StompFrameCreator() {}

    std::shared_ptr<StompFrame> createConnectFrame(const QString& acceptVersion, const QString& host, const QString& user = QString(),
                                   const QString& password = QString(), int heartBeatSelf = 0, int heartBeatServer = 0);

    std::shared_ptr<StompFrame> createStompFrame(const QString& acceptVersion, const QString& host, const QString& user = QString(),
                                   const QString& password = QString(), int heartBeatSelf = 0, int heartBeatServer = 0);

    std::shared_ptr<StompFrame> createSubscribeFrame(int id, const QString& destination, const QString& ack);
    std::shared_ptr<StompFrame> createSubscribeFrame(const QString& destination);

    std::shared_ptr<StompFrame> createSendTextFrame(const QString& destination, const QString& message);

    std::shared_ptr<StompFrame> createUnsubscribeFrame(int id);

private:
    void configureStompConnectFrame(std::shared_ptr<StompFrame>, const QString& acceptVersion, const QString& host, const QString& user = QString(), const QString& password = QString(), int headerBeatSelf = 0, int heartBeatServer = 0);

};








#endif

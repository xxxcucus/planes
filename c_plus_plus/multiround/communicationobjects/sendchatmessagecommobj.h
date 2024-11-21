#ifndef _SEND_CHAT_MESSAGE_COMM_OBJ__
#define _SEND_CHAT_MESSAGE_COMM_OBJ__

#if defined MAKE_MULTIPLAYERROUND_LIB
#define MULTIPLAYER_EXPORT Q_DECL_EXPORT
#else
#define MULTIPLAYER_EXPORT Q_DECL_IMPORT
#endif

#include "basiscommobj.h"
#include "viewmodels/basisrequestviewmodel.h"
#include "viewmodels/sendchatmessageviewmodel.h"


class  MULTIPLAYER_EXPORT SendChatMessageCommObj : public BasisCommObj {
    Q_OBJECT

public:
    SendChatMessageCommObj(const QString& requestPath, const QString& actionName, QWidget* parentWidget, QNetworkAccessManager* networkManager, QSettings* settings, bool isSinglePlayer, GlobalData* globalData):
        BasisCommObj(requestPath, actionName, parentWidget, networkManager, settings, isSinglePlayer, globalData) {}

    bool makeRequest(long int receiverId, const QString& message, long int messageId);
    bool validateReply(const QJsonObject& retJson) override;

protected:
    SendChatMessageCommObj() {}

public slots:
    void finishedRequest() override;

signals:
    void messageSent(long int messageId);

private:
    void processResponse(const QJsonObject& retJson);
    SendChatMessageViewModel prepareViewModel(long int receiverId, const QString& message, long int messageId);
    const int m_MaxMessageLength = 128;
};
#endif
//TODO:test

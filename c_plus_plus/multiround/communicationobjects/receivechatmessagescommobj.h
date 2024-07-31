#ifndef __RECEIVE_CHAT_MESSAGES__
#define __RECEIVE_CHAT_MESSAGES__

#if defined MAKE_MULTIPLAYERROUND_LIB
#define MULTIPLAYER_EXPORT Q_DECL_EXPORT
#else
#define MULTIPLAYER_EXPORT Q_DECL_IMPORT
#endif


#include "basiscommobj.h"
#include "viewmodels/getchatmessagesviewmodel.h"
#include "viewmodels/receivedchatmessageviewmodel.h"
#include <vector>

class MultiplayerRound;

class MULTIPLAYER_EXPORT ReceiveChatMessagesCommObj : public BasisCommObj {
    Q_OBJECT

public:
    ReceiveChatMessagesCommObj(const QString& requestPath, const QString& actionName, QWidget* parentWidget, QNetworkAccessManager* networkManager, QSettings* settings, bool isSinglePlayer, GlobalData* globalData, MultiplayerRound* mrd):
        BasisCommObj(requestPath, actionName, parentWidget, networkManager, settings, isSinglePlayer, globalData), m_MultiRound(mrd) {}

    bool makeRequest();
    bool validateReply(const QJsonObject& retJson) override;

protected:
    ReceiveChatMessagesCommObj() {}

public slots:
    void finishedRequest() override;

signals:
    void chatMessagesReceived(const std::vector<ReceivedChatMessageViewModel>& messages);

private:
    GetChatMessagesViewModel prepareViewModel();
    void processResponse(const QJsonObject& retJson);

protected:
    MultiplayerRound* m_MultiRound;

};

#endif

//TODO: test


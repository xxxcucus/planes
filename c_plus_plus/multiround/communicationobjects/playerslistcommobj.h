#ifndef _PLAYERS_LIST_COMM_OBJ__
#define _PLAYERS_LIST_COMM_OBJ__

#if defined MAKE_MULTIPLAYERROUND_LIB
#define MULTIPLAYER_EXPORT Q_DECL_EXPORT
#else
#define MULTIPLAYER_EXPORT Q_DECL_IMPORT
#endif

#include "basiscommobj.h"
#include "viewmodels/basisrequestviewmodel.h"

class  MULTIPLAYER_EXPORT PlayersListCommObj : public BasisCommObj {
    Q_OBJECT

public:
    PlayersListCommObj(const QString& requestPath, const QString& actionName, QWidget* parentWidget, QNetworkAccessManager* networkManager, QSettings* settings, bool isSinglePlayer, GlobalData* globalData):
        BasisCommObj(requestPath, actionName, parentWidget, networkManager, settings, isSinglePlayer, globalData) {}

    bool makeRequest();
    bool validateReply(const QJsonObject& retJson) override;

protected:
    PlayersListCommObj() {}

public slots:
    void finishedRequest() override;

signals:
    void playersListReceived(const QStringList& players);

private:
    void processResponse(const QJsonObject& retJson);
    BasisRequestViewModel prepareViewModel();
};

//TODO:test











#endif

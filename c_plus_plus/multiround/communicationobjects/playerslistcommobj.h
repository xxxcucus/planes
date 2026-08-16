#ifndef _PLAYERS_LIST_COMM_OBJ__
#define _PLAYERS_LIST_COMM_OBJ__

#if defined MAKE_MULTIPLAYERROUND_LIB
#define MULTIPLAYER_EXPORT Q_DECL_EXPORT
#else
#define MULTIPLAYER_EXPORT Q_DECL_IMPORT
#endif


#include "basiscommobj.h"
#include "viewmodels/getavailableusersviewmodel.h"
#include "viewmodels/userwithlastloginviewmodel.h"
#include <vector>

//class  MULTIPLAYER_EXPORT PlayersListCommObj : public BasisCommObj {
class PlayersListCommObj : public BasisCommObj {
    Q_OBJECT

public:
    PlayersListCommObj(const QString& requestPath, const QString& actionName, QNetworkAccessManager* networkManager, QSettings* settings, bool isSinglePlayer, GlobalData* globalData):
        BasisCommObj(requestPath, actionName, networkManager, settings, isSinglePlayer, globalData) {
    }

    bool makeRequest(int lastLoginDay = 0);
    bool validateReply(const QJsonObject& retJson) override;

protected:
    PlayersListCommObj() {}

public slots:
    void finishedRequest() override;

signals:
    void playersListReceived(const std::vector<UserWithLastLoginViewModel>& players);

private:
    void processResponse(const QJsonObject& retJson);
    GetAvailableUsersViewModel prepareViewModel(int lastLoginDay = 0);

private:
};

//TODO:test











#endif

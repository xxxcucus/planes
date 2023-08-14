#ifndef __DEACTIVATE_USER_COMMOBJ__
#define __DEACTIVATE_USER_COMMOBJ__

#if defined MAKE_MULTIPLAYERROUND_LIB
#define MULTIPLAYER_EXPORT Q_DECL_EXPORT
#else
#define MULTIPLAYER_EXPORT Q_DECL_IMPORT
#endif

#include "basiscommobj.h"
#include "viewmodels/gameviewmodel.h"

class MULTIPLAYER_EXPORT DeactivateUserCommObj : public BasisCommObj {
    Q_OBJECT

public:
    DeactivateUserCommObj(const QString& requestPath, const QString& actionName, QWidget* parentWidget, QNetworkAccessManager* networkManager, QSettings* settings, bool isSinglePlayer, GlobalData* globalData):
        BasisCommObj(requestPath, actionName, parentWidget, networkManager, settings, isSinglePlayer, globalData) {}

    bool makeRequest(const QString& gameName);
    bool validateReply(const QJsonObject& retJson) override;

protected:
    DeactivateUserCommObj() {}

public slots:
    void finishedRequest() override;

signals:
    void userDeactivated();

private:
    DeactivateUserViewModel prepareViewModel(const QString& username);
    void processResponse(const QJsonObject& retJson);

    //TODO: test
};



#ifndef __LOGOUT_COMM_OBJ__
#define __LOGOUT_COMM_OBJ__

#if defined MAKE_MULTIPLAYERROUND_LIB
#define MULTIPLAYER_EXPORT Q_DECL_EXPORT
#else
#define MULTIPLAYER_EXPORT Q_DECL_IMPORT
#endif

#include "basiscommobj.h"
#include <QMessageBox>
#include "viewmodels/logoutviewmodel.h"

class MULTIPLAYER_EXPORT LogoutCommObj : public BasisCommObj {
    Q_OBJECT

public:
    LogoutCommObj(const QString& requestPath, const QString& actionName, QWidget* parentWidget, QNetworkAccessManager* networkManager, QSettings* settings, bool isSinglePlayer, GlobalData* globalData):
        BasisCommObj(requestPath, actionName, parentWidget, networkManager, settings, isSinglePlayer, globalData) {}


    bool makeRequest(const QString& username);
    bool validateReply(const QJsonObject& retJson) override;

protected:
    LogoutCommObj() {}

public slots:
    void finishedRequest() override;

signals:
    void logoutCompleted();
    void logoutFailed();

private:
    LogoutViewModel prepareViewModel();
    void processResponse();

private:
    QString m_UserName;
    //friend class LoginCommObjTest;
};

//TODO: test

#endif

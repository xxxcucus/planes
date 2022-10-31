#ifndef __REFRESH_GAME_STATUS_COMMOBJ__
#define __REFRESH_GAME_STATUS_COMMOBJ__

#if defined MAKE_MULTIPLAYERROUND_LIB
#define MULTIPLAYER_EXPORT Q_DECL_EXPORT
#else
#define MULTIPLAYER_EXPORT Q_DECL_IMPORT
#endif

#include "basiscommobj.h"
#include "viewmodels/gameviewmodel.h"

class  MULTIPLAYER_EXPORT RefreshGameStatusCommObj : public BasisCommObj {
    Q_OBJECT
    
public:
    RefreshGameStatusCommObj(const QString& requestPath, const QString& actionName, QWidget* parentWidget, QNetworkAccessManager* networkManager, QSettings* settings, bool isSinglePlayer, GlobalData* globalData):
        BasisCommObj(requestPath, actionName, parentWidget, networkManager, settings, isSinglePlayer, globalData) {}
    
    bool makeRequest(const QString& gameName);
    bool validateReply(const QJsonObject& retJson) override;
    
protected:
    RefreshGameStatusCommObj() {}

public slots:
    void finishedRequest() override;  
    
signals:
    void refreshStatus(bool exists, const QString& gameName, const QString& firstPlayerName, const QString& secondPlayerName, const QString& currentRoundId);
    
    
private:
    GameViewModel prepareViewModel(const QString& gameName);
    void processResponse(const QJsonObject& retJson);

private:
    QString m_GameName;

    friend class RefreshGameStatusCommObjTest;
};



















#endif

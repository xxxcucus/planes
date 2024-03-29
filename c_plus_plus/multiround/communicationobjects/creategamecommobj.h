#ifndef __CREATE_GAME_COMMOBJ__
#define __CREATE_GAME_COMMOBJ__

#if defined MAKE_MULTIPLAYERROUND_LIB
#define MULTIPLAYER_EXPORT Q_DECL_EXPORT
#else
#define MULTIPLAYER_EXPORT Q_DECL_IMPORT
#endif

#include "basiscommobj.h"
#include "viewmodels/gameviewmodel.h"

class MULTIPLAYER_EXPORT CreateGameCommObj : public BasisCommObj {
    Q_OBJECT
    
public:
    CreateGameCommObj(const QString& requestPath, const QString& actionName, QWidget* parentWidget, QNetworkAccessManager* networkManager, QSettings* settings, bool isSinglePlayer, GlobalData* globalData):
        BasisCommObj(requestPath, actionName, parentWidget, networkManager, settings, isSinglePlayer, globalData) {}
    
    bool makeRequest(const QString& gameName);
    bool validateReply(const QJsonObject& retJson) override;
    
protected:
    CreateGameCommObj() {}

public slots:
    void finishedRequest() override;       
    
signals:
    void gameCreated(const QString& gameName, const QString& userName, bool resetGameScore);
    
private:
    GameViewModel prepareViewModel(const QString& gameName);
    void processResponse(const QJsonObject& retJson);

private:
    QString m_GameName;

    friend class CreateGameCommObjTest;
};














#endif

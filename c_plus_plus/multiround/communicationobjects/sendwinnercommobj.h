#ifndef __SEND_WINNER_COMMOBJ__
#define __SEND_WINNER_COMMOBJ__

#if defined MAKE_MULTIPLAYERROUND_LIB
#define MULTIPLAYER_EXPORT Q_DECL_EXPORT
#else
#define MULTIPLAYER_EXPORT Q_DECL_IMPORT
#endif

#include "basiscommobj.h"
#include "viewmodels/sendwinnerviewmodel.h"

class MULTIPLAYER_EXPORT SendWinnerCommObj : public BasisCommObj {
    Q_OBJECT
    
public:
    SendWinnerCommObj(const QString& requestPath, const QString& actionName, QWidget* parentWidget, QNetworkAccessManager* networkManager, QSettings* settings, bool isSinglePlayer, GlobalData* globalData):
        BasisCommObj(requestPath, actionName, parentWidget, networkManager, settings, isSinglePlayer, globalData) {}
    
    bool makeRequest(bool draw, long int winnerId);
    bool validateReply(const QJsonObject& retJson) override;
    
protected:
    SendWinnerCommObj() {}

public slots:
    void finishedRequest() override;   

private:
    SendWinnerViewModel prepareViewModel(bool draw, long int winnerId);

    friend class SendWinnerCommObjTest;
};

#endif 

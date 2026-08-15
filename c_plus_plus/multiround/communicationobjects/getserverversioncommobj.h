#ifndef __GET_SERVER_VERSION_COMMOBJ__
#define __GET_SERVER_VERSION_COMMOBJ__

#if defined MAKE_MULTIPLAYERROUND_LIB
#define MULTIPLAYER_EXPORT Q_DECL_EXPORT
#else
#define MULTIPLAYER_EXPORT Q_DECL_IMPORT
#endif

#include <QMessageBox>

#include "basiscommobj.h"

//class MULTIPLAYER_EXPORT GetServerVersionCommObj : public BasisCommObj {
class GetServerVersionCommObj : public BasisCommObj {
    Q_OBJECT
    
public:
    GetServerVersionCommObj(const QString& requestPath, const QString& actionName, QWidget* parentWidget, QNetworkAccessManager* networkManager, QSettings* settings, bool isSinglePlayer, GlobalData* globalData):
        BasisCommObj(requestPath, actionName, parentWidget, networkManager, settings, isSinglePlayer, globalData) {
        m_ServerUpdatedMessageBox = new QMessageBox(m_ParentWidget);
        m_ServerUpdatedMessageBox->setText("Game server was updated.\nThis version of the game client is outdated.\nPlease update the application.");
        m_ServerUpdatedMessageBox->setStandardButtons(QMessageBox::NoButton);
    }
    
    bool makeRequest();
    bool validateReply(const QJsonObject& retJson) override;
    
public slots:
    void finishedRequest() override;

private:
    QMessageBox* m_ServerUpdatedMessageBox = nullptr;
};












#endif

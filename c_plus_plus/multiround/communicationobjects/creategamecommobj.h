#ifndef __CREATE_GAME_COMMOBJ__
#define __CREATE_GAME_COMMOBJ__

#if defined MAKE_MULTIPLAYERROUND_LIB
#define MULTIPLAYER_EXPORT Q_DECL_EXPORT
#else
#define MULTIPLAYER_EXPORT Q_DECL_IMPORT
#endif

#include <QMessageBox>
#include "basiscommobj.h"
#include "viewmodels/gameviewmodel.h"

//class MULTIPLAYER_EXPORT CreateGameCommObj : public BasisCommObj {
class CreateGameCommObj: public BasisCommObj {
    Q_OBJECT
    
public:
    CreateGameCommObj(const QString& requestPath, const QString& actionName, QWidget* parentWidget, QNetworkAccessManager* networkManager, QSettings* settings, bool isSinglePlayer, GlobalData* globalData):
        BasisCommObj(requestPath, actionName, parentWidget, networkManager, settings, isSinglePlayer, globalData) {
        m_NoUserMessageBox = new QMessageBox(m_ParentWidget);
        m_NoUserMessageBox->setText("No user logged in");
        m_NoUserMessageBox->setStandardButtons(QMessageBox::NoButton);

        m_CreatedMessageBox = new QMessageBox(m_ParentWidget);
        m_CreatedMessageBox->setText("Game creation successful!");
        m_CreatedMessageBox->setStandardButtons(QMessageBox::NoButton);
    }

    virtual ~CreateGameCommObj();
    
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

    QMessageBox* m_NoUserMessageBox = nullptr;
    QMessageBox* m_CreatedMessageBox = nullptr;

    friend class CreateGameCommObjTest;
};














#endif

#ifndef __NOROBOT_COMM_OBJ__
#define __NOROBOT_COMM_OBJ__

#if defined MAKE_MULTIPLAYERROUND_LIB
#define MULTIPLAYER_EXPORT Q_DECL_EXPORT
#else
#define MULTIPLAYER_EXPORT Q_DECL_IMPORT
#endif

#include "basiscommobj.h"
#include "viewmodels/norobotviewmodel.h"
#include <QMessageBox>

class MULTIPLAYER_EXPORT NoRobotCommObj : public BasisCommObj {
    Q_OBJECT
    
public:
    NoRobotCommObj(const QString& requestPath, const QString& actionName, QWidget* parentWidget, QNetworkAccessManager* networkManager, QSettings* settings, bool isSinglePlayer, GlobalData* globalData):
        BasisCommObj(requestPath, actionName, parentWidget, networkManager, settings, isSinglePlayer, globalData) {
            m_LoadingMessageBox = new QMessageBox(m_ParentWidget);
            m_LoadingMessageBox->setText("Connecting to server ..");
            m_LoadingMessageBox->setStandardButtons(QMessageBox::NoButton);
        }
    virtual ~NoRobotCommObj();
    
    bool makeRequest(const QString& requestId, const QString& answer);
    bool validateReply(const QJsonObject& retJson) override;

protected:
    NoRobotCommObj() {}
    
public slots:
    void finishedRequest() override;   
    void errorRequest(QNetworkReply::NetworkError code) override;
    
    
signals:
    void registrationFailed();
    void registrationComplete();
    
private:
    NoRobotViewModel prepareViewModel(const QString& requestId, const QString& answer);
    void processResponse(const QJsonObject& retJson);

private:
    QMessageBox* m_LoadingMessageBox = nullptr;

    friend class NoRobotCommObjTest;
};















#endif

#ifndef __LOGIN_COMM_OBJ__
#define __LOGIN_COMM_OBJ__

#include "basiscommobj.h"
#include <QMessageBox>
#include "viewmodels/loginviewmodel.h"

class LoginCommObj : public BasisCommObj {
    Q_OBJECT
    
public:
    LoginCommObj(const QString& requestPath, const QString& actionName, QWidget* parentWidget, QNetworkAccessManager* networkManager, QSettings* settings, bool isSinglePlayer, GlobalData* globalData):
        BasisCommObj(requestPath, actionName, parentWidget, networkManager, settings, isSinglePlayer, globalData) {
            m_LoadingMessageBox = new QMessageBox(m_ParentWidget);
            m_LoadingMessageBox->setText("Connecting to server ..");
            m_LoadingMessageBox->setStandardButtons(QMessageBox::NoButton);
        }
    virtual ~LoginCommObj();
    
    bool makeRequest(const QString& username, const QString& password);
    bool validateReply(const QJsonObject& retJson) override;

protected:
    LoginCommObj() {}
    
public slots:
    void errorRequest(QNetworkReply::NetworkError code) override;
    void finishedRequest() override;       
    
signals:
    void loginCompleted();
    void loginFailed();
        
private:
    LoginViewModel prepareViewModel(const QString& username, const QString& password);
    bool searchAuthorizationInHeaders(const QList<QNetworkReply::RawHeaderPair>& headers);
    void processResponse(bool successfull, const QJsonObject& retJson);

private:
    QString m_UserName;
    QMessageBox* m_LoadingMessageBox = nullptr; 

    friend class LoginCommObjTest;
};










#endif


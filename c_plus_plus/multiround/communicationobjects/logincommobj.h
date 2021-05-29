#ifndef __LOGIN_COMM_OBJ__
#define __LOGIN_COMM_OBJ__

#include "basiscommobj.h"

class LoginCommObj : public BasisCommObj {
    Q_OBJECT
    
public:
    LoginCommObj(const QString& requestPath, const QString& actionName, QWidget* parentWidget, QNetworkAccessManager* networkManager, QSettings* settings, bool isSinglePlayer, GlobalData* globalData):
        BasisCommObj(requestPath, actionName, parentWidget, networkManager, settings, isSinglePlayer, globalData) {}
    
    bool makeRequest(const QString& username, const QString& password);
    bool validateReply(const QJsonObject& retJson) override;
    
public slots:
    void errorRequest(QNetworkReply::NetworkError code) override;
    void finishedRequest() override;       
    
signals:
    void loginCompleted();
    void loginFailed();
        
private:
    QString m_UserName;
};










#endif


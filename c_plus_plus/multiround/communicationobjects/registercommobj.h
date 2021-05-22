#ifndef _REGISTER_COMM_OBJ__
#define _REGISTER_COMM_OBJ__


#include "basiscommobj.h"

class RegisterCommObj : public BasisCommObj {
    Q_OBJECT
    
public:
    RegisterCommObj(const QString& requestPath, const QString& actionName, QWidget* parentWidget, QNetworkAccessManager* networkManager, QSettings* settings, bool isSinglePlayer, GlobalData* globalData):
        BasisCommObj(requestPath, actionName, parentWidget, networkManager, settings, isSinglePlayer, globalData) {}
    
    bool makeRequest(const QString& username, const QString& password);
    bool validateReply(const QJsonObject& retJson) override;
    
public slots:
    void finishedRequest() override;       
    
signals:
    void noRobotRegistration(const std::vector<QString>& images, const QJsonObject& request);
    
private:
    QString m_UserName;
};




















#endif

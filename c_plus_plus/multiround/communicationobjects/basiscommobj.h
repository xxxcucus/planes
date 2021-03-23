#ifndef __BASIS_COM_OBJ__
#define __BASIS_COM_OBJ__

#include <QObject>
#include <QJsonObject>
#include <QNetworkReply>
#include <QNetworkAccessManager>
#include <QSettings>
#include "global/globaldata.h"

class BasisCommObj : public QObject {
    Q_OBJECT
    
public:
    BasisCommObj(const QString& requestPath, const QString& actionName, QNetworkAccessManager* networkManager, QSettings* settings, bool isSinglePlayer, GlobalData* globalData): 
        m_RequestPath(requestPath), m_ActionName(actionName), m_NetworkManager(networkManager), m_Settings(settings), m_IsSinglePlayer(isSinglePlayer), m_GlobalData(globalData) {}
    
    void makeRequestBasis(bool withToken);
    virtual bool validateReply(const QJsonObject& reply) = 0;
    
public slots:
    virtual void errorRequest(QNetworkReply::NetworkError code);
    virtual void finishedRequest();   
    
protected:
    bool finishRequestHelper(QJsonObject& retJson);
    bool checkInt(const QJsonValue& jsonValue);
    
protected:
    QNetworkReply* m_ReplyObject = nullptr;
    QString m_RequestPath;
    QString m_ActionName;
    QJsonObject m_RequestData;
    
    QNetworkAccessManager* m_NetworkManager;
    QSettings* m_Settings;
    bool m_IsSinglePlayer = true;
    GlobalData* m_GlobalData;
};


#endif

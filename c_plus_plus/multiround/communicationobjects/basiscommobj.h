#ifndef __BASIS_COM_OBJ__
#define __BASIS_COM_OBJ__

#include <QObject>
#include <QJsonObject>
#include <QNetworkReply>
#include <QNetworkAccessManager>
#include <QSettings>
#include <QWidget>
#include "global/globaldata.h"

class BasisCommObj : public QObject {
    Q_OBJECT
    
public:
    BasisCommObj(const QString& requestPath, const QString& actionName, QWidget* parentWidget, QNetworkAccessManager* networkManager, QSettings* settings, bool isSinglePlayer, GlobalData* globalData): 
        m_RequestPath(requestPath), m_ActionName(actionName), m_ParentWidget(parentWidget), m_NetworkManager(networkManager), m_Settings(settings), m_IsSinglePlayer(isSinglePlayer), m_GlobalData(globalData) {
            connect( m_NetworkManager, SIGNAL(sslErrors(QNetworkReply*,QList<QSslError>)), this, SLOT(sslErrorOccured(QNetworkReply*,QList<QSslError>)));
        }
    virtual ~BasisCommObj();

    bool makeRequestBasis(bool withToken, bool fromFinishedSlot = false);
    virtual bool validateReply(const QJsonObject& reply) = 0;
    
protected:
    BasisCommObj() {}

public slots:
    virtual void errorRequest(QNetworkReply::NetworkError code);
    virtual void finishedRequest();   
    void sslErrorOccured(QNetworkReply* reply, const QList<QSslError>& errors);
    
protected:
    bool finishRequestHelper(QJsonObject& retJson);
    bool checkInt(const QJsonValue& jsonValue);
    bool checkLong(const QString& stringVal);
    
protected:
    std::vector<QNetworkReply*> m_ReplyObjectVector; //TODO: we don't need this
    QNetworkReply* m_ReplyObject = nullptr;
    QString m_RequestPath;
    QString m_ActionName;
    QJsonObject m_RequestData;
    
    QWidget* m_ParentWidget;
    QNetworkAccessManager* m_NetworkManager;
    QSettings* m_Settings;
    bool m_IsSinglePlayer = true;
    GlobalData* m_GlobalData;    
};


#endif

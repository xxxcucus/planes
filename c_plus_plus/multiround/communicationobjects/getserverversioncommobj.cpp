#include "getserverversioncommobj.h"

#include <QJsonObject>
#include <QApplication>

QString knownServerVersion = "0.1.2";

bool GetServerVersionCommObj::makeRequest()
{ 
    if (m_IsSinglePlayer) {
        //qDebug() << "makeRequestBasis in single player modus";
        return false;
    }

    m_RequestData = QJsonObject();
    
    makeRequestBasis(false);
    return true;
}

void GetServerVersionCommObj::finishedRequest()
{
    QJsonObject retJson;
    if (!finishRequestHelper(retJson)) 
        return;
    
    if (retJson.value("versionString").toString() != knownServerVersion) {
        emit logMessage("Game server was updated. Please update your application as well!");
        QApplication::quit();
    }
}

bool GetServerVersionCommObj::validateReply(const QJsonObject& reply) {
    return (reply.contains("versionString")); //TODO: stronger validation
}

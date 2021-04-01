#include "getserverversioncommobj.h"

#include <QMessageBox>
#include <QJsonObject>
#include <QApplication>

QString knownServerVersion = "0.1.0";

bool GetServerVersionCommObj::makeRequest()
{ 
    if (m_IsSinglePlayer) {
        qDebug() << "makeRequestBasis in single player modus";
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
        QMessageBox msgBox;
        msgBox.setText("Game server was updated.\nThis version of the game client is outdated.\nPlease update the application."); 
        msgBox.exec();   
        QApplication::quit();
    }
}

bool GetServerVersionCommObj::validateReply(const QJsonObject& reply) {
    return (reply.contains("versionString")); //TODO: stronger validation
}

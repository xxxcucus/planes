#include "basiscommobj.h"

#include <cmath>

#include <QTextCodec>
#include <QMessageBox>
#include <QDebug>
#include <QJsonValue>
#include "communicationtools.h"

//TODO: add timer to control maximum duration of request
void BasisCommObj::makeRequestBasis(bool withToken)
{
    if (m_IsSinglePlayer) {
        qDebug() << "makeRequestBasis in single player modus";
        return;
    }
    
    if (m_ReplyObject != nullptr && m_ReplyObject->isRunning())
        return;
    
    if (m_ReplyObject != nullptr)
        delete m_ReplyObject;
      
    if (withToken) {
        m_ReplyObject = CommunicationTools::buildPostRequestWithAuth(m_RequestPath, m_Settings->value("multiplayer/serverpath").toString(), m_RequestData, m_GlobalData->m_UserData.m_AuthToken, m_NetworkManager);
    } else {
        m_ReplyObject = CommunicationTools::buildPostRequest(m_RequestPath, m_Settings->value("multiplayer/serverpath").toString(), m_RequestData, m_NetworkManager);
    }

    connect(m_ReplyObject, &QNetworkReply::finished, this, &BasisCommObj::finishedRequest);
    connect(m_ReplyObject, &QNetworkReply::errorOccurred, this, &BasisCommObj::errorRequest);
        
}

void BasisCommObj::errorRequest(QNetworkReply::NetworkError code)
{
    qDebug() << "Error 1";
    if (m_IsSinglePlayer) {
        qDebug() << "errorRequest in single player modus";
        return;
    }

    CommunicationTools::treatCommunicationError(m_ActionName, m_ReplyObject);
}


void BasisCommObj::finishedRequest()
{
    qDebug() << "Finished Request 1";
    QJsonObject retJson;
    if (!finishRequestHelper(retJson)) 
        return;
}

bool BasisCommObj::finishRequestHelper(QJsonObject& retJson)
{
    if (m_IsSinglePlayer) {
        qDebug() << "finishRequestHelper in single player modus";
        return false;
    }

    if (m_ReplyObject->error() != QNetworkReply::NoError) {
        return false;
    }
    
    QByteArray reply = m_ReplyObject->readAll();
    QString replyQString = QTextCodec::codecForMib(106)->toUnicode(reply);
    qDebug() << replyQString;
    retJson = CommunicationTools::objectFromString(replyQString);
 
    if (!validateReply(retJson)) {
        QMessageBox msgBox;
        msgBox.setText(m_ActionName + " reply was not recognized"); 
        msgBox.exec();

        return false;
    }

    return true;
}

bool BasisCommObj::checkInt(const QJsonValue& jsonValue) {
    double val = jsonValue.toDouble();
    double fractpart, intpart;
    fractpart = modf(val , &intpart);
    if (fractpart < 0.000001)
        return true;
    return false;
}


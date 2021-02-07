#include "logincommobj.h"


#include <QMessageBox>
#include <QTextCodec>
#include "viewmodels/loginviewmodel.h"

bool LoginCommObj::makeRequest(const QString& username, const QString& password)
{
    LoginViewModel loginData;
    loginData.m_Password = password; 
    loginData.m_UserName = username; 

    m_GlobalData->reset();
    m_UserName = username;
    m_RequestData = loginData.toLoginJson();
    
    makeRequestBasis(false);
    return true;
}

void LoginCommObj::finishedRequest()
{
    QJsonObject retJson;
    if (!finishRequestHelper(retJson)) 
        return;

    //TODO: to receive the user id when successfull
    
    QList<QByteArray> headers = m_ReplyObject->rawHeaderList();
    bool successfull = false;
    
    for(QByteArray hdr : headers) {
        QString hdrQString = QTextCodec::codecForMib(106)->toUnicode(hdr);
        if (hdrQString == "Authorization") {
            m_GlobalData->m_UserData.m_AuthToken = m_ReplyObject->rawHeader(hdr);
            qDebug() << hdrQString << ":" << m_ReplyObject->rawHeader(hdr);
            successfull = true;
        }
    }
    
    if (successfull) {
        QMessageBox msgBox;
        msgBox.setText("Login successfull!"); 
        msgBox.exec();               
        m_GlobalData->m_UserData.m_UserName = m_UserName;
        emit loginCompleted();
    } else {
        QMessageBox msgBox;
        msgBox.setText("Login reply was not recognized"); 
        msgBox.exec();        
    }
}

bool LoginCommObj::validateReply(const QJsonObject& reply) {
    return true;
}

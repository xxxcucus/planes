#include "logincommobj.h"


#include <QMessageBox>
#include "viewmodels/loginviewmodel.h"
#include "communicationtools.h"

LoginCommObj::~LoginCommObj()
{
    delete m_LoadingMessageBox;
}

bool LoginCommObj::makeRequest(const QString& username, const QString& password)
{
    if (m_IsSinglePlayer) {
        //qDebug() << "makeRequestBasis in single player modus";
        return false;
    }
    m_GlobalData->reset();
    //m_UserName = username; TODO: is this needed
   
    m_RequestData = prepareViewModel(username, password).toLoginJson();
    if (m_LoadingMessageBox != nullptr)
        m_LoadingMessageBox->show();
    
    makeRequestBasis(false);
    return true;
}

LoginViewModel LoginCommObj::prepareViewModel(const QString& username, const QString& password) {
    LoginViewModel loginData;
    loginData.m_Password = password;
    loginData.m_UserName = username;
    return loginData;
}

void LoginCommObj::finishedRequest()
{
    if (m_LoadingMessageBox != nullptr) {
        if (m_LoadingMessageBox->isVisible())
            m_LoadingMessageBox->hide();
    }

    QJsonObject retJson;
    if (!finishRequestHelper(retJson)) 
        return;
    
    QList<QNetworkReply::RawHeaderPair> headers = m_ReplyObject->rawHeaderPairs();
    bool successfull = searchAuthorizationInHeaders(headers);
    processResponse(successfull, retJson);

}

void LoginCommObj::processResponse(bool successfull, const QJsonObject& retJson) {
    //TODO: token refresh
    if (successfull) {
        if (m_ParentWidget != nullptr) {
            QMessageBox msgBox(m_ParentWidget);
            msgBox.setText("Login successfull!");
            msgBox.exec();
        }
        m_GlobalData->m_UserData.m_UserName = retJson.value("username").toString();
        m_GlobalData->m_UserData.m_UserId = retJson.value("id").toString().toLong();
        m_GlobalData->m_UserData.m_UserPassword.clear();
        m_GlobalData->m_GameData.reset();
        //qDebug() << "Login successfull " << m_GlobalData->m_UserData.m_UserName << " " << m_GlobalData->m_UserData.m_UserId;
        emit loginCompleted();
    } else {
        if (m_ParentWidget != nullptr) {
            QMessageBox msgBox(m_ParentWidget);
            msgBox.setText("Login reply was not recognized");
            msgBox.exec();
        }
    }
}

bool LoginCommObj::searchAuthorizationInHeaders(const QList<QNetworkReply::RawHeaderPair>& headers) {
    bool successfull = false;

    for (QNetworkReply::RawHeaderPair hdr : headers) {
        QString hdrQString(hdr.first);
        if (hdrQString == "Authorization") {
            m_GlobalData->m_UserData.m_AuthToken = hdr.second;
            //qDebug() << hdrQString << ":" << m_ReplyObject->rawHeader(hdr);
            successfull = true;
        }
    }
    return successfull;
}

bool LoginCommObj::validateReply(const QJsonObject& reply) {
    if (!reply.contains("id") || !reply.contains("username"))
        return false;

    if (!checkLong(reply.value("id").toString()))
        return false;
    
    return true;
}

void LoginCommObj::errorRequest(QNetworkReply::NetworkError code)
{
    if (m_LoadingMessageBox != nullptr) {
        if (m_LoadingMessageBox->isVisible())
            m_LoadingMessageBox->hide();
    }
    BasisCommObj::errorRequest(code);
    emit loginFailed();
}

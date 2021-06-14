#include "registercommobj.h"
#include "viewmodels/loginviewmodel.h"

bool RegisterCommObj::makeRequest(const QString& username, const QString& password)
{
    LoginViewModel loginData;
    loginData.m_Password = password; 
    loginData.m_UserName = username; 

    m_UserName = username;
    m_RequestData = loginData.toRegisterJson();
    
    m_LoadingMessageBox = new QMessageBox(m_ParentWidget);
    m_LoadingMessageBox->setText("Connecting to server ..");
    m_LoadingMessageBox->setStandardButtons(QMessageBox::NoButton);
    m_LoadingMessageBox->show();
    
    makeRequestBasis(false);
    return true;
}

void RegisterCommObj::finishedRequest()
{
    if (m_LoadingMessageBox->isVisible())
        m_LoadingMessageBox->hide();

    QJsonObject retJson;
    if (!finishRequestHelper(retJson)) 
        return;

    std::vector<QString> images;
    
    images.push_back(retJson.value("image_id_1").toString());
    images.push_back(retJson.value("image_id_2").toString());    
    images.push_back(retJson.value("image_id_3").toString());
    images.push_back(retJson.value("image_id_4").toString());    
    images.push_back(retJson.value("image_id_5").toString());
    images.push_back(retJson.value("image_id_6").toString());    
    images.push_back(retJson.value("image_id_7").toString());
    images.push_back(retJson.value("image_id_8").toString());    
    images.push_back(retJson.value("image_id_9").toString());    
    long int request_id = retJson.value("id").toString().toLong(); //TODO validation
    
    //qDebug() << "Registration request with id " << request_id << " received ";
    emit noRobotRegistration(images, retJson);

}

bool RegisterCommObj::validateReply(const QJsonObject& reply) {
    return (reply.contains("id") && reply.contains("username") && reply.contains("createdAt") && reply.contains("question") && 
        reply.contains("image_id_1") && reply.contains("image_id_2") && reply.contains("image_id_3") &&
        reply.contains("image_id_4") && reply.contains("image_id_5") && reply.contains("image_id_6") &&
        reply.contains("image_id_7") && reply.contains("image_id_8") && reply.contains("image_id_9"));

}

void RegisterCommObj::errorRequest(QNetworkReply::NetworkError code)
{
    if (m_LoadingMessageBox->isVisible())
        m_LoadingMessageBox->hide();
    BasisCommObj::errorRequest(code);
}

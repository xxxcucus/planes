#include "registercommobj.h"


RegisterCommObj::~RegisterCommObj()
{
    if (m_LoadingMessageBox != nullptr)
        delete m_LoadingMessageBox;
}


bool RegisterCommObj::makeRequest(const QString& username, const QString& password)
{
    if (m_IsSinglePlayer) {
        //qDebug() << "makeRequestBasis in single player modus";
        return false;
    }

    m_UserName = username;
    m_RequestData = prepareViewModel(username, password).toRegisterJson();
    
    if (m_LoadingMessageBox != nullptr)
        m_LoadingMessageBox->show();
    
    makeRequestBasis(false);
    return true;
}

LoginViewModel RegisterCommObj::prepareViewModel(const QString& username, const QString& password) {
    LoginViewModel loginData;
    loginData.m_Password = password;
    loginData.m_UserName = username;
    return loginData;
}

void RegisterCommObj::finishedRequest()
{
    if (m_LoadingMessageBox != nullptr && m_LoadingMessageBox->isVisible())
        m_LoadingMessageBox->hide();

    QJsonObject retJson;
    if (!finishRequestHelper(retJson)) 
        return;

    processResponse(retJson);
}

void RegisterCommObj::processResponse(const QJsonObject& retJson) {
    m_Images.clear();

    m_Images.push_back(retJson.value("image_id_1").toString());
    m_Images.push_back(retJson.value("image_id_2").toString());
    m_Images.push_back(retJson.value("image_id_3").toString());
    m_Images.push_back(retJson.value("image_id_4").toString());
    m_Images.push_back(retJson.value("image_id_5").toString());
    m_Images.push_back(retJson.value("image_id_6").toString());
    m_Images.push_back(retJson.value("image_id_7").toString());
    m_Images.push_back(retJson.value("image_id_8").toString());
    m_Images.push_back(retJson.value("image_id_9").toString());
    long int requestId = retJson.value("id").toString().toLong(); //TODO validation
    QString question = retJson.value("question").toString();

    //qDebug() << "Registration request with id " << request_id << " received ";
    emit noRobotRegistration(m_Images, question, requestId);
}

bool RegisterCommObj::validateReply(const QJsonObject& reply) {
    return (reply.contains("id") && reply.contains("username") && reply.contains("createdAt") && reply.contains("question") && 
        reply.contains("image_id_1") && reply.contains("image_id_2") && reply.contains("image_id_3") &&
        reply.contains("image_id_4") && reply.contains("image_id_5") && reply.contains("image_id_6") &&
        reply.contains("image_id_7") && reply.contains("image_id_8") && reply.contains("image_id_9"));

}

void RegisterCommObj::errorRequest(QNetworkReply::NetworkError code)
{
    if (m_LoadingMessageBox != nullptr && m_LoadingMessageBox->isVisible())
        m_LoadingMessageBox->hide();
    BasisCommObj::errorRequest(code);
}

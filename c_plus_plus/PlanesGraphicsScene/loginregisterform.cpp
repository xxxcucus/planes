#include "loginregisterform.h"

#include <QVBoxLayout>
#include <QHBoxLayout>
#include <QJsonDocument>
#include <QMessageBox>
#include <QTextCodec>
#include <QDebug>
#include "logindata.h"
#include "communicationtools.h"

LoginRegisterForm::LoginRegisterForm(bool login, QNetworkAccessManager* networkManager, QSettings* settings, UserData* userData, GameInfo* gameInfo, QWidget* parent) 
        : QWidget(parent), m_Login(login), m_NetworkManager(networkManager), m_Settings(settings), m_UserData(userData), m_GameInfo(gameInfo) {
    
    m_passwordLineEdit = new QLineEdit();
    m_usernameLineEdit = new QLineEdit();
    m_passwordLineEdit->setEchoMode(QLineEdit::Password);
    
    QFrame* loginRegisterFrame = new QFrame();
    
    
    QString titleText = QString("<b> Register</b>");
    if (m_Login)
        titleText = QString("<b> Login </b>");
    
    m_TitleLabel = new QLabel();
    m_TitleLabel->setText(titleText);
    QLabel* passwordLabel = new QLabel("Password:");
    QLabel* userNameTextLabel = new QLabel("User Name:");
    QPushButton* submitButton = new QPushButton("Submit");
    QString toggleText = m_Login ? "New user ? -> Register" : "Existing user ? -> Login";
    
    m_ToggleLoginRegistrationButton = new QPushButton(toggleText);
    
    QGridLayout* gridLayout1 = new QGridLayout();
    gridLayout1->addWidget(m_TitleLabel);
    gridLayout1->addWidget(passwordLabel, 2, 0);
    gridLayout1->addWidget(m_passwordLineEdit, 2, 1);
    gridLayout1->addWidget(userNameTextLabel, 1, 0);
    gridLayout1->addWidget(m_usernameLineEdit, 1, 1);
    gridLayout1->addWidget(submitButton, 3, 1);
    gridLayout1->addWidget(m_ToggleLoginRegistrationButton, 4, 1);

    loginRegisterFrame->setLayout(gridLayout1);
    loginRegisterFrame->setFrameStyle(QFrame::Panel | QFrame::Raised);

    QHBoxLayout* hLayout = new QHBoxLayout();
    QSpacerItem* spacer1 = new QSpacerItem(10, 10, QSizePolicy::Fixed, QSizePolicy::Fixed);
    hLayout->addWidget(loginRegisterFrame);
    hLayout->addItem(spacer1);
    
    QVBoxLayout* windowLayout = new QVBoxLayout();
    windowLayout->addLayout(hLayout);
    QSpacerItem* spacer = new QSpacerItem(50, 50, QSizePolicy::Expanding, QSizePolicy::Expanding);
    windowLayout->addItem(spacer);
    
    connect(m_ToggleLoginRegistrationButton, &QPushButton::clicked, this, &LoginRegisterForm::toggleLoginRegistration);
    connect(submitButton, &QPushButton::clicked, this, &LoginRegisterForm::submitSlot);
    
    setLayout(windowLayout);
}


void LoginRegisterForm::toggleLoginRegistration()
{
    m_Login = !m_Login;
    QString titleText = QString("<b> Register</b>");
    if (m_Login)
        titleText = QString("<b> Login </b>");
    m_TitleLabel->setText(titleText);
    QString toggleText = m_Login ? "New user ? -> Register" : "Existing user ? -> Login";
    m_ToggleLoginRegistrationButton->setText(toggleText);
    
    m_passwordLineEdit->clear();
    m_usernameLineEdit->clear();
}

void LoginRegisterForm::submitSlot()
{
    if (m_Login)
        submitLogin();
    else
        submitRegistration();
}

void LoginRegisterForm::submitLogin()
{   
    LoginData loginData;
    loginData.m_Password = m_passwordLineEdit->text(); //TODO: validation
    loginData.m_UserName = m_usernameLineEdit->displayText(); //TODO: validation

    if (m_LoginReply != nullptr)
        delete m_LoginReply;

    m_UserData->reset();
    m_UserBeingLoggedIn = loginData.m_UserName;
    m_LoginReply = CommunicationTools::buildPostRequest("/login", m_Settings->value("multiplayer/serverpath").toString(), loginData.toLoginJson(), m_NetworkManager);

    connect(m_LoginReply, &QNetworkReply::finished, this, &LoginRegisterForm::finishedLogin);
    connect(m_LoginReply, &QNetworkReply::errorOccurred, this, &LoginRegisterForm::errorLogin);
}

void LoginRegisterForm::errorLogin(QNetworkReply::NetworkError code)
{
    if (m_GameInfo->getSinglePlayer())
        return;
    CommunicationTools::treatCommunicationError("logging in ", m_LoginReply);
    m_UserData->reset();
}

void LoginRegisterForm::finishedLogin()
{
    if (m_GameInfo->getSinglePlayer())
        return;

    if (m_LoginReply->error() != QNetworkReply::NoError) {
        return;
    }

    QByteArray reply = m_LoginReply->readAll();
    qDebug() << QTextCodec::codecForMib(106)->toUnicode(reply);
    
    QList<QByteArray> headers = m_LoginReply->rawHeaderList();
    bool successfull = false;
    
    for(QByteArray hdr : headers) {
        QString hdrQString = QTextCodec::codecForMib(106)->toUnicode(hdr);
        if (hdrQString == "Authorization") {
            m_UserData->m_AuthToken = m_LoginReply->rawHeader(hdr);
            qDebug() << hdrQString << ":" << m_LoginReply->rawHeader(hdr);
            successfull = true;
        }
    }
    
    if (successfull) {
        QMessageBox msgBox;
        msgBox.setText("Login successfull!"); 
        msgBox.exec();               
        m_UserData->m_UserName = m_UserBeingLoggedIn;
        emit loginCompleted();
    } else {
        QMessageBox msgBox;
        msgBox.setText("Login reply was not recognized"); 
        msgBox.exec();        
    }
}

void LoginRegisterForm::submitRegistration()
{
    LoginData loginData;
    loginData.m_Password = m_passwordLineEdit->displayText(); //TODO: validation
    loginData.m_UserName = m_usernameLineEdit->displayText(); //TODO: validation

    if (m_RegistrationReply != nullptr)
        delete m_RegistrationReply;

    m_RegistrationReply = CommunicationTools::buildPostRequest("/users/registration_request", m_Settings->value("multiplayer/serverpath").toString(), loginData.toRegisterJson(), m_NetworkManager);

    connect(m_RegistrationReply, &QNetworkReply::finished, this, &LoginRegisterForm::finishedRegister);
    connect(m_RegistrationReply, &QNetworkReply::errorOccurred, this, &LoginRegisterForm::errorRegister);
}


void LoginRegisterForm::errorRegister(QNetworkReply::NetworkError code)
{
    if (m_GameInfo->getSinglePlayer())
        return;

    CommunicationTools::treatCommunicationError("registering ", m_RegistrationReply);
}

void LoginRegisterForm::finishedRegister()
{
    if (m_GameInfo->getSinglePlayer())
        return;

    if (m_RegistrationReply->error() != QNetworkReply::NoError) {
        return;
    }
    
    QByteArray reply = m_RegistrationReply->readAll();
    QString registrationReplyQString = QTextCodec::codecForMib(106)->toUnicode(reply);
    QJsonObject registrationReplyJson = CommunicationTools::objectFromString(registrationReplyQString);
 
    if (!validateRegistrationReply(registrationReplyJson)) {
        QMessageBox msgBox;
        msgBox.setText("Registration reply was not recognized"); 
        msgBox.exec();

        return;
    }
    
    std::vector<QString> images;
    
    images.push_back(registrationReplyJson.value("image_id_1").toString());
    images.push_back(registrationReplyJson.value("image_id_2").toString());    
    images.push_back(registrationReplyJson.value("image_id_3").toString());
    images.push_back(registrationReplyJson.value("image_id_4").toString());    
    images.push_back(registrationReplyJson.value("image_id_5").toString());
    images.push_back(registrationReplyJson.value("image_id_6").toString());    
    images.push_back(registrationReplyJson.value("image_id_7").toString());
    images.push_back(registrationReplyJson.value("image_id_8").toString());    
    images.push_back(registrationReplyJson.value("image_id_9").toString());    
    int request_id = registrationReplyJson.value("id").toInt();
    
    qDebug() << "Registration request with id " << request_id << " received ";
    emit noRobotRegistration(images, registrationReplyJson);
}

bool LoginRegisterForm::validateRegistrationReply(const QJsonObject& reply) {
   return (reply.contains("id") && reply.contains("username") && reply.contains("createdAt") && reply.contains("question") && 
        reply.contains("image_id_1") && reply.contains("image_id_2") && reply.contains("image_id_3") &&
        reply.contains("image_id_4") && reply.contains("image_id_5") && reply.contains("image_id_6") &&
        reply.contains("image_id_7") && reply.contains("image_id_8") && reply.contains("image_id_9"));
}

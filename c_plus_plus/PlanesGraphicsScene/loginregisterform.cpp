#include "loginregisterform.h"

#include <QVBoxLayout>
#include <QHBoxLayout>
#include <QJsonDocument>
#include <QMessageBox>
#include <QTextCodec>
#include <QDebug>
#include "logindata.h"

LoginRegisterForm::LoginRegisterForm(bool login, QNetworkAccessManager* networkManager, QSettings* settings, UserData* userData, QWidget* parent) 
        : QWidget(parent), m_Login(login), m_NetworkManager(networkManager), m_Settings(settings), m_UserData(userData) {
    
    m_passwordLineEdit = new QLineEdit();
    m_usernameLineEdit = new QLineEdit();
    
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
    qDebug() << "1" ;
    QString settingsServerPath = m_Settings->value("multiplayer/serverpath").toString();
    qDebug() << settingsServerPath ;
    QString defaultServerPath = "http://localhost:8080";
    QString loginRequestPath = settingsServerPath;
    if (settingsServerPath.isEmpty())
        loginRequestPath = defaultServerPath;
    qDebug() << loginRequestPath ;
    QUrl loginRequestUrl = QUrl(loginRequestPath + "/login"); //TODO: or login without users
    
    LoginData loginData;
    loginData.m_Password = m_passwordLineEdit->displayText(); //TODO: validation
    loginData.m_UserName = m_usernameLineEdit->displayText(); //TODO: validation
        
    QNetworkRequest request(loginRequestUrl);
    request.setRawHeader("Content-Type", "application/fhir+json");
    
    if (m_LoginReply != nullptr)
        delete m_LoginReply;
    qDebug() << "2" ;
    QByteArray data = QJsonDocument(loginData.toJson()).toJson();
    qDebug() << "3";
    m_LoginReply = m_NetworkManager->post(request, data); 
    qDebug() << "4" ;
    connect(m_LoginReply, &QNetworkReply::finished, this, &LoginRegisterForm::finishedLogin);
    connect(m_LoginReply, &QNetworkReply::errorOccurred, this, &LoginRegisterForm::errorLogin);
}

void LoginRegisterForm::errorLogin(QNetworkReply::NetworkError code)
{
    QMessageBox msgBox;
    msgBox.setText("Error when logging  in " + QString::number(code)); //TODO: show error string
    msgBox.exec();
    m_UserData->m_AuthToken = QString();
    m_UserData->m_UserName = QString();
    m_UserData->m_UserPassword = QString();
}

void LoginRegisterForm::finishedLogin()
{
    QByteArray reply = m_LoginReply->readAll();
    qDebug() << QTextCodec::codecForMib(106)->toUnicode(reply);
}



void LoginRegisterForm::submitRegistration()
{
}




#include "loginregisterform.h"

#include <QVBoxLayout>
#include <QHBoxLayout>
#include <QJsonDocument>
#include <QMessageBox>
#include <QTextCodec>
#include <QDebug>
#include "viewmodels/loginviewmodel.h"
#include "communicationtools.h"

LoginRegisterForm::LoginRegisterForm(bool login, QNetworkAccessManager* networkManager, QSettings* settings, GlobalData* globalData, GameInfo* gameInfo, MultiplayerRound* mrd, QWidget* parent) 
        : QFrame(parent), m_Login(login), m_NetworkManager(networkManager), m_Settings(settings), m_GlobalData(globalData), m_GameInfo(gameInfo), m_MultiRound(mrd) {
    
    m_passwordLineEdit = new QLineEdit();
    m_usernameLineEdit = new QLineEdit();
    m_passwordLineEdit->setEchoMode(QLineEdit::Password);
        
    
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

    setLayout(gridLayout1);
    setFrameStyle(QFrame::Panel | QFrame::Raised);
    
    connect(m_ToggleLoginRegistrationButton, &QPushButton::clicked, this, &LoginRegisterForm::toggleLoginRegistration);
    connect(submitButton, &QPushButton::clicked, this, &LoginRegisterForm::submitSlot);
    
    setLayout(gridLayout1);
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
    QString username = m_passwordLineEdit->text().trimmed();
    QString password = m_usernameLineEdit->displayText();

    m_MultiRound->login(username, password);
}

void LoginRegisterForm::submitRegistration()
{
    QString username = m_passwordLineEdit->text().trimmed();
    QString password = m_usernameLineEdit->displayText();

    m_MultiRound->registerUser(username, password);
}


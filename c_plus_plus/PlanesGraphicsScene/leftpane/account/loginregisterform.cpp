#include "loginregisterform.h"

#include <QVBoxLayout>
#include <QHBoxLayout>
#include <QJsonDocument>
#include <QMessageBox>
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
    m_TitleLabel->setFont(QFont("Timer", 20, QFont::Bold));
    m_TitleLabel->setAlignment(Qt::AlignCenter);
    
    QHBoxLayout* hLayout = new QHBoxLayout();
    QSpacerItem* spacer1 = new QSpacerItem(20, 20, QSizePolicy::Expanding, QSizePolicy::Fixed);
    QSpacerItem* spacer2 = new QSpacerItem(20, 20, QSizePolicy::Expanding, QSizePolicy::Fixed);
    
    hLayout->addItem(spacer1);
    hLayout->addWidget(m_TitleLabel);
    hLayout->addItem(spacer2);
    
    QLabel* passwordLabel = new QLabel("Password:");
    QLabel* userNameTextLabel = new QLabel("User Name:");
    QPushButton* submitButton = new QPushButton("Submit");
    QString toggleText = m_Login ? "New user ? -> Register" : "Existing user ? -> Login";
    
    m_ToggleLoginRegistrationButton = new QPushButton(toggleText);
        
    QGridLayout* gridLayout1 = new QGridLayout();
    gridLayout1->addLayout(hLayout, 0, 0, 1, 2, Qt::AlignCenter);
    gridLayout1->addWidget(passwordLabel, 5, 0);
    gridLayout1->addWidget(m_passwordLineEdit, 6, 0, 1, 2);
    gridLayout1->addWidget(userNameTextLabel, 3, 0);
    gridLayout1->addWidget(m_usernameLineEdit, 4, 0, 1, 2);
    gridLayout1->addWidget(submitButton, 7, 1);
    gridLayout1->addWidget(m_ToggleLoginRegistrationButton, 8, 1);

    QHBoxLayout* hLayout1 = new QHBoxLayout();
    QSpacerItem* spacer3 = new QSpacerItem(20, 20, QSizePolicy::Expanding, QSizePolicy::Fixed);
    QSpacerItem* spacer4 = new QSpacerItem(20, 20, QSizePolicy::Expanding, QSizePolicy::Fixed);
    
    hLayout1->addItem(spacer3);
    hLayout1->addLayout(gridLayout1);
    hLayout1->addItem(spacer4);

    
    setLayout(hLayout1);
    setFrameStyle(QFrame::NoFrame);
    
    connect(m_ToggleLoginRegistrationButton, &QPushButton::clicked, this, &LoginRegisterForm::toggleLoginRegistration);
    connect(submitButton, &QPushButton::clicked, this, &LoginRegisterForm::submitSlot);    
    connect(m_MultiRound, &MultiplayerRound::noRobotRegistration, this, &LoginRegisterForm::noRobotRegistrationSlot);
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
    QString password = m_passwordLineEdit->text().trimmed();
    QString username = m_usernameLineEdit->displayText();

    m_MultiRound->login(username, password);
}

void LoginRegisterForm::submitRegistration()
{
    QString password = m_passwordLineEdit->text().trimmed();
    QString username = m_usernameLineEdit->displayText();

    m_MultiRound->registerUser(username, password);
}

void LoginRegisterForm::noRobotRegistrationSlot(const std::vector<QString>& images, const QString& question, long int requestId) {
    
    //qDebug() << "noRobotRegistrationSlot";
    m_NoRobotDialog = new NoRobotDialog(m_NetworkManager, m_Settings, m_GlobalData, m_GameInfo, m_MultiRound);

    m_NoRobotDialog->setImages(images);
    m_NoRobotDialog->setQuestion(question);
    m_NoRobotDialog->setRequestId(QString::number(requestId));

    m_NoRobotDialog->exec();
}

#include "loginregisterform.h"

#include <QVBoxLayout>
#include <QHBoxLayout>

LoginRegisterForm::LoginRegisterForm(bool login, QWidget* parent) : QWidget(parent), m_Login(login) {
    
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
    
    setLayout(windowLayout);
}


void LoginRegisterForm::toggleLoginRegistration()
{
    QString titleText = QString("<b> Register</b>");
    if (m_Login)
        titleText = QString("<b> Login </b>");
    m_TitleLabel->setText(titleText);
    QString toggleText = m_Login ? "New user ? -> Register" : "Existing user ? -> Login";
    m_ToggleLoginRegistrationButton->setText(toggleText);
    
    m_passwordLineEdit->clear();
    m_usernameLineEdit->clear();
    
    m_Login = !m_Login;
}

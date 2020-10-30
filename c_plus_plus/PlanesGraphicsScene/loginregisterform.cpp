#include "loginregisterform.h"

#include <QFormLayout>
#include <QVBoxLayout>
#include <QHBoxLayout>
#include <QPushButton>


LoginRegisterForm::LoginRegisterForm(bool login, QWidget* parent) : QWidget(parent), m_Login(login) {
    
    m_passwordLineEdit = new QLineEdit();
    m_usernameLineEdit = new QLineEdit();

    
    QFrame* loginRegisterFrame = new QFrame();
    
    
    QString titleText = QString("<b> Register</b>");
    if (login)
        titleText = QString("<b> Login </b>");
    
    QLabel* titleLabel = new QLabel();
    titleLabel->setText(titleText);
    QLabel* passwordLabel = new QLabel("Password:");
    QLabel* userNameTextLabel = new QLabel("User Name:");
    QPushButton* submitButton = new QPushButton("Submit");
    
    QGridLayout* gridLayout1 = new QGridLayout();
    gridLayout1->addWidget(titleLabel);
    gridLayout1->addWidget(passwordLabel, 2, 0);
    gridLayout1->addWidget(m_passwordLineEdit, 2, 1);
    gridLayout1->addWidget(userNameTextLabel, 1, 0);
    gridLayout1->addWidget(m_usernameLineEdit, 1, 1);
    gridLayout1->addWidget(submitButton, 3, 1);

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
    
    //(submitButton, &QPushButton::clicked, this, &UserRegistrationSubmitForm::submitClickedSlot);
    
    setLayout(windowLayout);
}

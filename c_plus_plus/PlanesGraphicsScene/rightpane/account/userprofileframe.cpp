#include "userprofileframe.h"
#include <QGridLayout>


UserProfileFrame::UserProfileFrame(GlobalData* globalData, QWidget* parent): QFrame(parent), m_GlobalData(globalData)
{
    QString titleText = QString("<b> User Profile</b>");
    QLabel* titleLabel = new QLabel();
    titleLabel->setText(titleText);
    m_UserNameTextLabel = new QLabel("");
    m_UserNameLabel = new QLabel("");
    m_UserNotLoggedInLabel = new QLabel("No user logged in");
    QGridLayout* gridLayout1 = new QGridLayout();
    gridLayout1->addWidget(titleLabel);
    gridLayout1->addWidget(m_UserNotLoggedInLabel, 1, 0, 1, 2);
    gridLayout1->addWidget(m_UserNameTextLabel, 2, 0);
    gridLayout1->addWidget(m_UserNameLabel, 2, 1);
    setLayout(gridLayout1);
    setFrameStyle(QFrame::Panel | QFrame::Raised);
}

void UserProfileFrame::loginCompleted()
{
    m_UserNotLoggedInLabel->setText("");
    m_UserNameLabel->setText(m_GlobalData->m_UserData.m_UserName);
    m_UserNameTextLabel->setText("User name: ");
}

void UserProfileFrame::loginFailed()
{
    m_UserNotLoggedInLabel->setText("No User Logged in");
    m_UserNameLabel->setText("");
    m_UserNameTextLabel->setText("");
}

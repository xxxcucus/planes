#include "userprofileframe.h"
#include <QGridLayout>


UserProfileFrame::UserProfileFrame(QWidget* parent)
{
    QString titleText = QString("<b> User Profile</b>");
    QLabel* titleLabel = new QLabel();
    titleLabel->setText(titleText);
    QLabel* userNameTextLabel = new QLabel("User Name:");
    QGridLayout* gridLayout1 = new QGridLayout();
    gridLayout1->addWidget(titleLabel);
    gridLayout1->addWidget(m_UserNotLoggedInLabel, 1, 0, 1, 2);
    gridLayout1->addWidget(userNameTextLabel, 2, 0);
    gridLayout1->addWidget(m_UserNameLabel, 2, 1);
    setLayout(gridLayout1);
    setFrameStyle(QFrame::Panel | QFrame::Raised);
}


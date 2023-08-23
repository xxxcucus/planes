#include "userprofileform.h"

#include <QGridLayout>

UserProfileForm::UserProfileForm(QWidget* parent): QFrame(parent) {
    QString titleText = QString("<b> User Profile</b>");
    QLabel* titleLabel = new QLabel();
    titleLabel->setText(titleText);
    titleLabel->setFont(QFont("Timer", 20, QFont::Bold));

    m_UserNameTextLabel = new QLabel("");
    m_UserNameLabel = new QLabel("");
    m_UserNotLoggedInLabel = new QLabel("No user logged in");
    m_DeactivateUserButton = new QPushButton("Delete user");
    QGridLayout* gridLayout1 = new QGridLayout();
    gridLayout1->addWidget(titleLabel, 0, 0, 1, 2);
    gridLayout1->addWidget(m_UserNotLoggedInLabel, 1, 0, 1, 2);
    gridLayout1->addWidget(m_UserNameTextLabel, 2, 0);
    gridLayout1->addWidget(m_UserNameLabel, 2, 1);
    gridLayout1->addWidget(m_DeactivateUserButton, 3, 1);
    setLayout(gridLayout1);

    connect(m_DeactivateUserButton, &QPushButton::clicked, this, &UserProfileForm::deactivatedClicked);
}

void UserProfileForm::setUsername(const QString& username) {
    if (username.isEmpty()) {
        m_UserNotLoggedInLabel->setText("No user logged in");
        m_UserNameTextLabel->setText("");
        m_UserNameLabel->setText("");
        m_DeactivateUserButton->setEnabled(false);
    } else {
        m_UserNotLoggedInLabel->setText("");
        m_UserNameTextLabel->setText("Username");
        m_UserNameLabel->setText(username);
        m_DeactivateUserButton->setEnabled(true);
    }

}

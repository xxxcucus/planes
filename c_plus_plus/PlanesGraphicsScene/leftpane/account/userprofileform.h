#ifndef __USER_PROFILE__FORM_
#define __USER_PROFILE__FORM_

#include <QObject>
#include <QLabel>
#include <QPushButton>

class UserProfileForm : public QFrame
{
    Q_OBJECT
public:
    UserProfileForm(QWidget *parent = nullptr);
    void setUsername(const QString& username);

signals:
    void deactivatedClicked();

private:
    QLabel* m_UserNameLabel;
    QLabel* m_UserNameTextLabel;
    QLabel* m_UserNotLoggedInLabel;
    QPushButton* m_DeactivateUserButton;
};










#endif


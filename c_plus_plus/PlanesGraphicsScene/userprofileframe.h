#ifndef __USERPROFILE_FRAME__
#define __USERPROFILE_FRAME__

#include <QLabel>

/**
 * User data for the user which is logged in
 **/ 
class UserProfileFrame : public QFrame
{
    Q_OBJECT

public:
    explicit UserProfileFrame(QWidget* parent = nullptr);

public:
    QLabel* m_UserNameLabel = new QLabel("");
    QLabel* m_UserNotLoggedInLabel = new QLabel("No User Logged in");
    bool m_UserLoggedIn = false;
};











#endif

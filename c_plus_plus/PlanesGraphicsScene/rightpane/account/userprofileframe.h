#ifndef __USERPROFILE_FRAME__
#define __USERPROFILE_FRAME__

#include <QLabel>
#include "global/globaldata.h"

/**
 * User data for the user which is logged in
 **/ 
class UserProfileFrame : public QFrame
{
    Q_OBJECT

public:
    explicit UserProfileFrame(GlobalData* globalData, QWidget* parent = nullptr);
    
public slots:
    void loginCompleted();
    void loginFailed();

private:
    QLabel* m_UserNameLabel;
    QLabel* m_UserNameTextLabel;
    QLabel* m_UserNotLoggedInLabel;
    GlobalData* m_GlobalData;
};











#endif

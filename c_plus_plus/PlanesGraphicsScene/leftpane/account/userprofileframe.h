#ifndef __USERPROFILE_FRAME__
#define __USERPROFILE_FRAME__

#include <QLabel>
#include "global/globaldata.h"
#include "multiplayerround.h"
#include "userprofileform.h"
/**
 * User data for the user which is logged in
 **/ 
class UserProfileFrame : public QFrame
{
    Q_OBJECT

public:
    explicit UserProfileFrame(GlobalData* globalData, MultiplayerRound* mrd, QWidget* parent = nullptr);
    
private slots:
    void loginCompleted();
    void logoutCompleted();
    void deactivateUser();

private:
    GlobalData* m_GlobalData;
    UserProfileForm* m_UserProfileForm;
    MultiplayerRound* m_MultiRound;
};

#endif

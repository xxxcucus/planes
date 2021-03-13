#ifndef __LOGIN_REGISTER__FORM_
#define __LOGIN_REGISTER__FORM_

#include <QLineEdit>
#include <QLabel>
#include <QWidget>
#include <QPushButton>
#include <QNetworkAccessManager>
#include <QSettings>
#include <QNetworkReply>
#include <QJsonObject>

#include "global/globaldata.h"
#include "gameinfo.h"
#include "multiplayerround.h"

class LoginRegisterForm : public QFrame
{
    Q_OBJECT
public:
    LoginRegisterForm(bool login, QNetworkAccessManager* networkManager, QSettings* settings, GlobalData* globalData, GameInfo* gameInfo, MultiplayerRound* mrd, QWidget *parent = nullptr);
    
private slots:
    void toggleLoginRegistration();
    void submitSlot();
    void submitLogin();
    void submitRegistration();
    
private:
    QLineEdit* m_passwordLineEdit = nullptr;
    QLineEdit* m_usernameLineEdit = nullptr;
    bool m_Login = false;
    
    QLabel* m_TitleLabel = nullptr;
    QPushButton* m_ToggleLoginRegistrationButton = nullptr;
    QNetworkAccessManager* m_NetworkManager;
    QSettings* m_Settings;
    GlobalData* m_GlobalData;
    GameInfo* m_GameInfo;
    MultiplayerRound* m_MultiRound;    
};










#endif

#ifndef __LOGIN_REGISTER__FORM_
#define __LOGIN_REGISTER__FORM_

#include <QLineEdit>
#include <QLabel>
#include <QWidget>
#include <QPushButton>
#include <QNetworkAccessManager>
#include <QSettings>
#include <QNetworkReply>

#include "userdata.h"

class LoginRegisterForm : public QWidget
{
    Q_OBJECT
public:
    explicit LoginRegisterForm(bool login, QNetworkAccessManager* networkManager, QSettings* settings, UserData* userData, QWidget *parent = nullptr);

private slots:
    void toggleLoginRegistration();
    void submitSlot();
    
private:
    void submitLogin();
    void submitRegistration();
    void errorLogin(QNetworkReply::NetworkError code);
    void finishedLogin();
    void errorRegister(QNetworkReply::NetworkError code);
    void finishedRegister();

    
private:
    QLineEdit* m_passwordLineEdit = nullptr;
    QLineEdit* m_usernameLineEdit = nullptr;
    bool m_Login = false;
    
    QLabel* m_TitleLabel = nullptr;
    QPushButton* m_ToggleLoginRegistrationButton = nullptr;
    QNetworkAccessManager* m_NetworkManager;
    QSettings* m_Settings;
    UserData* m_UserData;
    
    QNetworkReply* m_LoginReply = nullptr;
    QNetworkReply* m_RegistrationReply = nullptr;
};










#endif

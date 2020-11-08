#ifndef __LOGIN_REGISTER__FORM_
#define __LOGIN_REGISTER__FORM_

#include <QLineEdit>
#include <QLabel>
#include <QWidget>
#include <QPushButton>
#include <QNetworkAccessManager>
#include <QSettings>

class LoginRegisterForm : public QWidget
{
    Q_OBJECT
public:
    explicit LoginRegisterForm(bool login, QNetworkAccessManager* networkManager, QSettings* settings, QWidget *parent = nullptr);

private slots:
    void toggleLoginRegistration();
    void submitSlot();
    
private:
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
};










#endif

#ifndef __LOGIN_REGISTER__FORM_
#define __LOGIN_REGISTER__FORM_

#include <QLineEdit>
#include <QLabel>
#include <QWidget>
#include <QPushButton>

class LoginRegisterForm : public QWidget
{
    Q_OBJECT
public:
    explicit LoginRegisterForm(bool login, QWidget *parent = nullptr);

private slots:
    void toggleLoginRegistration();
    
private:
    QLineEdit* m_passwordLineEdit = nullptr;
    QLineEdit* m_usernameLineEdit = nullptr;
    bool m_Login = false;
    
    QLabel* m_TitleLabel = nullptr;
    QPushButton* m_ToggleLoginRegistrationButton = nullptr;
};










#endif

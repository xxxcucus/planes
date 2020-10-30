#ifndef _LOGIN_REGISTER_STACKED_WIDGET__
#define _LOGIN_REGISTER_STACKED_WIDGET__

#include <QStackedWidget>
#include "loginregisterform.h"

/**
 * Stacked widget between a login register form and
 * a no robot test widget
 * **/

class LoginRegisterStackedWidget : public QStackedWidget {
    
public:    
LoginRegisterStackedWidget(QWidget* parent = nullptr);



private:
    LoginRegisterForm* m_LoginForm;
    LoginRegisterForm* m_RegisterForm;


};


#endif

#include "loginregisterstackedwidget.h"


LoginRegisterStackedWidget::LoginRegisterStackedWidget(QWidget* parent) : QStackedWidget(parent)
{
    m_LoginForm = new LoginRegisterForm(true);
    addWidget(m_LoginForm);
    m_RegisterForm = new LoginRegisterForm(false);
    addWidget(m_RegisterForm);
}

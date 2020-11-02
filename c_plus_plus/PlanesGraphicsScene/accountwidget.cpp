#include "accountwidget.h"


AccountWidget::AccountWidget(UserData* userData, QWidget* parent) : QStackedWidget(parent), m_UserData(userData)
{
    m_MainAccountWidget = new MainAccountWidget(m_UserData);
    addWidget(m_MainAccountWidget);
    m_NoRobotWidget = new NoRobotWidget();
    addWidget(m_NoRobotWidget);
    setCurrentIndex(0);
}

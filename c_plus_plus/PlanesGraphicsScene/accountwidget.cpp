#include "accountwidget.h"


AccountWidget::AccountWidget(UserData* userData, QNetworkAccessManager* networkManager, QWidget* parent) : QStackedWidget(parent), m_UserData(userData), m_NetworkManager(networkManager)
{
    m_MainAccountWidget = new MainAccountWidget(m_UserData, m_NetworkManager);
    addWidget(m_MainAccountWidget);
    m_NoRobotWidget = new NoRobotWidget();
    addWidget(m_NoRobotWidget);
    setCurrentIndex(0);
}

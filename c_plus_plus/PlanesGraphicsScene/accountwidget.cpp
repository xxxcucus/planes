#include "accountwidget.h"


AccountWidget::AccountWidget(QSettings* settings, UserData* userData, QNetworkAccessManager* networkManager, QWidget* parent) 
    : QStackedWidget(parent), m_UserData(userData), m_NetworkManager(networkManager), m_Settings(settings)
{
    m_MainAccountWidget = new MainAccountWidget(m_Settings, m_UserData, m_NetworkManager);
    addWidget(m_MainAccountWidget);
    m_NoRobotWidget = new NoRobotWidget();
    addWidget(m_NoRobotWidget);
    setCurrentIndex(0);
}

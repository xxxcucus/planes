#include "accountwidget.h"


AccountWidget::AccountWidget(QWidget* parent) : QStackedWidget(parent)
{
    m_MainAccountWidget = new MainAccountWidget();
    addWidget(m_MainAccountWidget);
    m_NoRobotWidget = new NoRobotWidget();
    addWidget(m_NoRobotWidget);
    setCurrentIndex(0);
}

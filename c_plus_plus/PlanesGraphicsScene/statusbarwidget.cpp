#include "statusbarwidget.h"

#include <QVBoxLayout>


StatusBarWidget::StatusBarWidget(GameInfo* gameInfo, GlobalData* globalData, QWidget* parent) : 
    QWidget(parent), m_GlobalData(globalData), m_GameInfo(gameInfo)  {

    QVBoxLayout* vLayout1 = new QVBoxLayout();
    m_StatusLabel = new QLabel();
    if (m_GameInfo->getSinglePlayer())
        m_StatusLabel->setText("Single Player Game");
    else
        m_StatusLabel->setText("Multi-Player Game");
    vLayout1->addWidget(m_StatusLabel);
    setLayout(vLayout1);
}

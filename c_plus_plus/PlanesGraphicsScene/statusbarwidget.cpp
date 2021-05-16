#include "statusbarwidget.h"

#include <QVBoxLayout>


StatusBarWidget::StatusBarWidget(GameInfo* gameInfo, GlobalData* globalData, QWidget* parent) : 
    QWidget(parent), m_GlobalData(globalData), m_GameInfo(gameInfo)  {

    QVBoxLayout* vLayout1 = new QVBoxLayout();
    m_StatusLabel = new QLabel();
    vLayout1->addWidget(m_StatusLabel);
    setLayout(vLayout1);
    updateSlot();
}

void StatusBarWidget::updateSlot()
{
    if (m_GameInfo->getSinglePlayer()) {
        m_StatusLabel->setText("Single Player Game");
    } else {
        QString statusText("Multi-Player Game");
        
        if (!m_GlobalData->m_UserData.m_UserName.trimmed().isEmpty()) {
            statusText += " - Username: " + m_GlobalData->m_UserData.m_UserName;
        } else if (m_GlobalData->m_UserData.m_UserId != 0) {
            statusText += " - User id: " + QString::number(m_GlobalData->m_UserData.m_UserId);
        } else {
            statusText += " - Not logged in";
        }
        
        if (m_GlobalData->m_GameData.m_GameId != 0) 
            statusText += " - Game name: " + m_GlobalData->m_GameData.m_GameName;
        else
            statusText += " - Not connected to a game ";
        
        if (m_GlobalData->m_GameData.m_OtherUserId != 0 && m_GlobalData->m_GameData.m_OtherUserId != m_GlobalData->m_UserData.m_UserId)
            statusText += " - Opponent : " + m_GlobalData->m_GameData.m_OtherUsername;
        
        if (m_GlobalData->m_GameData.m_RoundId != 0)
            statusText += " - Round id " + QString::number(m_GlobalData->m_GameData.m_RoundId);
        
        m_StatusLabel->setText(statusText);
    }
}


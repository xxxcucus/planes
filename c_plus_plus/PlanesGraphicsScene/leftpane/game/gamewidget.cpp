#include "gamewidget.h"

#include <QVBoxLayout>
#include <QHBoxLayout>
#include <QPushButton>
#include <QMessageBox>

#include "customhorizlayout.h"
#include "gamestatuswidget.h"
#include "creategamewidget.h"
#include "gameendwidget.h"

GameWidget::GameWidget(GlobalData* globalData, MultiplayerRound* mrd, QWidget* parent)
    : QFrame(parent), m_GlobalData(globalData), m_MultiRound(mrd) {

    m_CreateGameWidget = new CreateGameWidget(m_MultiRound);
    QHBoxLayout* hLayout = new QHBoxLayout();
    QSpacerItem* spacer1 = new QSpacerItem(50, 50, QSizePolicy::Expanding, QSizePolicy::Fixed);
    QPushButton* toGameButton = new QPushButton("Start playing");
    hLayout->addItem(spacer1);
    hLayout->addWidget(toGameButton);
    QSpacerItem* spacer = new QSpacerItem(50, 50, QSizePolicy::Expanding, QSizePolicy::Expanding);
    QSpacerItem* spacer2 = new QSpacerItem(50, 50, QSizePolicy::Expanding, QSizePolicy::Expanding);    
    QVBoxLayout* vLayout = new QVBoxLayout();
    vLayout->addItem(spacer2);
    vLayout->addWidget(m_CreateGameWidget);
    vLayout->addItem(spacer);
    vLayout->addLayout(hLayout);
    
    setLayout(vLayout);

    connect(m_MultiRound, &MultiplayerRound::gameCreated, this, &GameWidget::periodicallyRefreshStatusSlot);
    connect(toGameButton, &QPushButton::clicked, this, &GameWidget::toGameButtonClickedSlot);
    connect(m_CreateGameWidget, &CreateGameWidget::connectToGameCalled, this, &GameWidget::connectToGameSlot);
    
    m_RefreshStatusTimer = new QTimer(this);
    connect(m_RefreshStatusTimer, &QTimer::timeout, this, &GameWidget::refreshStatusWithTimer);
        
}

void GameWidget::toGameButtonClickedSlot(bool value)
{
    if (m_GlobalData->m_GameData.m_GameId == 0 || m_GlobalData->m_GameData.m_OtherUserId == 0 || m_GlobalData->m_GameData.m_UserId == 0
        || m_GlobalData->m_GameData.m_RoundId  == 0) { 
            QMessageBox msgBox;
            msgBox.setText("Round was not yet initialized.\nTry acquiring all round data by clicking on the \'Refresh\' button"); 
            msgBox.exec();
            return;
    }

    emit toGameButtonClicked(value);
}

void GameWidget::periodicallyRefreshStatusSlot()
{
    m_RefreshStatusTimer->start(5000);
}

void GameWidget::refreshStatusWithTimer()
{
    if (m_GlobalData->m_GameData.m_GameId != 0 && m_GlobalData->m_GameData.m_RoundId != 0
        && m_GlobalData->m_GameData.m_UserId != 0 && m_GlobalData->m_GameData.m_OtherUserId != 0) {
        m_RefreshStatusTimer->stop();
        return;
    }
    
    m_MultiRound->refreshGameStatus(m_CreateGameWidget->getGameName()); //TODO: when the game name is changed manually stop timer
}

void GameWidget::stopRefreshStatusTimer()
{
    if (m_RefreshStatusTimer != nullptr)
        m_RefreshStatusTimer->stop();
}

void GameWidget::currentTabChanged()
{
    stopRefreshStatusTimer();
}

void GameWidget::connectToGameSlot()
{
    stopRefreshStatusTimer();
}

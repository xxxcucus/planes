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

    GameStatusWidget* gameStatusWidget = new GameStatusWidget(m_MultiRound);
    CreateGameWidget* createGameWidget = new CreateGameWidget(m_MultiRound);
    QHBoxLayout* hLayout = new QHBoxLayout();
    QSpacerItem* spacer1 = new QSpacerItem(50, 50, QSizePolicy::Expanding, QSizePolicy::Fixed);
    QPushButton* toGameButton = new QPushButton("Start playing");
    hLayout->addItem(spacer1);
    hLayout->addWidget(toGameButton);
    QSpacerItem* spacer = new QSpacerItem(50, 50, QSizePolicy::Expanding, QSizePolicy::Expanding);
    QVBoxLayout* vLayout = new QVBoxLayout();
    vLayout->addWidget(gameStatusWidget);
    vLayout->addWidget(createGameWidget);
    vLayout->addItem(spacer);
    vLayout->addLayout(hLayout);
    
    setLayout(vLayout);

    connect(m_MultiRound, &MultiplayerRound::gameCreated, gameStatusWidget, &GameStatusWidget::gameCreatedSlot);
    connect(m_MultiRound, &MultiplayerRound::gameConnectedTo, gameStatusWidget, &GameStatusWidget::gameConnectedToSlot);   
    connect(toGameButton, &QPushButton::clicked, this, &GameWidget::toGameButtonClickedSlot);
        
}

void GameWidget::toGameButtonClickedSlot(bool value)
{
    if (m_GlobalData->m_GameData.m_GameId == 0 || m_GlobalData->m_GameData.m_OtherUserId == 0 || m_GlobalData->m_GameData.m_UserId == 0
        || m_GlobalData->m_GameData.m_RoundId  == 0) {   //TODO: this should work also after a new game is created
            QMessageBox msgBox;
            msgBox.setText("Round was not yet initialized.\nTry acquiring all round data by clicking on the \'Refresh\' button"); 
            msgBox.exec();
            return;
    }

    emit toGameButtonClicked(value);
}

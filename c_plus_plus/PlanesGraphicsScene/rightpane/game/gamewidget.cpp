#include "gamewidget.h"

#include <QVBoxLayout>
#include <QHBoxLayout>

#include "customhorizlayout.h"
#include "gamestatuswidget.h"
#include "creategamewidget.h"
#include "gameendwidget.h"

GameWidget::GameWidget(GlobalData* globalData, GameInfo* gameInfo, QNetworkAccessManager* networkManager, QSettings* settings, MultiplayerRound* mrd, QWidget* parent)
    : QWidget(parent), m_GlobalData(globalData), m_GameInfo(gameInfo), m_NetworkManager(networkManager), m_Settings(settings), m_MultiRound(mrd) {

    CustomHorizLayout* cLayout = new CustomHorizLayout(50);
    
    QWidget* leftPane = new QWidget();
    QVBoxLayout* vLayout = new QVBoxLayout();
    GameStatusWidget* gameStatusWidget = new GameStatusWidget(m_MultiRound);
    vLayout->addWidget(gameStatusWidget);
    QSpacerItem* spacer = new QSpacerItem(50, 50, QSizePolicy::Expanding, QSizePolicy::Expanding);
    vLayout->addItem(spacer);
    leftPane->setLayout(vLayout);
    
    QWidget* rightContent = new QWidget();
    QVBoxLayout* vLayout1 = new QVBoxLayout();
    CreateGameWidget* createGameWidget = new CreateGameWidget(m_MultiRound);
    GameEndWidget* gameEndWidget = new GameEndWidget();
    QSpacerItem* spacer1 = new QSpacerItem(50, 50, QSizePolicy::Expanding, QSizePolicy::Expanding);
    vLayout1->addWidget(createGameWidget);
    vLayout1->addWidget(gameEndWidget);
    vLayout1->addItem(spacer1);
    rightContent->setLayout(vLayout1);
    
    QWidget* rightPane = new QWidget();
    QHBoxLayout* hLayout = new QHBoxLayout();
    QSpacerItem* spacer2 = new QSpacerItem(10, 10, QSizePolicy::Fixed, QSizePolicy::Fixed);
    hLayout->addWidget(rightContent);
    hLayout->addItem(spacer2);
    rightPane->setLayout(hLayout);
    
    cLayout->addWidget(leftPane);
    cLayout->addWidget(rightPane);
    setLayout(cLayout);

    connect(m_MultiRound, &MultiplayerRound::gameCreated, gameStatusWidget, &GameStatusWidget::gameCreatedSlot);
    connect(m_MultiRound, &MultiplayerRound::gameConnectedTo, gameStatusWidget, &GameStatusWidget::gameConnectedToSlot);   
}

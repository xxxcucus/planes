#include "gamewidget.h"

#include <QVBoxLayout>
#include <QHBoxLayout>

#include "customhorizlayout.h"
#include "gamestatuswidget.h"
#include "creategamewidget.h"
#include "gameendwidget.h"

GameWidget::GameWidget(UserData* userData, GameInfo* gameInfo, QNetworkAccessManager* networkManager, QSettings* settings, QWidget* parent)
    : QWidget(parent), m_UserData(userData), m_GameInfo(gameInfo), m_NetworkManager(networkManager), m_Settings(settings) {

    CustomHorizLayout* cLayout = new CustomHorizLayout(50);
    
    QWidget* leftPane = new QWidget();
    QVBoxLayout* vLayout = new QVBoxLayout();
    GameStatusWidget* gameStatusWidget = new GameStatusWidget();
    vLayout->addWidget(gameStatusWidget);
    QSpacerItem* spacer = new QSpacerItem(50, 50, QSizePolicy::Expanding, QSizePolicy::Expanding);
    vLayout->addItem(spacer);
    leftPane->setLayout(vLayout);
    
    QWidget* rightContent = new QWidget();
    QVBoxLayout* vLayout1 = new QVBoxLayout();
    CreateGameWidget* createGameWidget = new CreateGameWidget(m_UserData, m_GameInfo, m_NetworkManager, m_Settings);
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

    connect(createGameWidget, &CreateGameWidget::gameCreated, gameStatusWidget, &GameStatusWidget::gameCreatedSlot);
    connect(createGameWidget, &CreateGameWidget::gameConnectedTo, gameStatusWidget, &GameStatusWidget::gameConnectedToSlot);        
}

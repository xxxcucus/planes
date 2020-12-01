#include "gamewidget.h"

#include <QVBoxLayout>
#include <QHBoxLayout>

#include "customhorizlayout.h"
#include "gamestatuswidget.h"
#include "creategamewidget.h"
#include "gameendwidget.h"

GameWidget::GameWidget(GameInfo* gameInfo, QWidget* parent) : QWidget(parent), m_GameInfo(gameInfo) {

    CustomHorizLayout* cLayout = new CustomHorizLayout(50);
    
    QWidget* leftPane = new QWidget();
    QVBoxLayout* vLayout = new QVBoxLayout();
    GameStatusWidget* userProfileFrame = new GameStatusWidget();
    vLayout->addWidget(userProfileFrame);
    QSpacerItem* spacer = new QSpacerItem(50, 50, QSizePolicy::Expanding, QSizePolicy::Expanding);
    vLayout->addItem(spacer);
    leftPane->setLayout(vLayout);
    
    QWidget* rightContent = new QWidget();
    QVBoxLayout* vLayout1 = new QVBoxLayout();
    CreateGameWidget* createGameWidget = new CreateGameWidget();
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
    
}

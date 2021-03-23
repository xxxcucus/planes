#include "mainaccountwidget.h"

#include <QVBoxLayout>
#include <QHBoxLayout>
#include <QMessageBox>

#include "customhorizlayout.h"
#include "multiplayerround.h"
#include "userprofileframe.h"
#include "loginregisterform.h"


MainAccountWidget::MainAccountWidget(QSettings* settings, GlobalData* globalData, QNetworkAccessManager* networkManager, GameInfo* gameInfo, MultiplayerRound* mrd, QWidget* parent) 
    : QWidget(parent), m_GlobalData(globalData), m_NetworkManager(networkManager), m_Settings(settings), m_GameInfo(gameInfo), m_MultiRound(mrd)
{    
    QVBoxLayout* vLayout = new QVBoxLayout();
    m_userProfileFrame = new UserProfileFrame(m_GlobalData, m_MultiRound);
    m_loginRegisterForm = new LoginRegisterForm(true, m_NetworkManager, m_Settings, m_GlobalData, m_GameInfo, m_MultiRound);
    
    QHBoxLayout* hLayout = new QHBoxLayout();
    QSpacerItem* spacer1 = new QSpacerItem(50, 50, QSizePolicy::Expanding, QSizePolicy::Fixed);
    QPushButton* toGameCreationButton = new QPushButton("Join or Create a Game");
    hLayout->addItem(spacer1);
    hLayout->addWidget(toGameCreationButton);
    QSpacerItem* spacer = new QSpacerItem(50, 50, QSizePolicy::Expanding, QSizePolicy::Expanding);
    vLayout->addWidget(m_userProfileFrame);
    vLayout->addWidget(m_loginRegisterForm);
    vLayout->addItem(spacer);
    vLayout->addLayout(hLayout);
    setLayout(vLayout);  
    
    connect(toGameCreationButton, &QPushButton::clicked, this, &MainAccountWidget::toGameCreationClickedSlot);
}

void MainAccountWidget::toGameCreationClickedSlot(bool value) {
        
    if (m_GlobalData->m_UserData.m_UserName.isEmpty()) { 
            QMessageBox msgBox;
            msgBox.setText("Please login to game server first"); 
            msgBox.exec();
            return;
    }
    
    emit toGameCreationClicked(value);
}

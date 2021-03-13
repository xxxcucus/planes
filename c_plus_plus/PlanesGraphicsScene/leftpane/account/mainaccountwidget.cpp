#include "mainaccountwidget.h"

#include <QVBoxLayout>
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
    
    QSpacerItem* spacer = new QSpacerItem(50, 50, QSizePolicy::Expanding, QSizePolicy::Expanding);
    vLayout->addWidget(m_userProfileFrame);
    vLayout->addWidget(m_loginRegisterForm);

    vLayout->addItem(spacer);
    
    setLayout(vLayout);  
}

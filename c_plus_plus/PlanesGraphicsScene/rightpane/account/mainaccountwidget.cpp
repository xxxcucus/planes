#include "mainaccountwidget.h"

#include <QFrame>
#include <QHBoxLayout>
#include "customhorizlayout.h"
#include "userprofileframe.h"
#include "loginregisterform.h"
#include "multiplayerround.h"


MainAccountWidget::MainAccountWidget(QSettings* settings, GlobalData* globalData, QNetworkAccessManager* networkManager, GameInfo* gameInfo, MultiplayerRound* mrd, QWidget* parent) 
    : QWidget(parent), m_GlobalData(globalData), m_NetworkManager(networkManager), m_Settings(settings), m_GameInfo(gameInfo), m_MultiRound(mrd)
{
    CustomHorizLayout* cLayout = new CustomHorizLayout(50);
    
    QWidget* leftPane = new QWidget();
    QVBoxLayout* vLayout = new QVBoxLayout();
    UserProfileFrame* userProfileFrame = new UserProfileFrame(m_GlobalData, m_MultiRound);
    vLayout->addWidget(userProfileFrame);
    QSpacerItem* spacer = new QSpacerItem(50, 50, QSizePolicy::Expanding, QSizePolicy::Expanding);
    vLayout->addItem(spacer);
    leftPane->setLayout(vLayout);
    LoginRegisterForm* loginRegisterForm = new LoginRegisterForm(true, m_NetworkManager, m_Settings, m_GlobalData, m_GameInfo, m_MultiRound);
    cLayout->addWidget(leftPane);
    cLayout->addWidget(loginRegisterForm);
    setLayout(cLayout);    
}

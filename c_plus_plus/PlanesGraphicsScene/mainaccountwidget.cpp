#include "mainaccountwidget.h"

#include <QFrame>
#include <QHBoxLayout>
#include "customhorizlayout.h"
#include "userprofileframe.h"
#include "loginregisterform.h"


MainAccountWidget::MainAccountWidget(QSettings* settings, UserData* userData, QNetworkAccessManager* networkManager, GameInfo* gameInfo, QWidget* parent) 
    : QWidget(parent), m_UserData(userData), m_NetworkManager(networkManager), m_Settings(settings), m_GameInfo(gameInfo)
{
    CustomHorizLayout* cLayout = new CustomHorizLayout(50);
    
    QWidget* leftPane = new QWidget();
    QVBoxLayout* vLayout = new QVBoxLayout();
    UserProfileFrame* userProfileFrame = new UserProfileFrame(m_UserData);
    vLayout->addWidget(userProfileFrame);
    QSpacerItem* spacer = new QSpacerItem(50, 50, QSizePolicy::Expanding, QSizePolicy::Expanding);
    vLayout->addItem(spacer);
    leftPane->setLayout(vLayout);
    LoginRegisterForm* loginRegisterForm = new LoginRegisterForm(true, m_NetworkManager, m_Settings, m_UserData, m_GameInfo);
    cLayout->addWidget(leftPane);
    cLayout->addWidget(loginRegisterForm);
    setLayout(cLayout);
    
    connect(loginRegisterForm, &LoginRegisterForm::noRobotRegistration, this, &MainAccountWidget::noRobotRegistration);
    connect(loginRegisterForm, &LoginRegisterForm::loginCompleted, userProfileFrame, &UserProfileFrame::loginCompleted);
    
}

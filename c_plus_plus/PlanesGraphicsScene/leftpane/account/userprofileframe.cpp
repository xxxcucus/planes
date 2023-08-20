#include "userprofileframe.h"
#include <QGridLayout>


UserProfileFrame::UserProfileFrame(GlobalData* globalData, MultiplayerRound* mrd, QWidget* parent): QFrame(parent), m_GlobalData(globalData), m_MultiRound(mrd)
{
    QVBoxLayout* vLayout = new QVBoxLayout();
    //m_userProfileFrame = new UserProfileFrame(m_GlobalData, m_MultiRound);
    m_UserProfileForm = new UserProfileForm();

    QSpacerItem* spacer = new QSpacerItem(50, 50, QSizePolicy::Expanding, QSizePolicy::Expanding);
    QSpacerItem* spacer2 = new QSpacerItem(50, 50, QSizePolicy::Expanding, QSizePolicy::Expanding);

    QSpacerItem* spacer1 = new QSpacerItem(50, 50, QSizePolicy::Expanding, QSizePolicy::Expanding);
    QSpacerItem* spacer3 = new QSpacerItem(50, 50, QSizePolicy::Expanding, QSizePolicy::Expanding);


    //vLayout->addWidget(m_userProfileFrame);
    vLayout->addItem(spacer2);
    vLayout->addWidget(m_UserProfileForm);
    vLayout->addItem(spacer);

    QHBoxLayout* hLayout = new QHBoxLayout();
    hLayout->addItem(spacer1);
    hLayout->addLayout(vLayout);
    hLayout->addItem(spacer3);

    setLayout(hLayout);

    connect(m_MultiRound, &MultiplayerRound::loginCompleted, this, &UserProfileFrame::loginCompleted);
    connect(m_MultiRound, &MultiplayerRound::logoutCompleted, this, &UserProfileFrame::logoutCompleted);
}

void UserProfileFrame::loginCompleted() {
    m_UserProfileForm->setUsername(m_GlobalData->m_UserData.m_UserName);
}

void UserProfileFrame::logoutCompleted() {
    m_UserProfileForm->setUsername("");
}

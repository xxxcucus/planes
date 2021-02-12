#include "accountwidget.h"



AccountWidget::AccountWidget(QSettings* settings, GlobalData* globalData, QNetworkAccessManager* networkManager, GameInfo* gameInfo, MultiplayerRound* mrd, QWidget* parent) 
    : QStackedWidget(parent), m_GlobalData(globalData), m_NetworkManager(networkManager), m_Settings(settings), m_GameInfo(gameInfo), m_MultiRound(mrd)
{
    m_MainAccountWidget = new MainAccountWidget(m_Settings, m_GlobalData, m_NetworkManager, m_GameInfo, m_MultiRound);
    addWidget(m_MainAccountWidget);
    m_NoRobotWidget = new NoRobotWidget(m_NetworkManager, m_Settings, m_GlobalData, m_GameInfo, m_MultiRound);
    addWidget(m_NoRobotWidget);
    setCurrentIndex(0);
    
    connect(m_MultiRound, &MultiplayerRound::noRobotRegistration, this, &AccountWidget::noRobotRegistrationSlot);
    connect(m_MultiRound, &MultiplayerRound::registrationComplete, this, &AccountWidget::registrationComplete);
    connect(m_MultiRound, &MultiplayerRound::registrationFailed, this, &AccountWidget::registrationComplete);
}

void AccountWidget::noRobotRegistrationSlot(const std::vector<QString>& images, const QJsonObject& registrationReplyJson) {
    
    setCurrentIndex(1);
    m_NoRobotWidget->setImages(images);
    m_NoRobotWidget->setQuestion(registrationReplyJson.value("question").toString());
    m_NoRobotWidget->setRequestId(registrationReplyJson.value("id").toString());
}


void AccountWidget::registrationComplete() {
    setCurrentIndex(0);
    //TODO: update mainaccountwidget
}

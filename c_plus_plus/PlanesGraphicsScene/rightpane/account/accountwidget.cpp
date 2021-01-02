#include "accountwidget.h"

AccountWidget::AccountWidget(QSettings* settings, GlobalData* globalData, QNetworkAccessManager* networkManager, GameInfo* gameInfo, QWidget* parent) 
    : QStackedWidget(parent), m_GlobalData(globalData), m_NetworkManager(networkManager), m_Settings(settings), m_GameInfo(gameInfo)
{
    m_MainAccountWidget = new MainAccountWidget(m_Settings, m_GlobalData, m_NetworkManager, m_GameInfo);
    addWidget(m_MainAccountWidget);
    m_NoRobotWidget = new NoRobotWidget(m_NetworkManager, m_Settings, m_GlobalData, m_GameInfo);
    addWidget(m_NoRobotWidget);
    setCurrentIndex(0);
    
    connect(m_MainAccountWidget, &MainAccountWidget::noRobotRegistration, this, &AccountWidget::noRobotRegistrationSlot);
    connect(m_NoRobotWidget, &NoRobotWidget::registrationComplete, this, &AccountWidget::registrationComplete);
    connect(m_NoRobotWidget, &NoRobotWidget::registrationFailed, this, &AccountWidget::registrationComplete);
}

void AccountWidget::noRobotRegistrationSlot(const std::vector<QString>& images, const QJsonObject& registrationReplyJson) {
    
    setCurrentIndex(1);
    m_NoRobotWidget->setImages(images);
    m_NoRobotWidget->setQuestion(registrationReplyJson.value("question").toString());
    m_NoRobotWidget->setRequestId(QString::number(registrationReplyJson.value("id").toInt()));
}


void AccountWidget::registrationComplete() {
    setCurrentIndex(0);
    //TODO: update mainaccountwidget
}

#include "chatwidget.h"

#include <QHBoxLayout>
#include <QLabel>

ChatWidget::ChatWidget(GlobalData* globalData, MultiplayerRound* multiround, QSettings* settings, QWidget* parent):
        m_MultiRound(multiround), m_GlobalData(globalData), m_Settings(settings) {

    m_PlayersListWidget = new PlayersListWidget(m_GlobalData, m_MultiRound);
    m_ChatStackedWidget = new QStackedWidget();
    m_MessageLineEdit = new QLineEdit();
    m_SendMessageButton = new QPushButton("Send");

    QHBoxLayout* hLayout = new QHBoxLayout();
    QVBoxLayout* vLayout2 = new QVBoxLayout();
    QLabel* playersLabel = new QLabel("Players");
    vLayout2->addWidget(playersLabel);
    vLayout2->addWidget(m_PlayersListWidget);
    hLayout->addLayout(vLayout2);

    QVBoxLayout* vLayout1 = new QVBoxLayout();
    m_ChatStackedWidget->setSizePolicy(QSizePolicy::Expanding, QSizePolicy::Expanding);
    vLayout1->addWidget(m_ChatStackedWidget);
    QLabel* messageLabel = new QLabel("Write your message here");
    vLayout1->addWidget(messageLabel);

    QHBoxLayout* hLayout1 = new QHBoxLayout();
    hLayout1->addWidget(m_MessageLineEdit);
    hLayout1->addWidget(m_SendMessageButton);

    vLayout1->addLayout(hLayout1);

    hLayout->addLayout(vLayout1);
    setLayout(hLayout);

    connect(m_MultiRound, &MultiplayerRound::connectedToChat, this, &ChatWidget::subscribeToTopic);
    m_MultiRound->connectToChat();
}

void ChatWidget::setActive(bool active) {
    m_PlayersListWidget->setActive(active);

    if (!m_MultiRound->chatSocketConnected()) {
        qDebug() << "Not connected to chat server";
        return;
    }

    if (active) {
        m_MultiRound->createChatConnection();
        return;
    }

    m_MultiRound->destroyChatConnection();

    //TODO: if active true connect and subscribe to topic
    //TODO: if active false unsubscribe to topic and disconnect
}

void ChatWidget::subscribeToTopic() {
    qDebug() << "Subscribe to topic";
    m_MultiRound->subscribeToChatTopic();
}

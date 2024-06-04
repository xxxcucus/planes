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

    connect(m_MultiRound, &MultiplayerRound::chatMessageReceived, this, &ChatWidget::chatMessageReceived);
    connect(m_MultiRound, &MultiplayerRound::chatConnectionError, this, &ChatWidget::chatConnectionError);
    connect(m_PlayersListWidget, &PlayersListWidget::playerDoubleClicked, this, &ChatWidget::openChatWindow);
    //connect(m_PlayersListWidget, &PlayersListWidget::)
    connect(m_SendMessageButton, &QPushButton::clicked, this, &ChatWidget::sendMessageToPlayer);
}

void ChatWidget::setActive(bool active) {
    m_PlayersListWidget->setActive(active);
}

void ChatWidget::openChatWindow(const QString& player) {
    qDebug() << "Open chat window" << player;

    if (m_CurrentReceiver == player) {
        //qDebug() << "You are in the correct chat window";
        return;
    }

    if (m_ChatSessions.find(player) != m_ChatSessions.end()) {
        //qDebug() << "Getting chat window from list";
        m_ChatStackedWidget->setCurrentWidget(m_ChatSessions[player]);
        m_CurrentReceiver = player;
        return;
    }

    //qDebug() << "Creating chat window ";
    QTextEdit* chatSession = new QTextEdit();
    m_ChatSessions[player] = chatSession;
    m_ChatStackedWidget->addWidget(chatSession);
    m_ChatStackedWidget->setCurrentWidget(chatSession);
    m_CurrentReceiver = player;
}

void ChatWidget::sendMessageToPlayer() {
    QString message = m_MessageLineEdit->text().trimmed();
    if (message.isEmpty())
        return;

    QTextEdit* chatSession = dynamic_cast<QTextEdit*>(m_ChatStackedWidget->currentWidget());
    if (chatSession == nullptr) {
        qDebug() << "Chat session is null";
        return;
    }

    m_MultiRound->sendMessageThroughChat(m_CurrentReceiver, message);
    chatSession->append(QString("%1 : %2").arg(m_GlobalData->m_UserData.m_UserName).arg(message));
}

void ChatWidget::chatMessageReceived(const QString& sender, const QString& message) {
    m_PlayersListWidget->addPlayer(sender);

    qDebug() << "Chat message received from " << sender;
    openChatWindow(sender);
    QTextEdit* chatSession = dynamic_cast<QTextEdit*>(m_ChatStackedWidget->currentWidget());
    if (chatSession == nullptr) {
        qDebug() << "Chat session is null";
        return;
    }

    chatSession->append(QString("%1 : %2").arg(sender).arg(message));
}

void ChatWidget::chatConnectionError(const QString& errorMessage) {
    qDebug() << "Chat server error " << errorMessage;
}

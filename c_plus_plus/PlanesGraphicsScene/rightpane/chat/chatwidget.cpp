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
    connect(m_SendMessageButton, &QPushButton::clicked, this, &ChatWidget::sendMessageToPlayer);

    m_DatabaseService.openDb();
    m_DatabaseService.deleteOldMessages(90);
}

void ChatWidget::setActive(bool active) {
    m_PlayersListWidget->setActive(active);

    if (active) {
        qDebug() << "Read chat messages from db";
        std::vector<ReceivedChatMessageViewModel> dbMessages = m_DatabaseService.getMessages(m_GlobalData->m_UserData.m_UserName, m_GlobalData->m_UserData.m_UserId);

        for (ReceivedChatMessageViewModel m : dbMessages)
            addChatMessageFromDb(m);
    } else {
        for (int i = m_ChatStackedWidget->count() - 1; i >= 0 ; i--) {
            m_ChatStackedWidget->removeWidget(m_ChatStackedWidget->widget(i));
        }
        for (auto it = m_ChatSessions.begin(); it != m_ChatSessions.end(); it++) {
            delete it->second;
        }
        m_ChatSessions.clear();
    }
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

    //TODO: check that we do not get 0 here
    m_CurrentReceiverId = m_PlayersListWidget->getPlayerId(player);
}

void ChatWidget::sendMessageToPlayer() {
    QString message = m_MessageLineEdit->text().trimmed().left(128);
    if (message.isEmpty())
        return;

    QTextEdit* chatSession = dynamic_cast<QTextEdit*>(m_ChatStackedWidget->currentWidget());
    if (chatSession == nullptr) {
        qDebug() << "Chat session is null";
        return;
    }

    m_MultiRound->sendMessageThroughChat(m_CurrentReceiver, m_CurrentReceiverId, message);
    chatSession->append(QString("%1 : %2  : %3").arg(m_GlobalData->m_UserData.m_UserName).arg(message).arg(QDateTime::currentDateTime().toString()));

    ReceivedChatMessageViewModel messageViewModel;
    messageViewModel.m_ReceiverId = m_CurrentReceiverId;
    messageViewModel.m_ReceiverName = m_CurrentReceiver;
    messageViewModel.m_SenderId = m_GlobalData->m_UserData.m_UserId;
    messageViewModel.m_SenderName = m_GlobalData->m_UserData.m_UserName;
    messageViewModel.m_Message = message;
    messageViewModel.m_CreatedAt = QDateTime::currentDateTimeUtc();

    bool saveOK = m_DatabaseService.addChatMessage(messageViewModel, m_GlobalData->m_UserData.m_UserId, m_GlobalData->m_UserData.m_UserName);
    if (saveOK) {
        qDebug() << "Message saved to db";
    } else {
        qDebug() << "Error when saving message to db";
    }
}

//Message received from server
void ChatWidget::chatMessageReceived(const ReceivedChatMessageViewModel& message) {
    m_PlayersListWidget->addPlayer(message.m_SenderName, message.m_SenderId);

    qDebug() << "Chat message received from " << message.m_SenderName;
    openChatWindow(message.m_SenderName);
    QTextEdit* chatSession = dynamic_cast<QTextEdit*>(m_ChatStackedWidget->currentWidget());
    if (chatSession == nullptr) {
        qDebug() << "Chat session is null";
        return;
    }

    //here write the time in local time (in server and database is utc)

    chatSession->append(QString("%1 : %2  : %3").arg(message.m_SenderName).arg(message.m_Message).arg(message.m_CreatedAt.toLocalTime().toString()));
    bool saveOK = m_DatabaseService.addChatMessage(message, m_GlobalData->m_UserData.m_UserId, m_GlobalData->m_UserData.m_UserName);
    if (saveOK) {
        qDebug() << "Message saved to db";
    } else {
        qDebug() << "Error when saving message to db";
    }
}

void ChatWidget::addChatMessageFromDb(const ReceivedChatMessageViewModel& message) {

    if (message.m_ReceiverId == m_GlobalData->m_UserData.m_UserId && message.m_ReceiverName == m_GlobalData->m_UserData.m_UserName)  {
        //qDebug() << "Receiver " << message.m_ReceiverName;
        //qDebug() << "Add player 1 " << message.m_SenderName;
        m_PlayersListWidget->addPlayer(message.m_SenderName, message.m_SenderId);
        openChatWindow(message.m_SenderName);
    } else if (message.m_SenderId == m_GlobalData->m_UserData.m_UserId && message.m_SenderName == m_GlobalData->m_UserData.m_UserName ) {
        //qDebug() << "Sender " << message.m_SenderName;
        //qDebug() << "Add player 2 " << message.m_ReceiverName;
        m_PlayersListWidget->addPlayer(message.m_ReceiverName, message.m_ReceiverId);
        openChatWindow(message.m_ReceiverName);
    }
    QTextEdit* chatSession = dynamic_cast<QTextEdit*>(m_ChatStackedWidget->currentWidget());
    if (chatSession == nullptr) {
        qDebug() << "Chat session is null";
        return;
    }

    chatSession->append(QString("%1 : %2 : %3 ").arg(message.m_SenderName).arg(message.m_Message).arg(message.m_CreatedAt.toLocalTime().toString()));
}

void ChatWidget::chatConnectionError(const QString& errorMessage) {
    qDebug() << "Chat server error " << errorMessage;
}

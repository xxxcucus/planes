#include "playerslistwidget.h"
#include <QVBoxLayout>

PlayersListWidget::PlayersListWidget(GlobalData* globalData, MultiplayerRound* mrd, QWidget *parent):
    QWidget(parent), m_GlobalData(globalData), m_MultiplayerRound(mrd) {

    QVBoxLayout* vLayout = new QVBoxLayout();

    m_PlayersListWidget = new QListWidget();
    vLayout->addWidget(m_PlayersListWidget);

    setLayout(vLayout);

    m_RefreshPlayersListTimer = new QTimer(this);
    m_GetChatMessagesTimer = new QTimer(this);
    connect(m_MultiplayerRound, &MultiplayerRound::playersListReceived, this, &PlayersListWidget::updatePlayers);
    connect(m_RefreshPlayersListTimer, &QTimer::timeout, this, &PlayersListWidget::sendPlayersRequest);
    connect(m_GetChatMessagesTimer, &QTimer::timeout, this, &PlayersListWidget::requestChatMessages);
    connect(m_PlayersListWidget, &QListWidget::itemDoubleClicked, this, &PlayersListWidget::itemDoubleClicked);

}

void PlayersListWidget::updatePlayers(const std::vector<UserWithLastLoginViewModel>& players) {
   if (m_PlayersListWidget->currentItem() != nullptr)
       m_CurrentPlayer = getPlayerFromEntryListWidget(m_PlayersListWidget->currentItem()->text());
   else
       m_CurrentPlayer = QString();

    emptyPlayersListWidget();

    m_PlayersListFromServer.clear();
    for (UserWithLastLoginViewModel playerModel: players) {
        QString player = playerModel.m_UserName;
        if (player != m_GlobalData->m_UserData.m_UserName) {
            m_PlayersListWidget->addItem(buildPlayerEntryListWidget(playerModel));
            m_PlayersListFromServer.insert(playerModel);
        }
    }

    for (UserWithLastLoginViewModel player: m_PlayersList) {
        if (findPlayerInPlayersMap(player, m_PlayersListFromServer))
            continue;
        if (player.m_UserName != m_GlobalData->m_UserData.m_UserName)
            m_PlayersListWidget->addItem(buildPlayerEntryListWidget(player));
    }

    if (m_CurrentPlayer.isEmpty())
        return;

    for (int i = 0; i < m_PlayersListWidget->count(); i++) {
        QListWidgetItem* item = m_PlayersListWidget->item(i);
        if (getPlayerFromEntryListWidget(item->text()) == m_CurrentPlayer) {
            m_PlayersListWidget->setCurrentItem(item);
            break;
        }
    }
}

void PlayersListWidget::addPlayer(const QString& player, long int playerid) {

    UserWithLastLoginViewModel user(player, playerid);
    auto res = m_PlayersList.insert(user);
    if (res.second)
        updatePlayersFromPlayersList();
}

void PlayersListWidget::setActive(bool active) {
    m_IsActive = active;

    if (active) {
        m_RefreshPlayersListTimer->start(5000);
        m_GetChatMessagesTimer->start(5000);
    } else {
        m_RefreshPlayersListTimer->stop();
        m_GetChatMessagesTimer->stop();
    }
}

void PlayersListWidget::sendPlayersRequest() {
    if (m_IsActive)
        m_MultiplayerRound->requestLoggedInPlayers(90);
}

void PlayersListWidget::requestChatMessages() {
    if (m_IsActive)
        m_MultiplayerRound->requestChatMessages();
}

void PlayersListWidget::itemDoubleClicked(QListWidgetItem* item) {
    QString playerAndStatus = item->text();
    QStringList playerAndStatusParts = playerAndStatus.split(" ", Qt::SkipEmptyParts);
    if (!playerAndStatus.isEmpty())
        emit playerDoubleClicked(playerAndStatusParts[0]);
}

/*void PlayersListWidget::updatePlayersList(const QStringList& players) {
    for (QString player : players)
        m_PlayersList.insert(player);
}*/

void PlayersListWidget::updatePlayersFromPlayersList() {
    emptyPlayersListWidget();

    for (UserWithLastLoginViewModel player: m_PlayersListFromServer) {
        if (player.m_UserName != m_GlobalData->m_UserData.m_UserName) {
            m_PlayersListWidget->addItem(player.m_UserName);
        }
    }

    for (UserWithLastLoginViewModel player: m_PlayersList) {
        if (findPlayerInPlayersMap(player, m_PlayersListFromServer))
            continue;
        if (player.m_UserName != m_GlobalData->m_UserData.m_UserName)
            m_PlayersListWidget->addItem(buildPlayerEntryListWidget(player));
    }
}

bool PlayersListWidget::findPlayerInPlayersMap(const UserWithLastLoginViewModel& player, const std::set<UserWithLastLoginViewModel>& playersMap) {
    auto it = playersMap.begin();
    while (it != playersMap.end()) {
        if (it->m_UserName == player.m_UserName)
            break;
        it++;
    }

    if (it != m_PlayersListFromServer.end())
        return true;

    return false;
}

void PlayersListWidget::emptyPlayersListWidget() {
    while (m_PlayersListWidget->count() > 0) {
        m_PlayersListWidget->takeItem(0);
    }
}

QString PlayersListWidget::buildPlayerEntryListWidget(const UserWithLastLoginViewModel& player) {
    QDateTime lastLogin = player.m_LastLogin;
    QString status = "Online";

    qint64 timeDiff = lastLogin.msecsTo(QDateTime::currentDateTime());
    timeDiff = timeDiff / 1000;

    if (timeDiff > 1800) //30 min
        status = "Offline";

    return QString("%1 %2").arg(player.m_UserName).arg(status);
}

QString PlayersListWidget::getPlayerFromEntryListWidget(const QString& entryText) {
    QStringList tokens = entryText.split(" ", Qt::SkipEmptyParts);
    if (tokens.size() > 1)
        return tokens[0];
    return QString();
}

long int PlayersListWidget::getPlayerId(const QString& player) {
    for (auto p : m_PlayersList) {
        if (p.m_UserName == player)
            return p.m_UserId;
    }

    for (auto p : m_PlayersListFromServer) {
        if (p.m_UserName == player)
            return p.m_UserId;
    }

    qDebug() << "GetPlayerId " << player << "id 0";
    return 0L;
}

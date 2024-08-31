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
       m_CurrentPlayer = getPlayerFromEntryListWidget(m_PlayersListWidget->currentItem());
   else
       m_CurrentPlayer = QString();

    emptyPlayersListWidget();

    m_PlayersListFromServer.clear();
    for (UserWithLastLoginViewModel playerModel: players) {
        QString player = playerModel.m_UserName;
        if (player != m_GlobalData->m_UserData.m_UserName) {
            addPlayer(playerModel);
            m_PlayersListFromServer.push_back(playerModel);
        }
    }

    for (UserWithLastLoginViewModel player: m_PlayersList) {
        if (findPlayerInPlayersMap(player, m_PlayersListFromServer))
            continue;
        if (player.m_UserName != m_GlobalData->m_UserData.m_UserName) {
            addPlayer(player);
        }
    }

    if (m_CurrentPlayer.isEmpty())
        return;

    for (int i = 0; i < m_PlayersListWidget->count(); i++) {
        QListWidgetItem* item = m_PlayersListWidget->item(i);
        if (getPlayerFromEntryListWidget(item) == m_CurrentPlayer) {
            m_PlayersListWidget->setCurrentItem(item);
            break;
        }
    }
}

void PlayersListWidget::addPlayer(const QString& player, long int playerid) {

    UserWithLastLoginViewModel user(player, playerid);
    auto it = std::find_if(m_PlayersList.begin(), m_PlayersList.end(), [player, playerid](const UserWithLastLoginViewModel& usr) {
        return usr.m_UserName == player && usr.m_UserId == playerid;
    });
    if (it == m_PlayersList.end())  {
        m_PlayersList.push_back(user);
        updatePlayersFromPlayersList();
    }
}

void PlayersListWidget::setActive(bool active) {
    m_IsActive = active;

    if (active) {
        m_RefreshPlayersListTimer->start(5000);
        m_GetChatMessagesTimer->start(5000);
    } else {
        m_RefreshPlayersListTimer->stop();
        m_GetChatMessagesTimer->stop();
        m_PlayersList.clear();
        m_PlayersListFromServer.clear();
        m_PlayersListWidget->clear();
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
        QString player = getPlayerFromEntryListWidget(item);
        if (!player.trimmed().isEmpty())
            emit playerDoubleClicked(player.trimmed());
}

/*void PlayersListWidget::updatePlayersList(const QStringList& players) {
    for (QString player : players)
        m_PlayersList.insert(player);
}*/

void PlayersListWidget::updatePlayersFromPlayersList() {
    emptyPlayersListWidget();

    for (UserWithLastLoginViewModel player: m_PlayersListFromServer) {
        if (player.m_UserName != m_GlobalData->m_UserData.m_UserName) {
            addPlayer(player);
        }
    }

    for (UserWithLastLoginViewModel player: m_PlayersList) {
        if (findPlayerInPlayersMap(player, m_PlayersListFromServer))
            continue;
        if (player.m_UserName != m_GlobalData->m_UserData.m_UserName) {
            addPlayer(player);
        }
    }
}

bool PlayersListWidget::findPlayerInPlayersMap(const UserWithLastLoginViewModel& player, const std::vector<UserWithLastLoginViewModel>& playersMap) {
    auto it = playersMap.begin();
    while (it != playersMap.end()) {
        if (it->m_UserName == player.m_UserName)
            break;
        it++;
    }

    if (it != playersMap.end())
        return true;

    return false;
}

void PlayersListWidget::emptyPlayersListWidget() {
    while (m_PlayersListWidget->count() > 0) {
        m_PlayersListWidget->takeItem(0);
    }
}

UserWithStatusWidget* PlayersListWidget::buildPlayerEntryListWidget(const UserWithLastLoginViewModel& player, int width) {
    QDateTime lastLogin = player.m_LastLogin;
    bool online = true;

    qint64 timeDiff = lastLogin.msecsTo(QDateTime::currentDateTime());
    timeDiff = timeDiff / 1000;

    if (timeDiff > 1800) //30 min
        online = false;

    return new UserWithStatusWidget(player.m_UserName, online, width);
}

QString PlayersListWidget::getPlayerFromEntryListWidget(QListWidgetItem* item) {
    UserWithStatusWidget* w = (UserWithStatusWidget*)m_PlayersListWidget->itemWidget(item);
    return w->getName();
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

void PlayersListWidget::addPlayer(const UserWithLastLoginViewModel& player) {
    UserWithStatusWidget* widget = buildPlayerEntryListWidget(player, m_PlayersListWidget->width());
    QListWidgetItem* item = new QListWidgetItem();
    item->setSizeHint(widget->sizeHint());
    m_PlayersListWidget->addItem(item);
    m_PlayersListWidget->setItemWidget(item, widget);

}

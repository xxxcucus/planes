#include "playerslistwidget.h"
#include <QVBoxLayout>

PlayersListWidget::PlayersListWidget(GlobalData* globalData, MultiplayerRound* mrd, QWidget *parent):
    QWidget(parent), m_GlobalData(globalData), m_MultiplayerRound(mrd) {

    QVBoxLayout* vLayout = new QVBoxLayout();

    m_PlayersListWidget = new QListWidget();
    vLayout->addWidget(m_PlayersListWidget);

    setLayout(vLayout);

    m_RefreshPlayersListTimer = new QTimer(this);
    connect(m_MultiplayerRound, &MultiplayerRound::playersListReceived, this, &PlayersListWidget::updatePlayers);
    connect(m_RefreshPlayersListTimer, &QTimer::timeout, this, &PlayersListWidget::sendPlayersRequest);
    connect(m_PlayersListWidget, &QListWidget::itemDoubleClicked, this, &PlayersListWidget::itemDoubleClicked);

}

void PlayersListWidget::updatePlayers(const std::vector<UserWithLastLoginViewModel>& players) {
   if (m_PlayersListWidget->currentItem() != nullptr)
       m_CurrentItemText = m_PlayersListWidget->currentItem()->text();
   else
       m_CurrentItemText = QString();

    while (m_PlayersListWidget->count() > 0) {
        m_PlayersListWidget->takeItem(0);
    }

    m_PlayersListFromServer.clear();
    for (UserWithLastLoginViewModel playerModel: players) {
        QString player = playerModel.m_UserName;
        if (player != m_GlobalData->m_UserData.m_UserName) {
            m_PlayersListWidget->addItem(player);
            m_PlayersListFromServer.insert(playerModel);
        }
    }

    for (UserWithLastLoginViewModel player: m_PlayersList) {
        auto it = m_PlayersListFromServer.begin();
        while (it != m_PlayersListFromServer.end()) {
            if (it->m_UserName == player.m_UserName)
                break;
            it++;
        }

        if (it != m_PlayersListFromServer.end())
            continue;
        if (player.m_UserName != m_GlobalData->m_UserData.m_UserName)
            m_PlayersListWidget->addItem(player.m_UserName);
    }

    //TODO: should one chat window be selected here ?
    if (m_CurrentItemText.isEmpty())
        return;

    for (int i = 0; i < m_PlayersListWidget->count(); i++) {
        QListWidgetItem* item = m_PlayersListWidget->item(i);
        if (item->text() == m_CurrentItemText) {
            m_PlayersListWidget->setCurrentItem(item);
            break;
        }
    }
}

void PlayersListWidget::addPlayer(const QString& player) {

    UserWithLastLoginViewModel user(player);
    auto res = m_PlayersList.insert(user);
    if (res.second)
        updatePlayersFromPlayersList();
}

void PlayersListWidget::setActive(bool active) {
    m_IsActive = active;

    if (active) {
        m_RefreshPlayersListTimer->start(5000);
    } else {
        m_RefreshPlayersListTimer->stop();
    }
}

void PlayersListWidget::sendPlayersRequest() {
    if (m_IsActive)
        m_MultiplayerRound->requestLoggedInPlayers(90);
}

void PlayersListWidget::itemDoubleClicked(QListWidgetItem* item) {
    emit playerDoubleClicked(item->text());
}

/*void PlayersListWidget::updatePlayersList(const QStringList& players) {
    for (QString player : players)
        m_PlayersList.insert(player);
}*/

void PlayersListWidget::updatePlayersFromPlayersList() {
    while (m_PlayersListWidget->count() > 0) {
        m_PlayersListWidget->takeItem(0);
    }

    m_PlayersListFromServer.clear();
    for (UserWithLastLoginViewModel player: m_PlayersListFromServer) {
        if (player.m_UserName != m_GlobalData->m_UserData.m_UserName) {
            m_PlayersListWidget->addItem(player.m_UserName);
        }
    }

    for (UserWithLastLoginViewModel player: m_PlayersList) {
        auto it = m_PlayersListFromServer.begin();
        while (it != m_PlayersListFromServer.end()) {
            if (it->m_UserName == player.m_UserName)
                break;
            it++;
        }


        if (it != m_PlayersListFromServer.end())
            continue;
        if (player.m_UserName != m_GlobalData->m_UserData.m_UserName)
            m_PlayersListWidget->addItem(player.m_UserName);
    }
}

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

void PlayersListWidget::updatePlayers(const QStringList& players) {
    while (m_PlayersListWidget->count() > 0) {
        m_PlayersListWidget->takeItem(0);
    }

    m_PlayersListFromServer.clear();
    for (QString player: players) {
        if (player != m_GlobalData->m_UserData.m_UserName) {
            m_PlayersListWidget->addItem(player);
            m_PlayersListFromServer.insert(player);
        }
    }

    for (QString player: m_PlayersList) {
        if (m_PlayersListFromServer.find(player) != m_PlayersListFromServer.end())
            continue;
        if (player != m_GlobalData->m_UserData.m_UserName)
            m_PlayersListWidget->addItem(player);
    }
}

void PlayersListWidget::addPlayer(const QString& player) {
    auto res = m_PlayersList.insert(player);
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
        m_MultiplayerRound->requestLoggedInPlayers();
}

void PlayersListWidget::itemDoubleClicked(QListWidgetItem* item) {
    emit playerDoubleClicked(item->text());
}

void PlayersListWidget::updatePlayersList(const QStringList& players) {
    for (QString player : players)
        m_PlayersList.insert(player);
}

void PlayersListWidget::updatePlayersFromPlayersList() {
    while (m_PlayersListWidget->count() > 0) {
        m_PlayersListWidget->takeItem(0);
    }

    m_PlayersListFromServer.clear();
    for (QString player: m_PlayersListFromServer) {
        if (player != m_GlobalData->m_UserData.m_UserName) {
            m_PlayersListWidget->addItem(player);
        }
    }

    for (QString player: m_PlayersList) {
        if (m_PlayersListFromServer.find(player) != m_PlayersListFromServer.end())
            continue;
        if (player != m_GlobalData->m_UserData.m_UserName)
            m_PlayersListWidget->addItem(player);
    }
}

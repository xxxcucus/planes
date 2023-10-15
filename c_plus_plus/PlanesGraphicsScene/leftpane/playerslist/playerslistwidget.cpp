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

}

void PlayersListWidget::updatePlayers(const QStringList& players) {
    while (m_PlayersListWidget->count() > 0) {
        m_PlayersListWidget->takeItem(0);
    }

    for (QString player: players) {
        if (player != m_GlobalData->m_UserData.m_UserName)
            m_PlayersListWidget->addItem(player);
    }
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


#ifndef _PLAYERS_LIST_WIDGET_H
#define _PLAYERS_LIST_WIDGET_H

#include "multiplayerround.h"
#include <QListWidget>

class PlayersListWidget: public QWidget {
    Q_OBJECT

public:
    PlayersListWidget(MultiplayerRound* mrd, QWidget *parent = 0);
    void setActive(bool active);

private slots:
    void updatePlayers(const QStringList& players);

private:
    void sendPlayersRequest();

private:
    MultiplayerRound* m_MultiplayerRound = nullptr;
    QListWidget* m_PlayersListWidget = nullptr;
    QTimer* m_RefreshPlayersListTimer = nullptr;

    bool m_IsActive = false;
};












#endif

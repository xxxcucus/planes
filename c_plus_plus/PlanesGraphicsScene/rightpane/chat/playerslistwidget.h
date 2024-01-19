#ifndef _PLAYERS_LIST_WIDGET_H
#define _PLAYERS_LIST_WIDGET_H

#include "multiplayerround.h"
#include <QListWidget>
#include "global/globaldata.h"
#include <set>

class PlayersListWidget: public QWidget {
    Q_OBJECT

public:
    PlayersListWidget(GlobalData* globalData, MultiplayerRound* mrd, QWidget *parent = 0);
    void setActive(bool active);
    void addPlayer(const QString& player);

private slots:
    void updatePlayers(const QStringList& players);
    void itemDoubleClicked(QListWidgetItem* item);

signals:
    void playerDoubleClicked(const QString& player);

private:
    void sendPlayersRequest();
    void updatePlayersList(const QStringList& players);
    void updatePlayersFromPlayersList();

private:
    MultiplayerRound* m_MultiplayerRound = nullptr;
    GlobalData* m_GlobalData = nullptr;
    QListWidget* m_PlayersListWidget = nullptr;
    QTimer* m_RefreshPlayersListTimer = nullptr;

    std::set<QString> m_PlayersList;

    bool m_IsActive = false;
};












#endif

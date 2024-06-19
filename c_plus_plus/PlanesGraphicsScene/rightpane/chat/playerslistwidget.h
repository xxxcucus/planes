#ifndef _PLAYERS_LIST_WIDGET_H
#define _PLAYERS_LIST_WIDGET_H

#include "multiplayerround.h"
#include <QListWidget>
#include "global/globaldata.h"
#include <set>
#include <vector>
#include "viewmodels/userwithlastloginviewmodel.h"

class PlayersListWidget: public QWidget {
    Q_OBJECT

public:
    PlayersListWidget(GlobalData* globalData, MultiplayerRound* mrd, QWidget *parent = 0);
    void setActive(bool active);
    void addPlayer(const QString& player);

private slots:
    void updatePlayers(const std::vector<UserWithLastLoginViewModel>& players);
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

    std::set<UserWithLastLoginViewModel> m_PlayersList;
    std::set<UserWithLastLoginViewModel> m_PlayersListFromServer;
    QString m_CurrentItemText;

    bool m_IsActive = false;
};












#endif

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
    bool findPlayerInPlayersMap(const UserWithLastLoginViewModel& player, const std::set<UserWithLastLoginViewModel>& playersMap);
    void emptyPlayersListWidget();
    QString buildPlayerEntryListWidget(const UserWithLastLoginViewModel& player);
    QString getPlayerFromEntryListWidget(const QString& entryText);

private:
    MultiplayerRound* m_MultiplayerRound = nullptr;
    GlobalData* m_GlobalData = nullptr;
    QListWidget* m_PlayersListWidget = nullptr;
    QTimer* m_RefreshPlayersListTimer = nullptr;

    //players added from initiating conversations
    std::set<UserWithLastLoginViewModel> m_PlayersList;
    //players added from the response to the request for the available users on server
    std::set<UserWithLastLoginViewModel> m_PlayersListFromServer;
    QString m_CurrentPlayer;

    bool m_IsActive = false;
};












#endif

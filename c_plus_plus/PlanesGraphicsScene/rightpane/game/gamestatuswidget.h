#ifndef __GAME_STATUS_WIDGET__
#define __GAME_STATUS_WIDGET__

#include <QFrame>
#include <QLabel>
#include <QNetworkReply>
#include <QSettings>
#include <QNetworkAccessManager>
#include "global/globaldata.h"
#include "gameinfo.h"
#include "multiplayerround.h"


class GameStatusWidget : public QFrame
{
    Q_OBJECT

public:
    explicit GameStatusWidget(MultiplayerRound* mrd, QWidget* parent = nullptr);

public slots:
    void gameCreatedSlot(const QString& gameName, const QString& username);
    void gameConnectedToSlot(const QString& gameName, const QString& firstPlayerName, const QString& secondPlayerName, const QString& currentRoundId);
    void refreshSlot();
        
public:
    QLabel* m_GameName;
    QLabel* m_FirstPlayerName;
    QLabel* m_SecondPlayerName;
    QLabel* m_RoundName;
    
    MultiplayerRound* m_MultiRound;
};





#endif

#ifndef _PLAYROUND_WIDGET__
#define _PLAYROUND_WIDGET__

#include <QWidget>
#include <QPushButton>
#include <QNetworkAccessManager>
#include <QSettings>

#include "gamestatsframe.h"
#include "gameinfo.h"
#include "gamestatistics.h"

class PlayRoundWidget: public QWidget {
    Q_OBJECT
    
public:
    PlayRoundWidget(GameInfo* gameInfo, QWidget *parent = 0);
    void updateGameStatistics(const GameStatistics &gs);
    
signals:
    void acquireOpponentMovesClicked(bool);
    void cancelRoundClicked(bool);
    
private:
    GameStatsFrame* m_PlayerStatsFrame;
    GameStatsFrame* m_ComputerStatsFrame;    
    
    QPushButton* m_acquireOpponentMovesButton;
    QPushButton* m_CancelRoundButton;
    
    GameInfo* m_GameInfo;
};












#endif

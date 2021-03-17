#ifndef __GAME_WIDGET__
#define __GAME_WIDGET__

#include <QWidget>
#include <QFrame>
#include <QNetworkAccessManager>
#include <QSettings>

#include "gameinfo.h"
#include "global/globaldata.h"
#include "multiplayerround.h"

class GameWidget : public QFrame {
    Q_OBJECT
    
public:
    GameWidget(GlobalData* globalData, MultiplayerRound* mrd, QWidget* parent = nullptr);

public:
    void toGameButtonClickedSlot(bool value);
    
signals:
    void toGameButtonClicked(bool value);
    
private:    
    GlobalData* m_GlobalData;
    MultiplayerRound* m_MultiRound;
};

#endif

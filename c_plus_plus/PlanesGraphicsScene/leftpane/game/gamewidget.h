#ifndef __GAME_WIDGET__
#define __GAME_WIDGET__

#include <QWidget>
#include <QFrame>
#include <QNetworkAccessManager>
#include <QSettings>

#include "gameinfo.h"
#include "global/globaldata.h"
#include "multiplayerround.h"
#include "gamestatuswidget.h"

class GameWidget : public QFrame {
    Q_OBJECT
    
public:
    GameWidget(GlobalData* globalData, MultiplayerRound* mrd, QWidget* parent = nullptr);
    
    /**
     * Tab is changed in the left pane
     * **/
    void currentTabChanged();

public slots:
    void toGameButtonClickedSlot(bool value);
    void periodicallyRefreshStatusSlot();
    void refreshStatusWithTimer();
    void connectToGameSlot();
    
private:
    void stopRefreshStatusTimer();
    
signals:
    void toGameButtonClicked(bool value);
    
private:    
    GlobalData* m_GlobalData;
    MultiplayerRound* m_MultiRound;
    QTimer* m_RefreshStatusTimer = nullptr;
    GameStatusWidget* m_GameStatusWidget;
};

#endif

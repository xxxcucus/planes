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
#include "creategamewidget.h"

class GameWidget : public QFrame {
    Q_OBJECT
    
public:
    GameWidget(GlobalData* globalData, MultiplayerRound* mrd, QWidget* parent = nullptr);
    
    /**
     * Tab is changed in the left pane
     * **/
    void currentTabChanged();
    void stopRefreshStatusTimer();

public slots:
    void toGameButtonClickedSlot(bool value);
    void periodicallyRefreshStatusSlot();
    void refreshStatusWithTimer();
    void connectToGameSlot();

signals:
    void toGameButtonClicked(bool value);
    
private:    
    GlobalData* m_GlobalData;
    MultiplayerRound* m_MultiRound;
    QTimer* m_RefreshStatusTimer = nullptr;
    CreateGameWidget* m_CreateGameWidget;
};

#endif

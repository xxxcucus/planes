#ifndef PLANESGSVIEW_H
#define PLANESGSVIEW_H

#include <QWidget>
#include <QNetworkAccessManager>
#include <QSettings>

#include "leftpane/leftpane.h"
#include "rightpane/rightpane.h"
#include "planegrid.h"
#include "planeround.h"
#include "global/globaldata.h"
#include "gameinfo.h"
#include "multiplayerround.h"

class PlanesGSView : public QWidget
{
    Q_OBJECT
public:
    PlanesGSView(PlaneRound* rd, MultiplayerRound* mrd, GlobalData* globalData, QNetworkAccessManager* networkManager, GameInfo* gameInfo, QSettings* settings, QWidget *parent = 0);

signals:

public slots:

    inline void activateBoardEditingTab() {
        m_LeftPane->activateEditorTab();
    }
	void displayStatusMsg(const std::string& str);
	void receivedPlayerGuess(const GuessPoint& gp);
	void doneClicked();
	void startNewGame();
    
    void opponentMoveGeneratedSlot(const GuessPoint& gp);

private:

    //PlaneGrid objects manage the logic of a set of planes on a grid
    //as well as various operations: save, remove, search, etc.
    PlaneGrid* m_playerGrid;
    PlaneGrid* m_computerGrid;

    //PlaneRound is the object that coordinates the game
    PlaneRound* m_round;
    MultiplayerRound* m_MultiRound;

    LeftPane* m_LeftPane;
    RightPane* m_RightPane;
    
    GlobalData* m_GlobalData;
    QNetworkAccessManager* m_NetworkManager;
    GameInfo* m_GameInfo;
    QSettings* m_Settings;
};

#endif // PLANESGSVIEW_H

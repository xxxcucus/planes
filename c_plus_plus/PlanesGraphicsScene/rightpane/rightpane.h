#ifndef RIGHTPANE_H
#define RIGHTPANE_H

#include <QTabWidget>
#include <QSettings>
#include <QNetworkAccessManager>
#include <QSettings>
#include "boards/playerboard.h"
#include "boards/computerboard.h"
#include "planeround.h"
#include "global/globaldata.h"
#include "gameinfo.h"
#include "multiplayerround.h"
#include "chat/chatwidget.h"

class RightPane : public QTabWidget
{
    Q_OBJECT
public:
    RightPane(PlaneRound *pr, MultiplayerRound* mrd, QSettings *settings, GlobalData* globalData, QNetworkAccessManager* networkManager, GameInfo* gameInfo, StompClient* stompClient, QWidget* parent = nullptr);
    ~RightPane();

    void setMinWidth();

public slots:
    inline void resetGameBoard() {
        m_PlayerBoard->reset();
        m_ComputerBoard->reset();
    }
    void selectPlaneClicked(bool);
    void rotatePlaneClicked(bool);
    void upPlaneClicked(bool);
    void downPlaneClicked(bool);
    void leftPlaneClicked(bool);
    void rightPlaneClicked(bool);

    /**
     * @brief Switch to computer tab and start looking for planes.
     * Change the internal state of the player's and computer's boards to game stage
     */
    void doneClicked();

    /**
     * @brief Display winner message in the player and computer boards.
     * Block mouse click events in the computer board.
     */
    void endRound(bool isPlayerWinner, bool isDraw);

    void startNewGame();

	void showComputerMove(const GuessPoint& gp);
    
    void multiplayerRoundReset(bool exists, const QString& gameName, const QString& firstPlayerName, const QString& secondPlayerName, const QString& currentRoundId);
    void gameConnectedToSlot(const QString& gameName, const QString& firstPlayerName, const QString& secondPlayerName, const QString& currentRoundId);
    
    void roundWasCancelledSlot();
    void loginCompleted();
    void logoutCompleted();
    
signals:
    void planePositionNotValid(bool);

    void guessMade(const GuessPoint& gp);
private:
    PlayerBoard* m_PlayerBoard;
    ComputerBoard* m_ComputerBoard;
	PlaneRound* m_PlaneRound; //for setting options
	MultiplayerRound* m_MultiRound;
	QSettings* m_Settings; //TODO: to move planesgswindow
    GlobalData* m_GlobalData;
    QNetworkAccessManager* m_NetworkManager;
    StompClient* m_StompClient;
    GameInfo* m_GameInfo;
    ChatWidget* m_ChatWidget;
    
    int m_OwnBoardIndex = 0;
    int m_OpponentBoardIndex = 0;
    int m_OptionsIndex = 0;
    int m_HelpIndex = 0;
    int m_AboutIndex = 0;
    int m_ChatWidgetIndex = 0;

};

#endif // RIGHTPANE_H

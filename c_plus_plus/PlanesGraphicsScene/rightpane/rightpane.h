#ifndef RIGHTPANE_H
#define RIGHTPANE_H

#include <QTabWidget>
#include <QSettings>
#include <QNetworkAccessManager>
#include "boards/playerboard.h"
#include "boards/computerboard.h"
#include "planeround.h"
#include "userdata.h"
#include "gameinfo.h"

class RightPane : public QTabWidget
{
    Q_OBJECT
public:
    explicit RightPane(PlaneGrid* pGrid, PlaneGrid* cGrid, PlaneRound *pr, UserData* userData, QNetworkAccessManager* networkManager, GameInfo* gameInfo, QWidget* parent = nullptr);
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

signals:
    void planePositionNotValid(bool);

    void guessMade(const GuessPoint& gp);
private:
    PlayerBoard* m_PlayerBoard;
    ComputerBoard* m_ComputerBoard;
	PlaneRound* m_PlaneRound; //for setting options
	QSettings* m_Settings; //TODO: to move planesgswindow
    UserData* m_UserData;
    QNetworkAccessManager* m_NetworkManager;
    GameInfo* m_GameInfo;
    
    int m_OwnBoardIndex = 0;
    int m_OpponentBoardIndex = 0;
    int m_AccountWidgetIndex = 0;
    int m_GameWidgetIndex = 0;
private:
    void defineHelpWindow(QWidget* w); //TODO: is this still used ?
};

#endif // RIGHTPANE_H

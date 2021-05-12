#ifndef PLANESGSLEFTPANE_H
#define PLANESGSLEFTPANE_H

#include <QTabWidget>
#include <QPushButton>
#include <QNetworkAccessManager>
#include <QMessageBox>
#include <QNetworkReply>
#include <QSettings>
#include <QJsonObject>
#include "gamestatistics.h"
#include "gameinfo.h"
#include "global/globaldata.h"
#include "multiplayerround.h"
#include "round/playroundwidget.h"
#include "boardediting/boardeditingwidget.h"
#include "startnewround/startnewroundwidget.h"
#include "account/mainaccountwidget.h"
#include "game/gamewidget.h"

class LeftPane : public QTabWidget
{
    Q_OBJECT
public:
    LeftPane(GameInfo* gameInfo, QNetworkAccessManager* networkManager, GlobalData* globalData, QSettings* settings, MultiplayerRound* mrd, QWidget *parent = 0);

    void activateGameTab();
    void activateEditorTab();
    void activateStartGameTab();
    void setMinWidth();
    void setMinHeight();

signals:
    void selectPlaneClicked(bool);
    void rotatePlaneClicked(bool);
    void upPlaneClicked(bool);
    void downPlaneClicked(bool);
    void leftPlaneClicked(bool);
    void rightPlaneClicked(bool);
    void doneClicked();
    void newRoundStarted();
    void roundWasCancelled();

public slots:
    /**
     * @brief When planes overlap deactivate the done button
     * @param planesOverlap - received info from corresponding signal
     */
    void activateDoneButton(bool planesOverlap);

    /**
     * @brief Activate the game tab when the done button is clicked
     */
    void doneClickedSlot();

    /**
     * @brief Sends the corresponding signal or shows warning message 
     * when in multiplayer mode and no round was started
     */
    void selectPlaneClickedSlot(bool c);
    /**
     * @brief Sends the corresponding signal or shows warning message 
     * when in multiplayer mode and no round was started
     */
    void rotatePlaneClickedSlot(bool c);
    /**
     * @brief Sends the corresponding signal or shows warning message 
     * when in multiplayer mode and no round was started
     */
    void upPlaneClickedSlot(bool c);
    /**
     * @brief Sends the corresponding signal or shows warning message 
     * when in multiplayer mode and no round was started
     */
    void downPlaneClickedSlot(bool c);
    /**
     * @brief Sends the corresponding signal or shows warning message 
     * when in multiplayer mode and no round was started
     */
    void leftPlaneClickedSlot(bool c);
    /**
     * @brief Sends the corresponding signal or shows warning message 
     * when in multiplayer mode and no round was started
     */
    void rightPlaneClickedSlot(bool c);


    /**
     * @brief Query the server for the opponent's plane positions
     */
    void acquireOpponentPositionsClickedSlot(bool c);    
    void acquireOpponentPositionsTimeoutSlot();
    void acquireOpponentMovesClickedSlot(bool c);
    void acquireOpponentMovesSlot();
    void acquireOpponentMovesTimeoutSlot();
    
    void startNewGameSlot();
    
    /**
     * @brief activates the editing board tab and the buttons in it
     */
    void activateEditingBoard();

    /**
     * @brief Updates the statistics in the left pane
     */
    void updateGameStatistics(const GameStatistics& gs);

    /**
     * @brief
     * Hide the other tabs.
     */

    void endRound(bool isPlayerWinner);
     
    void roundWasCancelledSlot();
    void cancelRoundClickedSlot(bool b);
    
    void activateGameTabDeactivateButtons();
    void WaitForOpponentPlanesPositionsSlot();
    
    void startNewRound();
    void activateAccountWidget();
    void activateGameWidget();
    
    void currentTabChangedSlot();
    
private:
    void submitDoneClicked();    
    
private:
    
    PlayRoundWidget* m_PlayRoundWidget;
    BoardEditingWidget* m_BoardEditingWidget;
    StartNewRoundWidget* m_StartNewRoundWidget;
    MainAccountWidget* m_MainAccountWidget;
    GameWidget* m_GameWidget;

    int m_MainAccountWidgetIndex = -1;
    int m_GameWidgetIndex = -1;
    int m_GameTabIndex = -1;
    int m_EditorTabIndex = -1;
    int m_GameStartIndex = -1;

        
    GameInfo* m_GameInfo;
    QNetworkAccessManager* m_NetworkManager;
    GlobalData* m_GlobalData;
    QSettings* m_Settings;
    MultiplayerRound* m_MultiRound;
    
    QTimer* m_AcquireOpponentPlanesPositionsTimer = nullptr;
    QTimer* m_AcquireOpponentMovesTimer = nullptr;
};

#endif // PLANESGSLEFTPANE_H

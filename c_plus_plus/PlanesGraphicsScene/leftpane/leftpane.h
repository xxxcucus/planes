#ifndef PLANESGSLEFTPANE_H
#define PLANESGSLEFTPANE_H

#include <QTabWidget>
#include <QPushButton>
#include <QNetworkAccessManager>
#include <QMessageBox>
#include <QNetworkReply>
#include <QSettings>
#include <QJsonObject>
#include "gamestatsframe.h"
#include "scoreframe.h"
#include "gamestatistics.h"
#include "gameinfo.h"
#include "global/globaldata.h"
#include "multiplayerround.h"

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
    void startNewGame();

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

    void errorDoneClicked(QNetworkReply::NetworkError code);

    void finishedDoneClicked();    
    
    void finishedAcquireOpponentPositions();
    void errorAcquireOpponentPositions(QNetworkReply::NetworkError code);
 
    
    
private:
    void submitDoneClicked();
    bool validateDoneClickedReply(const QJsonObject& reply);
    
private:
    GameStatsFrame* m_PlayerStatsFrame;
    GameStatsFrame* m_ComputerStatsFrame;

    QWidget* m_GameWidget;
    QWidget* m_BoardEditingWidget;
    QWidget* m_StartGameWidget;

    int m_GameTabIndex = -1;
    int m_EditorTabIndex = -1;
    int m_GameStartIndex = -1;

    ///butons to edit the position of the planes
    QPushButton* m_selectPlaneButton;
    QPushButton* m_rotatePlaneButton;
    QPushButton* m_leftPlaneButton;
    QPushButton* m_rightPlaneButton;
    QPushButton* m_upPlaneButton;
    QPushButton* m_downPlaneButton;
    QPushButton* m_doneButton;
    QPushButton* m_acquireOpponentPositionsButton;
    
    ScoreFrame* m_ScoreFrame;
    GameInfo* m_GameInfo;
    QNetworkAccessManager* m_NetworkManager;
    GlobalData* m_GlobalData;
    QSettings* m_Settings;
    MultiplayerRound* m_MultiRound;
    
    QNetworkReply* m_DoneClickedReply = nullptr;
    QNetworkReply* m_AcquireOpponentPositionsReply = nullptr;
};

#endif // PLANESGSLEFTPANE_H

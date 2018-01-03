#ifndef PLANESGSLEFTPANE_H
#define PLANESGSLEFTPANE_H

#include <QDebug>
#include <QTabWidget>
#include <QPushButton>
#include "gamestatsframe.h"
#include "scoreframe.h"
#include "planeround.h"

class LeftPane : public QTabWidget
{
    Q_OBJECT
public:
    explicit LeftPane(QWidget *parent = 0);

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
    void doneClicked(bool);
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

    ScoreFrame* m_ScoreFrame;
};

#endif // PLANESGSLEFTPANE_H

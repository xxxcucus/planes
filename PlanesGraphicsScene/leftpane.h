#ifndef PLANESGSLEFTPANE_H
#define PLANESGSLEFTPANE_H

#include <QDebug>
#include <QTabWidget>
#include <QPushButton>
#include "gamestatsframe.h"

class LeftPane : public QTabWidget
{
    Q_OBJECT
public:
    explicit LeftPane(QWidget *parent = 0);

    inline void activateGameTab() { setCurrentIndex(m_GameTabIndex);  }
    inline void activateEditorTab() { setCurrentIndex(m_EditorTabIndex); }

signals:
    void selectPlaneClicked(bool);
    void rotatePlaneClicked(bool);
    void upPlaneClicked(bool);
    void downPlaneClicked(bool);
    void leftPlaneClicked(bool);
    void rightPlaneClicked(bool);
    void doneClicked(bool);

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

private:
    GameStatsFrame* m_PlayerStatsFrame;
    GameStatsFrame* m_ComputerStatsFrame;

    QWidget* m_GameWidget;
    QWidget* m_BoardEditingWidget;

    int m_GameTabIndex = -1;
    int m_EditorTabIndex = -1;

    ///butons to edit the position of the planes
    QPushButton* m_selectPlaneButton;
    QPushButton* m_rotatePlaneButton;
    QPushButton* m_leftPlaneButton;
    QPushButton* m_rightPlaneButton;
    QPushButton* m_upPlaneButton;
    QPushButton* m_downPlaneButton;
    QPushButton* m_doneButton;

};

#endif // PLANESGSLEFTPANE_H

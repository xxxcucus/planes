#ifndef RIGHTPANE_H
#define RIGHTPANE_H

#include <QTabWidget>
#include <QSettings>
#include "playerboard.h"
#include "computerboard.h"
#include "planeround.h"

class RightPane : public QTabWidget
{
    Q_OBJECT
public:
    explicit RightPane(PlaneGrid* pGrid, PlaneGrid* cGrid, PlaneRound *pr, QWidget* parent = nullptr);
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
	QSettings* m_Settings;
private:
    void defineHelpWindow(QWidget* w);
};

#endif // RIGHTPANE_H

#ifndef RIGHTPANE_H
#define RIGHTPANE_H

#include <QTabWidget>
#include "playerboard.h"
#include "computerboard.h"

class RightPane : public QTabWidget
{
    Q_OBJECT
public:
    explicit RightPane(PlaneGrid& pGrid, PlaneGrid& cGrid, QWidget* parent = nullptr);
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
    void endRound(bool isPlayerWinner);

    void startNewGame();

	void showComputerMove(const GuessPoint& gp);

signals:
    void planePositionNotValid(bool);

    void guessMade(const GuessPoint& gp);
private:
    PlayerBoard* m_PlayerBoard;
    ComputerBoard* m_ComputerBoard;
private:
    void defineHelpWindow(QWidget* w);
};

#endif // RIGHTPANE_H

#ifndef RIGHTPANE_H
#define RIGHTPANE_H

#include <QTabWidget>
#include "gameboard.h"


class RightPane : public QTabWidget
{
    Q_OBJECT
public:
    explicit RightPane(PlaneGrid& pGrid, PlaneGrid& cGrid, QWidget* parent = nullptr);

public slots:
    inline void resetGameBoard() { m_GameBoard->reset(); }
    void selectPlaneClicked(bool);
    void rotatePlaneClicked(bool);
    void upPlaneClicked(bool);
    void downPlaneClicked(bool);
    void leftPlaneClicked(bool);
    void rightPlaneClicked(bool);
    void doneClicked(bool);

private:
    GameBoard* m_GameBoard;

private:
    void defineHelpWindow(QWidget* w);
};

#endif // RIGHTPANE_H

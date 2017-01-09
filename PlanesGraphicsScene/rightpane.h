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

private:
    GameBoard* m_GameBoard;

private:
    void defineHelpWindow(QWidget* w);
};

#endif // RIGHTPANE_H

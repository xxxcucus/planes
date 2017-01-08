#ifndef GAMEBOARD_H
#define GAMEBOARD_H

#include <QGraphicsScene>
#include <QGraphicsView>

class GameBoard
{
public:
    GameBoard();

    inline QWidget* getView() { return m_View; }

    ///deletes all the objects in the graphicscene
    ///and creates the board for placing the planes
    inline void reset() {
        clearBoard();
        initBoardEditor();
    }

private:
    inline void clearBoard() { m_Scene->clear();}
    inline void initBoardEditor() {}

private:
    QGraphicsScene* m_Scene;
    QGraphicsView* m_View;
};

#endif // GAMEBOARD_H

#ifndef PLAYAREAGRIDSQUARE_H
#define PLAYAREAGRIDSQUARE_H

#include "gridsquare.h"

class PlayAreaGridSquare : public GridSquare
{
public:

    PlayAreaGridSquare(int row, int col, int width, QGraphicsItem* parent = 0) : GridSquare(row, col, width, parent) {}
    inline void setGameStatus(PlayAreaGridSquare::GameStatus st) {
        if (m_Status == st)
            return;
        prepareGeometryChange();
        m_Status = st;
    }

    inline void showPlane(bool val) {
        if (m_ShowPlane == val)
            return;
        prepareGeometryChange();
        m_ShowPlane = val;
    }
    inline void showGuesses(bool val) {
        if (m_ShowGuesses == val)
            return;
        prepareGeometryChange();
        m_ShowGuesses = val;
    }

    void paint(QPainter* painter, const QStyleOptionGraphicsItem* option, QWidget* widget) override;

private:
    void drawCommonGraphics(QPainter* painter);
    void drawPlaneGuessed(QPainter* painter);
    void drawPlaneHeadGuessed(QPainter* painter);
    void drawTestedNotPlane(QPainter* painter);

private:
    PlayAreaGridSquare::GameStatus m_Status = GameStatus::Empty;
    bool m_ShowPlane = true;
    bool m_ShowGuesses = false;
};

#endif // PLAYAREAGRIDSQUARE_H

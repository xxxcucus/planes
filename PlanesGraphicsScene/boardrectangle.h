#ifndef BOARDRECTANGLE_H
#define BOARDRECTANGLE_H

#include <QGraphicsItem>

class BoardRectangle : public QGraphicsItem
{
public:
    enum class GameStatus { Empty, PlaneGuessed, PlaneHeadGuessed, TestedNotPlane };
    enum class Type { Empty, PlaneHead, Plane };

    explicit BoardRectangle(int row, int col, int width, QGraphicsItem* parent = 0);
    virtual ~BoardRectangle() {}

    inline void setWidth(int width) {
        if (m_Width == width)
            return;
        prepareGeometryChange();
        m_Width = width;
    }
    inline int getWidth() const { return m_Width; }
    inline QRectF boundingRect() const {
        return QRectF(0, 0, m_Width, m_Width);
    }
    inline void setGameStatus(BoardRectangle::GameStatus st) {
        if (m_Status == st)
            return;
        prepareGeometryChange();
        m_Status = st;
    }
    inline void setType(BoardRectangle::Type tp) {
        if (m_Type == tp)
            return;
        prepareGeometryChange();
        m_Type = tp;
    }
    inline void showPlane(bool val) {
        if (m_ShowPlane == val)
            return;
        prepareGeometryChange();
        m_ShowPlane = val;
    }
    inline void showGuesse(bool val) {
        if (m_ShowGuesses == val)
            return;
        prepareGeometryChange();
        m_ShowGuesses = val;
    }

    void paint(QPainter* painter, const QStyleOptionGraphicsItem* option, QWidget* widget);

private:
    void drawCommonGraphics(QPainter* painter);
    void drawPlaneGuessed(QPainter* painter);
    void drawPlaneHeadGuessed(QPainter* painter);
    void drawTestedNotPlane(QPainter* painter);

private:
    BoardRectangle::Type m_Type = Type::Empty;
    BoardRectangle::GameStatus m_Status = GameStatus::Empty;
    int m_Width = 75;
    bool m_Selected = false;
    bool m_ShowPlane = false;
    bool m_ShowGuesses = false;
    int m_GridRow = -1;
    int m_GridCol = -1;
};

#endif // BOARDRECTANGLE_H

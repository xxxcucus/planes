#ifndef BOARDRECTANGLE_H
#define BOARDRECTANGLE_H

#include <QGraphicsItem>

class GridSquare : public QGraphicsItem
{
public:
    enum class Type { Empty, PlaneHead, Plane };
    enum class GameStatus { Empty, PlaneGuessed, PlaneHeadGuessed, TestedNotPlane };

    explicit GridSquare(int row, int col, int width, QGraphicsItem* parent = 0);
    virtual ~GridSquare() {}

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

    inline void setType(GridSquare::Type tp) {
        if (m_Type == tp)
            return;
        prepareGeometryChange();
        m_Type = tp;
    }

    virtual void paint(QPainter* painter, const QStyleOptionGraphicsItem* option, QWidget* widget);
    void setSelected(bool val) {
        if (m_Selected == val)
            return;
        prepareGeometryChange();
        m_Selected = val;
    }
    void clearPlaneOptions() {
        if (!m_Selected && m_Type == Type::Empty)
            return;
        prepareGeometryChange();
        m_Selected = false;
        m_Type = Type::Empty;
    }

protected:
    int m_Width = 30;
    int m_GridRow = -1;
    int m_GridCol = -1;
    bool m_Selected = false;

    GridSquare::Type m_Type = Type::Empty;
};

#endif // BOARDRECTANGLE_H

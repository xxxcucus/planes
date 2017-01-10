#ifndef BOARDRECTANGLE_H
#define BOARDRECTANGLE_H

#include <QGraphicsItem>

class GridSquare : public QGraphicsItem
{
public:

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
    virtual void paint(QPainter* painter, const QStyleOptionGraphicsItem* option, QWidget* widget);

protected:
    int m_Width = 30;
    int m_GridRow = -1;
    int m_GridCol = -1;
};

#endif // BOARDRECTANGLE_H

#include "gridsquare.h"

#include <QPainter>

GridSquare::GridSquare(int row, int col, int width, QGraphicsItem* parent) : QGraphicsItem(parent),
    m_Width(width), m_GridRow(row), m_GridCol(col)
{
}

void GridSquare::paint(QPainter* painter, const QStyleOptionGraphicsItem* option, QWidget* widget)
{
    Q_UNUSED(option)
    Q_UNUSED(widget)

    switch(m_Type) {
        case Type::Empty:
            painter->fillRect(boundingRect(), Qt::yellow);
            break;
        case Type::PlaneHead:
            painter->fillRect(boundingRect(), Qt::green);
            break;
        case Type::Plane:
            painter->fillRect(boundingRect(), Qt::black);
            break;
    }
    painter->setPen(Qt::black);
    painter->drawRect(boundingRect());
}

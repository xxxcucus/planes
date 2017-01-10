#include "gridsquare.h"

#include <QPainter>

GridSquare::GridSquare(int row, int col, int width, QGraphicsItem* parent) : QGraphicsItem(parent),
    m_Width(width), m_GridRow(row), m_GridCol(col)
{
}

void GridSquare::paint(QPainter* painter, const QStyleOptionGraphicsItem* option, QWidget* widget)
{
    Q_UNUSED(option)
    painter->fillRect(boundingRect(), Qt::yellow);
    painter->setPen(Qt::black);
    painter->drawRect(boundingRect());
}

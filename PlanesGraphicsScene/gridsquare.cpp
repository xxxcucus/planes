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

    QPainterPath fillPath;
    fillPath.addRoundedRect(boundingRect().adjusted(m_Width/10, m_Width/10, -m_Width/10, -m_Width/10), 5, 5);
    painter->setRenderHint(QPainter::Antialiasing);

    if (!m_Selected) {
        switch(m_Type) {
            case Type::Empty:
                painter->fillPath(fillPath, Qt::yellow);
                painter->strokePath(fillPath, QPen(Qt::black));
                break;
            case Type::PlaneHead:
                painter->fillPath(fillPath, Qt::green);
                painter->strokePath(fillPath, QPen(Qt::black));
                break;
            case Type::Plane:
                painter->fillPath(fillPath, m_Color);
                painter->strokePath(fillPath, QPen(Qt::black));
                break;
        }
    } else {
        painter->fillPath(fillPath, Qt::blue);
        painter->strokePath(fillPath, QPen(Qt::black));
    }
}

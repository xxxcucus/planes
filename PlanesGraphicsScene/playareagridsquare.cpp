#include "playareagridsquare.h"
#include <QPainter>

void PlayAreaGridSquare::paint(QPainter* painter, const QStyleOptionGraphicsItem* option, QWidget* widget) {
    Q_UNUSED(option)
    Q_UNUSED(widget)

    drawCommonGraphics(painter);

    if (m_ShowGuesses) {
        switch(m_Status) {
            case GameStatus::Empty:
                break;
            case GameStatus::PlaneGuessed:
                drawPlaneGuessed(painter);
                break;
            case GameStatus::PlaneHeadGuessed:
                drawPlaneHeadGuessed(painter);
                break;
            case GameStatus::TestedNotPlane:
                drawTestedNotPlane(painter);
                break;
        }
    }
}

void PlayAreaGridSquare::drawCommonGraphics(QPainter* painter)
{
    if (m_Selected) {
        painter->fillRect(boundingRect(), Qt::blue);
    } else {
        if (m_ShowPlane) {
            switch(m_Type) {
                case Type::Empty:
                    painter->fillRect(boundingRect(), Qt::white);
                    break;
                case Type::PlaneHead:
                    painter->fillRect(boundingRect(), Qt::green);
                    break;
                case Type::Plane:
                    painter->fillRect(boundingRect(), m_Color);
                    break;
            }
        } else {
            painter->fillRect(boundingRect(), Qt::white);
        }
    }

    if ((m_ShowPlane && m_Type != Type::Empty) || m_Selected)
        painter->setPen(Qt::magenta);
    else
        painter->setPen(Qt::black);
    painter->drawRect(boundingRect());
}

void PlayAreaGridSquare::drawPlaneGuessed(QPainter* painter)
{
    painter->setPen(Qt::red);
    painter->drawLine(0, 0, m_Width/2, m_Width);
    painter->drawLine(m_Width/2, m_Width, m_Width, 0);
}

void PlayAreaGridSquare::drawPlaneHeadGuessed(QPainter* painter)
{
    painter->setPen(Qt::red);
    painter->drawLine(0, 0, m_Width, m_Width);
    painter->drawLine(0, m_Width, m_Width, 0);
}

void PlayAreaGridSquare::drawTestedNotPlane(QPainter* painter)
{
    painter->setPen(Qt::red);
    painter->drawEllipse(m_Width/3, m_Width/3, m_Width/3, m_Width/3);
}

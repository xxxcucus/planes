#include "buttons.h"
#include <QPainter>
#include <QPainterPath>

NextMoveButton::NextMoveButton(QWidget *parent):
    QAbstractButton(parent), m_originalWidth(20),m_originalHeight(20)
{
    m_width = m_originalWidth;
    m_height = m_originalHeight;

    setSizePolicy(QSizePolicy::Fixed,QSizePolicy::Fixed);
}


void NextMoveButton::paintEvent(QPaintEvent */*e*/)
{
    QPainter painter(this);

    QPainterPath pathTriangle;
    pathTriangle.moveTo(0,m_height);
    pathTriangle.lineTo(m_width,m_height/2);
    pathTriangle.lineTo(0,0);
    pathTriangle.lineTo(0,m_height);


    QPainterPath pathSquare;
    pathSquare.moveTo(0,0);
    pathSquare.lineTo(0,m_height);
    pathSquare.lineTo(m_width,m_height);
    pathSquare.lineTo(m_width,0);
    pathSquare.lineTo(0,0);

    if(!isEnabled())
    {
        painter.fillPath(pathSquare,QColor("Darkgray"));
        painter.fillPath(pathTriangle,QColor("Grey"));
        return;
    }


    if(!isDown())
    {
        painter.fillPath(pathSquare,QColor("Black"));
        painter.fillPath(pathTriangle,QColor("Grey"));
    }
    else
    {
        painter.fillPath(pathSquare,QColor("Grey"));
        painter.fillPath(pathTriangle,QColor("Black"));
    }


}


QSize NextMoveButton::sizeHint() const
{
    return QSize(m_width,m_height);
}


PreviousMoveButton::PreviousMoveButton(QWidget* parent):
    NextMoveButton(parent)
{

}


void PreviousMoveButton::paintEvent(QPaintEvent */*e*/)
{
    QPainter painter(this);

    QPainterPath pathTriangle;
    pathTriangle.moveTo(0,m_height/2);
    pathTriangle.lineTo(m_width,m_height);
    pathTriangle.lineTo(m_width,0);
    pathTriangle.lineTo(0,m_height/2);


    QPainterPath pathSquare;
    pathSquare.moveTo(0,0);
    pathSquare.lineTo(0,m_height);
    pathSquare.lineTo(m_width,m_height);
    pathSquare.lineTo(m_width,0);
    pathSquare.lineTo(0,0);


    if(!isEnabled())
    {
        painter.fillPath(pathSquare,QColor("Darkgray"));
        painter.fillPath(pathTriangle,QColor("Grey"));
        return;
    }


    if(!isDown())
    {
        painter.fillPath(pathSquare,QColor("Black"));
        painter.fillPath(pathTriangle,QColor("Grey"));
    }
    else
    {
        painter.fillPath(pathSquare,QColor("Grey"));
        painter.fillPath(pathTriangle,QColor("Black"));
    }


}

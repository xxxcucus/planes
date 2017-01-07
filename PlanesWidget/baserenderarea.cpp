#include "baserenderarea.h"
#include <QPainter>
#include "planegame.h"



//constructs the class object

BaseRenderArea::BaseRenderArea(int row, int col, QWidget *parent):
    QWidget(parent),
    m_rowNo(row), m_colNo(col),
    m_spacing(20),
    m_offsetRow(20),
    m_offsetCol(20)

{

    //sets the size policy as expanding ; we want the widget to nicely resize when we resize the widget
    setSizePolicy(QSizePolicy::Expanding, QSizePolicy::Expanding);
    setWindowTitle("");

}

//gives the minimum size hint for this widget

QSize BaseRenderArea::minimumSizeHint() const
{
    return calculateSizeHint(20,20,20);
}

//calculates the size hint for the widget

QSize BaseRenderArea::sizeHint() const
{
    return calculateSizeHint(m_spacing, m_offsetRow, m_offsetCol);

}


//calculates the proper sizeHint
QSize BaseRenderArea::calculateSizeHint(int spacing, int offsetRow, int offsetCol) const
{
    return QSize((m_rowNo+2)*spacing+offsetRow, (m_colNo+1)*spacing+offsetCol);
}

//draws the grid, the numbers on the side and the title

void BaseRenderArea::paintEvent(QPaintEvent* /*event*/)
{
    //calculates the dimensions of the grid as a function of the window size
    calculateDimensions();

    QPainter painter(this);
    //draws the grid of the game
    drawGrid(&painter);

    //draw the numbers and the title
    drawTexts(&painter);
}

//calculates the dimensions of the grid
//the spacing between two consecutive lines, m_spacing
//the offset of the left top corner with the
//grid with respect to the widget

void BaseRenderArea::calculateDimensions()
{
    //numbers of rows and columns left as margin
    int noRowMargin = m_rowNo/8;
    int noColMargin = m_colNo/8;
    //total number of rows and columns including margin
    int noVirtualRows = m_rowNo+2+2*noRowMargin;
    int noVirtualCols = m_colNo+1+2*noColMargin;

    //the spacing between the lines
    m_spacing = std::min(this->geometry().height()/noVirtualRows, this->geometry().width()/noVirtualCols);
    //the offset of the grid with respect to the widget
    m_offsetRow = (this->geometry().width()- m_spacing*(m_colNo+1))/2;
    m_offsetCol = (this->geometry().height() - m_spacing*(m_rowNo+2))/2;

}


//draws the grid lines

void BaseRenderArea::drawGrid(QPainter *painter) const
{

    //draw horizontal lines
    for(int i=0;i<=m_colNo;i++)
        painter->drawLine(m_offsetRow+m_spacing,(i+2)*m_spacing+m_offsetCol,(m_rowNo+1)*m_spacing+m_offsetRow,(i+2)*m_spacing+m_offsetCol);

    //draw vertical lines
    for(int i=0;i<=m_rowNo;i++)
        painter->drawLine((i+1)*m_spacing+m_offsetRow,m_offsetCol+2*m_spacing,(i+1)*m_spacing+m_offsetRow,(m_colNo+2)*m_spacing+m_offsetCol);
}

//draw texts and title

void BaseRenderArea::drawTexts(QPainter *painter) const
{
    //draw vertical texts
    for(int i=0;i<m_colNo;i++)
        painter->drawText(m_offsetRow,(i+2)*m_spacing+m_offsetCol+m_spacing*2/3,QString::number(i));

    //draw horizontal texts
    for(int i=0;i<m_rowNo;i++)
        painter->drawText((i+1)*m_spacing+m_offsetRow,m_offsetCol+3*m_spacing/2,QString::number(i));

    //displays the window title
    QFontMetrics fm = this->fontMetrics();
    int titleLength = fm.width(windowTitle());


    painter->drawText(m_rowNo/2*m_spacing+m_spacing+ m_offsetRow-titleLength/2, m_offsetCol, windowTitle());
}


//checks whether a given position is in grid
bool BaseRenderArea::posInGrid(int row, int col) const
{
    if(row<0 || row>=m_rowNo || col<0 || col>=m_colNo)
        return false;

    return true;
}


//draws a miss
//this will be a small circle

void BaseRenderArea::drawMiss(int row, int col, QPainter *painter) const
{
    if(!posInGrid(row,col))
        return;

    painter->drawEllipse((row+1)*m_spacing+m_spacing/3+m_offsetRow, (col+2)*m_spacing+m_spacing/3+m_offsetCol, m_spacing/3, m_spacing/3);
}

//draw a hit
//this will be a triangle

void BaseRenderArea::drawHit(int row, int col, QPainter *painter) const
{
    if(!posInGrid(row,col))
        return;

    painter->drawLine((row+1)*m_spacing+m_offsetRow, (col+3)*m_spacing+m_offsetCol, (row+1)*m_spacing+m_offsetRow+m_spacing/2, (col+2)*m_spacing+m_offsetCol);
    painter->drawLine((row+1)*m_spacing+m_offsetRow+m_spacing/2, (col+2)*m_spacing+m_offsetCol, (row+2)*m_spacing+m_offsetRow, (col+3)*m_spacing+m_offsetCol);
    painter->drawLine((row+2)*m_spacing+m_offsetRow, (col+3)*m_spacing+m_offsetCol, (row+1)*m_spacing+m_offsetRow, (col+3)*m_spacing+m_offsetCol);
}

//draw a dead
//this will be an X

void BaseRenderArea::drawDead(int row, int col, QPainter *painter) const
{
    if(!posInGrid(row,col))
        return;

    painter->drawLine((row+1)*m_spacing+m_offsetRow, (col+2)*m_spacing+m_offsetCol, (row+2)*m_spacing+m_offsetRow, (col+3)*m_spacing+m_offsetCol);
    painter->drawLine((row+2)*m_spacing+m_offsetRow, (col+2)*m_spacing+m_offsetCol, (row+1)*m_spacing+m_offsetRow, (col+3)*m_spacing+m_offsetCol);

}

//draws a number in the grid
void BaseRenderArea::drawNumber(int row, int col, int val, QPainter *painter) const
{
    if(!posInGrid(row,col))
        return;


    painter->drawText(m_offsetRow+(row+1)*m_spacing, m_offsetCol+(col+2)*m_spacing,m_spacing,m_spacing, Qt::AlignHCenter, QString::number(val));
}

//draws a list of guesses in the grid
void BaseRenderArea::drawGuesses(const QList <GuessPoint> &list, QPainter *painter) const
{

    QPen currentPen = painter->pen();


    painter->setPen(QString("red"));

    for(int i=0;i<list.size();i++)
    {
        GuessPoint gp = list.at(i);

        switch(gp.m_type)
        {
        case GuessPoint::Miss:
            drawMiss(gp.m_row, gp.m_col, painter);
            break;
        case GuessPoint::Dead:
            drawDead(gp.m_row, gp.m_col, painter);
            break;
        case GuessPoint::Hit:
            drawHit(gp.m_row, gp.m_col, painter);
            break;
        default:
            ;
        }
    }

    painter->setPen(currentPen);

}


//fills a grid rect with a given color
//before the filling of the rect the validity of the coordinates is tested
//returns false if coordinates are not good and true otherwise
bool BaseRenderArea::fillGridRect(int row, int col, QString color, QPainter *painter) const
{
    if(!posInGrid(row,col))
        return false;

    painter->fillRect((row+1)*m_spacing+m_offsetRow,(col+2)*m_spacing+m_offsetCol,m_spacing,m_spacing,QColor(color));
    return true;
}

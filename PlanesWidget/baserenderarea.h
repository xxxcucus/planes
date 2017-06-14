#ifndef BASERENDERAREA_H
#define BASERENDERAREA_H
#include <QWidget>
#include "plane.h"
#include "guesspoint.h"

//Base class for displaying grids
//It is a automatically scalable widget
//that shows a grid with a title and
//numbers marking the columns

//It can also draw numbers, hits,
//misses and dead positions on the grid
//as well as lists of guesses


class BaseRenderArea : public QWidget
{
protected:

    //number of rows and columns in the grid
    int m_rowNo, m_colNo;
    // the spacing between grid lines
    int m_spacing;
    // the offset of the grid relative to the origin of the widgets client area
    int m_offsetRow, m_offsetCol;


protected:
    //the widget's paint event
    void paintEvent(QPaintEvent *event);

public:
    BaseRenderArea( int row, int col,QWidget *parent=0);
    //minimum size for layout managers
    QSize minimumSizeHint() const;
    //the size hint for layout managers
    QSize sizeHint() const;



protected:

    //checks whether a given position is in grid
    bool posInGrid(int row, int col) const;
    //draws a number in the grid
    void drawNumber(int row, int col, int val,QPainter *painter) const;
    //draws a list of guesses
    void drawGuesses(const QList <GuessPoint> &list, QPainter *painter) const;
    //fills a rect in the grid with the color
    bool fillGridRect(int row, int col, QString color, QPainter *painter) const;

private:

    //calculates the characteristic dimensions
    //of the widget for automatic rescaling
    void calculateDimensions();
    //calculates the proper sizeHint
    QSize calculateSizeHint(int,int,int) const;


    //draws the grid
    void drawGrid(QPainter *painter) const;
    //draws the numbers on the two axis
    void drawTexts(QPainter *painter) const;

    //draws a miss
    void drawMiss(int row, int col, QPainter *painter) const;
    //draw a hit
    void drawHit(int row, int col, QPainter *painter) const;
    //draw a dead
    void drawDead(int row, int col, QPainter *painter) const;




};

#endif // BaseRenderArea_H

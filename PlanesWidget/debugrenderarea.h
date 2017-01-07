#ifndef DEBUGRENDERAREA_H
#define DEBUGRENDERAREA_H

#include "computerlogic.h"
#include <QWidget>
#include <QPainter>
#include "baserenderarea.h"

//this is a widget that displays how the computer evaluates choices
//before deciding one move
class DebugRenderArea:public BaseRenderArea
{

    //a pointer to a revert computer logic object that is used to display the data
    RevertComputerLogic *m_logic;
    //the orientation that is to be displayed in this render area
    Plane::Orientation m_orient;

protected:
    //the widget's paint event
    void paintEvent(QPaintEvent *event);


public:
    DebugRenderArea(RevertComputerLogic* logic, Plane::Orientation orient, QWidget *parent = 0);


private:

    //draws the guesses made until now
    void drawGuesses(QPainter *painter) const;
    //draws the scores in the m_choices array
    void drawScores(QPainter *painter) const;

    //utility function
    //gives the grid's title based on the m_orient data member
    QString title() const;


};

#endif // DebugRenderArea_H

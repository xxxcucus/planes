#include "debugrenderarea.h"


//logic contains all the information extracted from the guesses made by the computer
//orient keeps the orientation corresponding to the grid
//there can be four orientations: North-South, South-North, East-West, West-East
DebugRenderArea::DebugRenderArea(RevertComputerLogic* logic,Plane::Orientation orient, QWidget *parent):
    BaseRenderArea(logic->getRowNo(),logic->getColNo(),parent),
    m_logic(logic),
    m_orient(orient)

{

    setWindowTitle(title());
    //set the window title

}

//gives the grid's title
QString DebugRenderArea::title() const
{
    switch(m_orient)
    {
        case Plane::EastWest:
            return QString("East-West");
        case Plane::WestEast:
            return QString("West-East");
        case Plane::SouthNorth:
            return QString("South-North");
        case Plane::NorthSouth:
            return QString("North-South");
    }

    return QString("");
}


//the paint event
//uses the base class to draw the texts and
//the grid
//and then draws the guesses made until now
//as well as the score for each choice of the computer
void DebugRenderArea::paintEvent(QPaintEvent *event)
{


    BaseRenderArea::paintEvent(event);

    QPainter painter(this);

    //draw scores
    drawScores(&painter);

    //to do: draw guesses
    drawGuesses(&painter);


}


//shows the score that the computer assigns to
//each of his possible moves
void DebugRenderArea::drawScores(QPainter *painter) const
{
    for(int i=0;i<m_rowNo;i++)
        for(int j=0;j<m_colNo;j++)
        {
            Plane pl(i,j,m_orient);
            int idx = m_logic->mapPlaneToIndex(pl);
            const int *values = m_logic->getChoicesArray();
            if(values[idx]==0)
                fillGridRect(i,j,QString("Yellow"),painter);
            if(values[idx]>0)
                fillGridRect(i,j,QString("Cyan"),painter);
            if(values[idx]!=-2)
                drawNumber(i,j,values[idx], painter);

        }
}


//draws the guesses made until this moment and highlights the last guess made
void DebugRenderArea::drawGuesses(QPainter *painter) const
{
    std::vector<GuessPoint> list = m_logic->getListGuesses();
    if (!list.empty())
    {
        GuessPoint gp = list.back();
        fillGridRect(gp.m_row, gp.m_col, "Blue", painter);
    }
    std::vector<GuessPoint> elist = m_logic->getExtendedListGuesses();
    BaseRenderArea::drawGuesses(elist, painter);
}


//sets the current state of the computer strategy in the object that can revert the strategy


#include "gamerenderarea.h"
#include <QtGui>
#include <QDebug>


//constructs the object
//m_currentOperation is the current operation in player editor mode
//m_temp plane is the position of the temporary plane in player editor mode
//m_planePosValid is the validity of the last drawn plane
//m_planeSelected keeps whether a plane is selected or not
//m_grid keeps a pointer to a PlaneGrid object
GameRenderArea::GameRenderArea(PlaneGrid* grid, QWidget* parent):
    BaseRenderArea(grid->getRowNo(),grid->getColNo(), parent),
    m_currentOperation(No_operation),
    m_tempPlane(-5,-5,Plane::NorthSouth),
    m_planePosValid(true),
    m_planeSelected(false),
    m_grid(grid)

{
    //enable the mouse tracking to be able to detect mouse motion
    setMouseTracking(true);
    setMode(Editor);

    if(isComputer())
        setWindowTitle("Computer");
    else
        setWindowTitle("Player");
}



void GameRenderArea::paintEvent(QPaintEvent *event)
{
    BaseRenderArea::paintEvent(event);

    QPainter painter(this);

    //if is computer
    //draws the planes when the game has endet
    //or colors the grid location selected
    //with the mouse during the game
    if(m_currentMode==Game && isComputer())
    {
        if(m_roundEndet)
            drawConfirmedPlanes(&painter);
        if(!m_roundEndet)
            fillGridRect(m_curMouseRow, m_curMouseCol, QString("grey"),&painter  );
        drawGuesses(&painter);
    }

    //in game mode for player
    //draws the planes
    //and the guesses made by the computer
    if(m_currentMode==Game && !isComputer())
    {
        drawConfirmedPlanes(&painter);
        drawGuesses(&painter);
    }


    //in editor mode for player    
    if(m_currentMode==Editor && !isComputer())
    {
        //draws the planes
        drawConfirmedPlanes(&painter);

        //draws the temp plane
        if(m_currentOperation==Add_plane)
            drawTempPlane(&painter);

        if(m_currentOperation==Move_plane && m_planeSelected)
            drawTempPlane(&painter);

        if(m_currentOperation==Rotate_plane && m_planeSelected)
            drawTempPlane(&painter);
    }

}


//sets the current operation in editor mode for player
bool GameRenderArea::setOperation(Operation o)
{
    if((o==Delete_plane || o==Rotate_plane || o==Move_plane) && m_grid->getPlaneListSize()==0)
        return false;

    m_currentOperation = o;

    if(o==Add_plane)
        m_tempPlane.orientation(Plane::NorthSouth);
    update();

    return true;
}

//sets the current mode
void GameRenderArea::setMode(Mode m)
{
    m_currentMode = m;
}


//fills a grid rect with a given color
//before the filling of the rect the validity of the coordinates is tested
//returns false if coordinates are not good and true otherwise
void GameRenderArea::fillGridRect(int row, int col, QString color, QPainter *painter)
{
    if(!BaseRenderArea::fillGridRect(row,col,color,painter))
        m_planePosValid = false;

}

//draws the guesses made
void GameRenderArea::drawGuesses(QPainter *painter) const
{

    BaseRenderArea::drawGuesses(m_guessPointList, painter);
}


//draws the temporary plane
void GameRenderArea::drawTempPlane(QPainter *painter)
{
    drawPlane(m_tempPlane, QString("grey"), painter);
}

//treats the mouse movement event
//only two situations are interesting
//when the player edits the plane positions
//and when the player guesses the planes
//in computer's grid
void GameRenderArea::mouseMoveEvent(QMouseEvent *event)
{
    //extracts the location of the event
    int row = event->pos().x();
    int col = event->pos().y();

    m_curMouseRow = (row-m_offsetRow)/m_spacing-1;
    m_curMouseCol = (col-m_offsetCol)/m_spacing-2;


    if(m_currentMode == Editor && !isComputer())
    {
        if(m_currentOperation!=Rotate_plane || !m_planeSelected)
        {

            m_tempPlane.row(m_curMouseRow);
            m_tempPlane.col(m_curMouseCol);
        }
    }

    update();
}



//treats the mouse pressed events
void GameRenderArea::mousePressEvent(QMouseEvent *event)
{
    if(m_currentMode == Game && isComputer() &&!m_roundEndet)
        mousePressEventComputerGame(event);

    if(m_currentMode == Editor && !isComputer())
       mousePressEventPlayerEditor(event);
}

//mouse event handler for player when decides the position of the planes
void GameRenderArea::mousePressEventPlayerEditor(QMouseEvent *event)
{
    if(event->button() == Qt::RightButton)
    {
        if(m_currentOperation==Add_plane || (m_currentOperation==Move_plane && m_planeSelected)
                || (m_currentOperation==Rotate_plane && m_planeSelected))
        {
            if(m_planePosValid)
            {

                //save the plane in the list of planes
                saveTempPlane();
                resetOperation();

            }
        }
    }

    if(event->button() == Qt::LeftButton)
    {
        if(m_currentOperation==Move_plane || m_currentOperation==Rotate_plane)
        {
            if(!m_planeSelected)
                {
                    if(selectPlane())
                    {
                        if(m_currentOperation==Move_plane)
                            emit displayMsg(tr("Right click to decide the new position of the plane."));
                        if(m_currentOperation==Rotate_plane)
                            emit displayMsg(tr("Click to rotate plane. Right click to decide position."));
                    }
                }
        }
        if(m_currentOperation==Rotate_plane)
        {
            if(m_planeSelected)
                rotateTempPlane();
        }

        if(m_currentOperation==Delete_plane)
        {
            if(selectPlane())
                resetOperation();
        }
    }
}

//event handler for mouse button pressed when the player tries to guess the position of the
//planes in the computer grid
void GameRenderArea::mousePressEventComputerGame(QMouseEvent *event)
{
    if(event->button() == Qt::LeftButton)
    {
        //queries the m_grid object
        //about the point currently selected with the mouse
        QPoint qp(m_curMouseRow, m_curMouseCol);
        GuessPoint::Type tp= m_grid->getGuessResult(qp);

        //the m_grid object returns whether is a miss, hit or dead
        //with this data builda guess point object
        GuessPoint gp(qp.x(), qp.y(), tp);

        //verify if the guess point is not already in the list
        //emit a signal that the guess has been made
        if(m_guessPointList.indexOf(gp)==-1)
        {
            m_guessPointList.append(gp);
            //to not let the user draw while the computer is thinking
            m_currentMode = Editor;
            emit guessMade(gp);
            update();
        }
    }
}

//saves the temporary plane in the m_grid object
void GameRenderArea::saveTempPlane()
{
    m_grid->savePlane(m_tempPlane);
}

//saves a plane in the m_grid object
void GameRenderArea::savePlane(Plane pl)
{
    m_grid->savePlane(pl);
}

//checks that more planes need to be added
bool GameRenderArea::addMorePlanes() const
{

    return (m_grid->getPlaneListSize()<m_grid->getPlaneNo());
}

//when the user cancels a rotation or movement operation
//restore the moved or rotated plane to the initial position
void GameRenderArea::restoreBackupPlane()
{
    //restore the backup plane and save in the list of planes
    //when the current operation is rotation or movement
    if(m_currentOperation == Rotate_plane || m_currentOperation == Move_plane)
    {
        m_tempPlane = m_backupPlane;
        saveTempPlane();
    }

}

//resets the operation in editor player mode
void GameRenderArea::resetOperation()
{

    m_currentOperation = No_operation;
    m_planeSelected = false;
    m_tempPlane.orientation(Plane::NorthSouth);
    emit operationEndet();

    //if we added all the planes
    if(!addMorePlanes())
    {
        //signal that all planes have been drawn
        emit enoughPlanes();
        emit displayStatusMsg(tr("You have drawn all the required planes."));
        //check to see if the planes draw overlap
        if(m_grid->computePlanePointsList(false))
            emit activateDone();
        else displayStatusMsg(tr("Some planes superimpose."));
    }
    else
    {
        //calculate how many planes need to be drawn
        QString text("");
        text+= tr("You must draw ");
        text+=QString::number(m_grid->getPlaneNo()-m_grid->getPlaneListSize());
        text+=tr(" more planes");
        emit displayStatusMsg(text);
        emit notEnoughPlanes();
    }
    update();
}


//draws a plane on the render area
void GameRenderArea::drawPlane(Plane pl,QString color, QPainter *painter)
{
    m_planePosValid = true;

    //create a plane point iterator to obtain the cells occupied by the plane
    PlanePointIterator ppi(pl);

    while(ppi.hasNext())
    {
        QPoint qp = ppi.next();
        fillGridRect(qp.x(),qp.y(),color,painter);
    }
}

//draws the planes that were confirmaed
void GameRenderArea::drawConfirmedPlanes(QPainter *painter)
{
    //draws the plane shapes
    for(int i=0;i<m_grid->getPlaneListSize();i++)
    {
        Plane pl;
        if(m_grid->getPlane(i,pl))
            drawPlane(pl, QString("black"), painter);
    }

    //draws the plane heads
    for(int i=0;i<m_grid->getPlaneListSize();i++)
    {
        Plane pl;
        if(m_grid->getPlane(i,pl))
            fillGridRect(pl.row(),pl.col(), QString("green"), painter);
    }

}

//search for a plane in the m_grid object
int GameRenderArea::searchPlane(Plane pl) const
{
    return m_grid->searchPlane(pl);
}

//search for a plane in the m_grid object
int GameRenderArea::searchPlane(int row, int col) const
{
    return m_grid->searchPlane(row,col);
}

//selects a plane
bool GameRenderArea::selectPlane()
{
    //at the currently selected cell check to see if there is a plane
    int idx = searchPlane(m_curMouseRow, m_curMouseCol);
    if(idx!=-1)
    {
        //if there is get the plane object from the m_grid object
        //to the m_tempPlane object
        if(m_grid->getPlane(idx, m_tempPlane))
        {
            //save the initial position of the plane
            m_grid->getPlane(idx, m_backupPlane);

            m_planeSelected = true;
            //remove the plane from list of selected planes

            removePlane(idx);
            return true;
        }
    }

    return false;
}

//removes the plane at the given index from the m_grid object
//and returns it
bool GameRenderArea::removePlane(int idx)
{
    Plane pl;
    return m_grid->removePlane(idx,pl);
}

//rotates the temp plane
void GameRenderArea::rotateTempPlane()
{
    m_tempPlane.rotate();
    update();
}

//checks to see if the temp plane intersects other planes
bool GameRenderArea::tempPlaneIntersectsOtherPlanes() const
{
    PlanePointIterator ppi(m_tempPlane);

    while(ppi.hasNext())
    {
        QPoint qp = ppi.next();
        int idx = 0;
        if(m_grid->isPointOnPlane(qp.x(), qp.y(), idx))
            return true;
    }

    return false;
}

//changes the current mode of the render area
void GameRenderArea::changeMode()
{
    QString text="";

    if(m_currentMode==Editor)
        {
        m_currentMode = Game;
        text+=tr("Play! Guess a point on the Computer's grid");
        }
    else
    {
        m_currentMode = Editor;
        text+=tr("Draw your planes!");
    }
    displayStatusMsg(text);
    update();
}

//resets the render area
void GameRenderArea::reset()
{
    m_currentMode = Editor;
    m_currentOperation = No_operation;
    m_guessPointList.clear();
    update();
}

//adds to the list of guess points
void GameRenderArea::showMove(GuessPoint gp)
{
    //here should check for repeating elements

    m_guessPointList.append(gp);
    update();
}

//switches ti game mode
void GameRenderArea::activateGameMode()
{
    m_currentMode = Game;
    m_roundEndet = false;
}

//switches to editor mode
void GameRenderArea::activateEditorMode()
{
    m_currentMode = Editor;
}

//marks that the round is endet: a winner has been selected
void GameRenderArea::roundEndet()
{
    m_roundEndet = true;
    //make sure that the current mode is game
    m_currentMode = Game;
}

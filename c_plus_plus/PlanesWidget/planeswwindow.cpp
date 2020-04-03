#include "planeswwindow.h"

PlanesWWindow::PlanesWWindow(QWidget *parent) :
    QMainWindow(parent)
{
    //builds the game object - the controller
    mRound = new PlaneRound(10, 10, 3);

    //builds the view object
    mPlanesView = new PlanesWView(mRound);
    setCentralWidget(mPlanesView);

    //starts the game
    mRound->initRound();
}


PlanesWWindow::~PlanesWWindow()
{
    delete mPlanesView;
    delete mRound;
}

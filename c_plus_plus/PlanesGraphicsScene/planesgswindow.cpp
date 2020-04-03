#include "planesgswindow.h"

PlanesGSWindow::PlanesGSWindow(QWidget *parent) : QMainWindow(parent)
{
    //builds the game object - the controller
    mRound = new PlaneRound(10, 10, 3);

    //builds the view object
    mPlanesView = new PlanesGSView(mRound);
    setCentralWidget(mPlanesView);

    //starts the game
    //mRound->initRound();
}

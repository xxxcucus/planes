#include "planesgswindow.h"

PlanesGSWindow::PlanesGSWindow(QWidget *parent) : QMainWindow(parent)
{

    //builds the model object
    mPlanesModel = new PlanesModel(10,10,3);

    //builds the game object - the controller
    mRound = new PlaneRound(mPlanesModel->playerGrid(), mPlanesModel->computerGrid(), mPlanesModel->computerLogic(), true);

    //builds the view object
    mPlanesView = new PlanesGSView(mPlanesModel->playerGrid(), mPlanesModel->computerGrid(), mPlanesModel->computerLogic(), mRound);
    setCentralWidget(mPlanesView);

    //starts the game
    mRound->play();
}

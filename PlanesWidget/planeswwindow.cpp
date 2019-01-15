#include "planeswwindow.h"

PlanesWWindow::PlanesWWindow(QWidget *parent) :
    QMainWindow(parent)
{

    //builds the model object
    mPlanesModel = new PlanesModel(10, 10, 3);

    //builds the game object - the controller
    mRound = new PlaneRoundJavaFx(mPlanesModel->playerGrid(), mPlanesModel->computerGrid(), mPlanesModel->computerLogic(), true);

    //builds the view object
    mPlanesView = new PlanesWView(mPlanesModel->playerGrid(), mPlanesModel->computerGrid(), mPlanesModel->computerLogic(), mRound);
    setCentralWidget(mPlanesView);

    //starts the game
    mRound->initRound();
}


PlanesWWindow::~PlanesWWindow()
{
    delete mPlanesView;
    delete mRound;
    delete mPlanesModel;
}

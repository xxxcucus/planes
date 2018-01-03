#include "planegameqml.h"

PlaneGameQML::PlaneGameQML()
{
    //builds the model object
    mPlanesModel = new PlanesModel(10, 10, 3);

    //builds the game object - the controller
    mRound = new PlaneRound(mPlanesModel->playerGrid(), mPlanesModel->computerGrid(), mPlanesModel->computerLogic(), false);

    mRound->play();
}

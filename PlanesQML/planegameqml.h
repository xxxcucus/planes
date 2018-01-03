#ifndef PLANEGAMEQML_H
#define PLANEGAMEQML_H

#include "planesmodel.h"
#include "planeround.h"

class PlaneGameQML
{
public:
    PlaneGameQML();

private:
    //The model object
    PlanesModel* mPlanesModel;
    //The controller object
    PlaneRound* mRound;
};

#endif // PLANEGAMEQML_H

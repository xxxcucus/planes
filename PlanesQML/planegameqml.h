#ifndef PLANEGAMEQML_H
#define PLANEGAMEQML_H

#include <QObject>
#include "planesmodel.h"
#include "planeround.h"
#include "guesspoint.h"

class PlaneGameQML : public QObject
{
    Q_OBJECT
public:
    PlaneGameQML();

public:
    Q_INVOKABLE void doneEditing();

    inline PlaneGrid* playerGrid() { return mPlanesModel->playerGrid(); }
    inline PlaneGrid* computerGrid() { return mPlanesModel->computerGrid(); }

signals:
    void guessMade(const GuessPoint& gp);
    void computerMoveGenerated(const GuessPoint& gp);

private:
    //The model object
    PlanesModel* mPlanesModel;
    //The controller object
    PlaneRound* mRound;
};

#endif // PLANEGAMEQML_H

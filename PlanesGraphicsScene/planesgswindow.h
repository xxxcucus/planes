#ifndef PLANESGSWINDOW_H
#define PLANESGSWINDOW_H

#include <QMainWindow>
#include "planesgsview.h"
#include "planeroundjavafx.h"
#include "planesmodel.h"

class PlanesGSWindow : public QMainWindow
{
    Q_OBJECT
public:
    explicit PlanesGSWindow(QWidget *parent = 0);

signals:

public slots:

private:

    //The view object
    PlanesGSView* mPlanesView;
    //The model object
    PlanesModel* mPlanesModel;
    //The controller object
    PlaneRoundJavaFx* mRound;
};

#endif // PLANESGSWINDOW_H

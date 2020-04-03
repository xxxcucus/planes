#ifndef PLANESGSWINDOW_H
#define PLANESGSWINDOW_H

#include <QMainWindow>
#include "planesgsview.h"
#include "planeround.h"

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
    //The controller object
    PlaneRound* mRound;
};

#endif // PLANESGSWINDOW_H

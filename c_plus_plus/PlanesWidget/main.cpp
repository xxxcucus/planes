
#include <QApplication>
#include "planeswwindow.h"



 int main(int argc, char *argv[])
 {

    QApplication app(argc, argv);

    //constructs and shows the program main window
    PlanesWWindow *planesWindow = new PlanesWWindow;
    planesWindow->show();

    int returnCode = app.exec();
    delete planesWindow;
    return returnCode;
 }

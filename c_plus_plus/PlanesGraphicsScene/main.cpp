#include <QApplication>
#include "planesgswindow.h"

 int main(int argc, char *argv[])
 {

    QApplication app(argc, argv);

    //constructs and shows the program main window
    PlanesGSWindow *planesWindow = new PlanesGSWindow;
    planesWindow->show();

    int returnCode = app.exec();
    delete planesWindow;
    return returnCode;
 }

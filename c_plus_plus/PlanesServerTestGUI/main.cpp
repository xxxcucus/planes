
#include <QApplication>
#include "mainwindow.h"

 int main(int argc, char *argv[])
 {

    QApplication app(argc, argv);

    //constructs and shows the program main window
    MainWindow *mainWindow = new MainWindow;
    mainWindow->show();

    int returnCode = app.exec();
    delete mainWindow;
    return returnCode;
 }

#include <QApplication>
#include "planesgswindow.h"

 int main(int argc, char *argv[])
 {

    QApplication app(argc, argv);

    QMessageBox msgBox;
    msgBox.setText("Single player / Multiplayer game?"); 
    msgBox.addButton("Single player", QMessageBox::YesRole);
    msgBox.addButton("Multiplayer", QMessageBox::NoRole);
    msgBox.exec();
    bool isMultiplayer = msgBox.standardButton(msgBox.clickedButton()) == QMessageBox::No;
    
    //constructs and shows the program main window
    PlanesGSWindow *planesWindow = new PlanesGSWindow(isMultiplayer);
    planesWindow->show();

    int returnCode = app.exec();
    delete planesWindow;
    return returnCode;
 }

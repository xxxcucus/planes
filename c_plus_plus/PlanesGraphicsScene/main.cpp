#include <QApplication>
#include "planesgswindow.h"

 int main(int argc, char *argv[])
 {

    QApplication app(argc, argv);

    QMessageBox msgBox;
    msgBox.setText("Single player / Multiplayer game?"); 
    QPushButton* singlePlayerButton = msgBox.addButton("Single player", QMessageBox::YesRole);
    QPushButton* multiplayerButton = msgBox.addButton("Multiplayer", QMessageBox::NoRole);
    msgBox.exec();
    bool isMultiplayer = msgBox.clickedButton() == multiplayerButton;
    
    //constructs and shows the program main window
    PlanesGSWindow *planesWindow = new PlanesGSWindow(isMultiplayer);
    planesWindow->show();

    int returnCode = app.exec();
    delete planesWindow;
    return returnCode;
 }

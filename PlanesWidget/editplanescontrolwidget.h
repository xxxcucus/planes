#ifndef EDITPLANESCONTROLWIDGET_H
#define EDITPLANESCONTROLWIDGET_H

#include "ui_EditControl.h"
#include "gamerenderarea.h"


//this is a widget with 6 buttons and line edit controls
//the widget controls the creation and placement of
//planes on the player's grid
class EditPlanesControlWidget: public QWidget, public Ui::EditControlForm
{
    Q_OBJECT

    //the control is associated with a GameRenderArea button
    GameRenderArea* m_RenderArea;

public:
    EditPlanesControlWidget(GameRenderArea *renderArea, QWidget *parent=0);
    GameRenderArea* getRenderArea() {return m_RenderArea; }

public slots:

    //add plane button is clicked
    void addPlane_clicked();
    //move plane button is clicked
    void movePlane_clicked();
    //delete plane button is clicked
    void deletePlane_clicked();
    //cancel button is clicked
    void cancel_clicked();
    //rotate plane button is clicked
    void rotatePlane_clicked();
    //done button is clicked
    void done_clicked();
    //displayMsg signal has been received
    void displayMsg(QString msg);
    //display status Msg signal has been received
    void displayStatusMsg(const std::string& msg);
    //enoughPlanes signal has been received
    void deactivateAddPlane();
    //norEnoughPlanes signal has been received
    void deactivateDoneButton();
    //activateDone button signal has been received
    void activateDoneButton();
    //in edit mode for player inits the render area and the buttons
    void initButtons();
    //deactivate edit mode for player
    void deactivateButtons();

signals:
    //signals that the done button has been clicked
    void doneClicked();

private:
	bool m_DoneButtonActiveBackup = false;
};

#endif // EDITPLANESCONTROLWIDGET_H


#include "editplanescontrolwidget.h"

EditPlanesControlWidget::EditPlanesControlWidget(GameRenderArea *renderArea, QWidget *parent):
    QWidget(parent)
{
    //creates the form from the designer file
    setupUi(this);

    //initializes
    m_RenderArea = renderArea;

    //connects the clicked signals of the buttons with corresponding slots
    connect(addPlaneButton,SIGNAL(clicked()), this, SLOT(addPlane_clicked()));
    connect(cancelButton, SIGNAL(clicked()),this, SLOT(cancel_clicked()));
    connect(movePlaneButton, SIGNAL(clicked()), this, SLOT(movePlane_clicked()));
    connect(deletePlaneButton, SIGNAL(clicked()), this, SLOT(deletePlane_clicked()));
    connect(rotatePlaneButton, SIGNAL(clicked()), this, SLOT(rotatePlane_clicked()));
    connect(doneButton, SIGNAL(clicked()), this, SLOT(done_clicked()));
}

//TODO: to remove this
void EditPlanesControlWidget::addPlane_clicked()
{
    //update state of buttons
//    if(m_RenderArea->addMorePlanes())
//        addPlaneButton->setEnabled(true);
//    else
//        addPlaneButton->setEnabled(false);
    movePlaneButton->setEnabled(false);
    deletePlaneButton->setEnabled(false);
    rotatePlaneButton->setEnabled(false);

    //set operation mode for render Area
    m_RenderArea->setOperation(GameRenderArea::Add_plane);
    displayMsg(tr("Right click to decide the position of the plane."));
}

void EditPlanesControlWidget::movePlane_clicked()
{
    //set operation mode for render Area
    if(m_RenderArea->setOperation(GameRenderArea::Move_plane))
    {
        //update state of buttons
//        if(m_RenderArea->addMorePlanes())
//            addPlaneButton->setEnabled(true);
//        else
//            addPlaneButton->setEnabled(false);
		m_DoneButtonActiveBackup = doneButton->isEnabled();
        movePlaneButton->setEnabled(true);
        deletePlaneButton->setEnabled(false);
        rotatePlaneButton->setEnabled(false);
		doneButton->setEnabled(false);
        displayMsg(tr("Click on plane's head to select it."));
    }
}

//TODO: to remove
void EditPlanesControlWidget::deletePlane_clicked()
{
    //set operation mode for render Area
    if(m_RenderArea->setOperation(GameRenderArea::Delete_plane))
    {
        //update state of buttons
//        if(m_RenderArea->addMorePlanes())
//            addPlaneButton->setEnabled(true);
//        else
//            addPlaneButton->setEnabled(false);
        movePlaneButton->setEnabled(false);
        deletePlaneButton->setEnabled(false);
        rotatePlaneButton->setEnabled(false);
        displayMsg(tr("Click on plane's head to delete it"));
    }
}

void EditPlanesControlWidget::rotatePlane_clicked()
{
    //set operation mode for render Area
    if(m_RenderArea->setOperation(GameRenderArea::Rotate_plane))
    {
        //update state of buttons
//        if(m_RenderArea->addMorePlanes())
//            addPlaneButton->setEnabled(true);
//        else
//            addPlaneButton->setEnabled(false);
		m_DoneButtonActiveBackup = true;
        movePlaneButton->setEnabled(false);
        deletePlaneButton->setEnabled(false);
        rotatePlaneButton->setEnabled(true);
		doneButton->setEnabled(false);
        displayMsg(tr("Click on plane's head to select."));
    }
}

void EditPlanesControlWidget::cancel_clicked()
{
    //update state of buttons
//    if(m_RenderArea->addMorePlanes())
//        addPlaneButton->setEnabled(true);
//    else
//        addPlaneButton->setEnabled(false);
    movePlaneButton->setEnabled(true);
    deletePlaneButton->setEnabled(false);
    rotatePlaneButton->setEnabled(true);
	if (m_RenderArea->isOperation())
		doneButton->setEnabled(m_DoneButtonActiveBackup);

    //set operation mode for render Area
    m_RenderArea->restoreBackupPlane();
    m_RenderArea->setOperation(GameRenderArea::No_operation);


    displayMsg(tr("Choose an operation from the list below"));
}

void EditPlanesControlWidget::done_clicked()
{
    //break all the current operations especially move plane and rotate plane
    deactivateButtons();
    emit doneClicked();
}

//utility functions
void EditPlanesControlWidget::displayMsg(QString msg)
{
    infoLineEdit->setText(msg);
}

void EditPlanesControlWidget::displayStatusMsg(const std::string& msg)
{
    statusLineEdit->setText(msg.c_str());
}

void EditPlanesControlWidget::deactivateAddPlane()
{
    addPlaneButton->setEnabled(false);
}

void EditPlanesControlWidget::deactivateDoneButton()
{
    doneButton->setEnabled(false);
}

void EditPlanesControlWidget::activateDoneButton()
{
    doneButton->setEnabled(true);
}

void EditPlanesControlWidget::initButtons()
{
    addPlaneButton->setEnabled(false);
    cancelButton->setEnabled(true);
    movePlaneButton->setEnabled(true);
    deletePlaneButton->setEnabled(false);
    rotatePlaneButton->setEnabled(true);
    doneButton->setEnabled(true);
}

void EditPlanesControlWidget::deactivateButtons()
{
    addPlaneButton->setEnabled(false);
    cancelButton->setEnabled(false);
    movePlaneButton->setEnabled(false);
    deletePlaneButton->setEnabled(false);
    rotatePlaneButton->setEnabled(false);
    doneButton->setEnabled(false);
    infoLineEdit->setText("");
    statusLineEdit->setText("");
}

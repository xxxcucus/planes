#ifndef CHOICEDEBUGWIDGET_H
#define CHOICEDEBUGWIDGET_H

#include "debugrenderarea.h"
#include "buttons.h"
#include <QGridLayout>
#include <QLabel>


//This is a widget that displays how the computer is thinking
class ChoiceDebugWidget: public QWidget
{

    Q_OBJECT

    //the model of computer playing
    ComputerLogic *m_logic; 
    //the model of computer playing used to revert the computer strategy
    RevertComputerLogic *m_revertLogic;
    //the layout of the widget
    QGridLayout *m_layout;
    //the widget display the possibilities of
    //placing a plane in 4 different orientations
    //North South, South North, West East, East West


    //The 4 graphic objects that display
    //the possible movements
    DebugRenderArea* m_nsarea;
    DebugRenderArea* m_snarea;
    DebugRenderArea* m_wearea;
    DebugRenderArea* m_ewarea;

    //the two buttons on the top side of the widget
    NextMoveButton* m_nextButton;
    PreviousMoveButton *m_prevButton;


public slots:
    //when the user selects to display this widget
    void setLogic();
    //show the previous move of the computer
    void prevMove();
    //show the next move of the computer
    void nextMove();

signals:
    //tells the render areas to update
    void updateRenderAreas();

public:
    //constructor
    ChoiceDebugWidget(ComputerLogic* logic, QWidget *parent=0);
    ~ChoiceDebugWidget();

private:
    //updates the enabled or disabled states of the buttons
    void updateButtonStates();

};

#endif // ChoiceDebugWidget_H

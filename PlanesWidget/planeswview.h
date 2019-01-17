#ifndef PLANESVIEW_H
#define PLANESVIEW_H

#include <QWidget>
#include "editorwindow.h"
#include "editplanescontrolwidget.h"
#include "gamestatswidget.h"
#include "choicedebugwidget.h"
#include "planeround.h"

//creates the main view object of the program
//TODO: member variables are named m_
class PlanesWView : public QWidget
{
    Q_OBJECT

    //PlaneGrid objects manage the logic of a set of planes on a grid
    //as well as various operations: save, remove, search, etc.
    PlaneGrid* m_playerGrid;
    PlaneGrid* m_computerGrid;

    //GameRenderArea objects are graphical objects that
    //display guesses, computer choices , planes
    //in grids
    GameRenderArea* m_playerArea;
    GameRenderArea* m_computerArea;

    //EditPlanesControlWidget is a widget that controls the
    //process of planes positioning and adding in the
    //player grid
    EditPlanesControlWidget* m_editPlanesWidget;

    //GameStatsWidget displays the statistics about the current
    //game and about the score
    GameStatsWidget* m_gameStatsWidget;

    //ChoiceDebug window is a widget that shows how the
    //computer plays
    ChoiceDebugWidget* m_choiceDebugWidget;

    //ComputerLogic is the object that keeps the
    //computer's strategy
    ComputerLogic* m_computerLogic;

    //PlaneRound is the object that coordinates the game
    PlaneRound* m_round;

public:
    explicit PlanesWView(PlaneRound*, QWidget* parent = 0);
    
signals:
    //signals when the widget showing the computer strategy becomes active
    void debugWidgetSelected();
public slots:
    //a widget is selected
    void widgetSelected(int); 

	//TODO: implement in the controller
	void doneClicked();
	void receivedPlayerGuess(const GuessPoint& gp);
	void startNewRound();
};

#endif // PLANESVIEW_H

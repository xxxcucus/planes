#include "choicedebugwidget.h"

#include <QWidget>
#include <QGridLayout>
#include <QLabel>


//builds the widget's layout
ChoiceDebugWidget::ChoiceDebugWidget(ComputerLogic *logic, QWidget *parent):
    QWidget(parent)

{
    //initializes the computer logic
    m_logic = logic;
    //builds the object that reverts the computer strategy
    m_revertLogic = new RevertComputerLogic(m_logic->getRowNo(),m_logic->getColNo(),m_logic->getPlaneNo());

    m_layout = new QGridLayout();

    //builds the widgets that display the computer choices
    m_nsarea = new DebugRenderArea(m_revertLogic, Plane::NorthSouth);
    m_snarea = new DebugRenderArea(m_revertLogic, Plane::SouthNorth);
    m_wearea = new DebugRenderArea(m_revertLogic, Plane::WestEast);
    m_ewarea = new DebugRenderArea(m_revertLogic, Plane::EastWest);

    //creates the buttons
    m_nextButton = new NextMoveButton();
    m_prevButton = new PreviousMoveButton();
    m_nextButton->setEnabled(false);
    m_prevButton->setEnabled(false);

    //sets tool tips for widgets and buttons
    m_nsarea->setToolTip("Computer options and guesses for planes oriented in the North-South direction.\n -1 means impossible position; 0 means no data ; \n a positive number gives the number of hits that correspond to this position being a plane head.\n A circle means a miss guess, a triangle a hit guess, an X a dead guess.");
    m_snarea->setToolTip("Computer options and guesses for planes oriented in the South-North direction.\n -1 means impossible position; 0 means no data ;\n a positive number gives the number of hits that correspond to this position being a plane head.\n A circle means a miss guess, a triangle a hit guess, an X a dead guess.");
    m_wearea->setToolTip("Computer options and guesses for planes oriented in the West-East direction.\n -1 means impossible position; 0 means no data ;\n a positive number gives the number of hits that correspond to this position being a plane head.\n A circle means a miss guess, a triangle a hit guess, an X a dead guess.");
    m_ewarea->setToolTip("Computer options and guesses for planes oriented in the East-West direction.\n -1 means impossible position; 0 means no data ;\n a positive number gives the number of hits that correspond to this position being a plane head.\n A circle means a miss guess, a triangle a hit guess, an X a dead guess.");

    m_nextButton->setToolTip("See next move");
    m_prevButton->setToolTip("See previous move");

    //creates the layout for the widget
    m_layout->addWidget(m_prevButton,1,2);
    m_layout->addWidget(m_nextButton,1,3);

    m_layout->addWidget(m_nsarea,2,0);
    m_layout->addWidget(m_wearea,2,1);

    m_layout->addWidget(m_snarea,3,0);
    m_layout->addWidget(m_ewarea,3,1);

    setLayout(m_layout);

    //connects various signals in this widget
    connect(m_nextButton,SIGNAL(clicked()),this,SLOT(nextMove()));
    connect(m_prevButton,SIGNAL(clicked()),this,SLOT(prevMove()));
    connect(this,SIGNAL(updateRenderAreas()),m_nsarea, SLOT(update()));
    connect(this,SIGNAL(updateRenderAreas()),m_snarea, SLOT(update()));
    connect(this,SIGNAL(updateRenderAreas()),m_wearea, SLOT(update()));
    connect(this,SIGNAL(updateRenderAreas()),m_ewarea, SLOT(update()));
}

//this occurs when the user selects the choice window in the list widget
//all the debugrenderarea use the computerlogic to diplay the data
//resets the revertLogic object
void ChoiceDebugWidget::setLogic()
{
    *m_revertLogic = *m_logic;
    updateButtonStates();
    emit updateRenderAreas();
}


//shows the previous move of the computer strategy
void ChoiceDebugWidget::prevMove()
{
    m_revertLogic->revert(1);
    updateButtonStates();
    emit updateRenderAreas();
}

//shows the next move of the computer strategy
void ChoiceDebugWidget::nextMove()
{
    m_revertLogic->next();
    updateButtonStates();
    emit updateRenderAreas();
}

//update the next move and prev move buttons
void ChoiceDebugWidget::updateButtonStates()
{
    if(m_revertLogic->hasNext())
        m_nextButton->setEnabled(true);
    else
        m_nextButton->setEnabled(false);


    if(m_revertLogic->hasPrev())
        m_prevButton->setEnabled(true);
    else
        m_prevButton->setEnabled(false);

}

//destructor
ChoiceDebugWidget::~ChoiceDebugWidget()
{
    delete m_revertLogic;
}

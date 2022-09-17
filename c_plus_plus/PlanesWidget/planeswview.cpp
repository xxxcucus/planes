#include "planeswview.h"
#include <QSplitter>
#include <QListWidget>
#include <QStackedLayout>
#include "planeround.h"

PlanesWView::PlanesWView(PlaneRound *rd,QWidget *parent) :
    QWidget(parent), m_round(rd)
{
	m_playerGrid = m_round->playerGrid();
	m_computerGrid = m_round->computerGrid();
	m_computerLogic = m_round->computerLogic();

    //builds the gamerenderarea objects
    m_playerArea = new GameRenderArea(m_playerGrid);
    m_computerArea = new GameRenderArea(m_computerGrid);

    //builds the editing control window and the game stats widget
    m_editPlanesWidget = new EditPlanesControlWidget(m_playerArea);
    m_gameStatsWidget = new GameStatsWidget();

    //builds the widget that displays the computer strategy
    m_choiceDebugWidget = new ChoiceDebugWidget(m_computerLogic);

    //builds the layout for this view
    QSplitter* hsplitter = new QSplitter(Qt::Horizontal);
    hsplitter->addWidget(m_playerArea);
    hsplitter->addWidget(m_computerArea);

    QSplitter* vsplitter = new QSplitter(Qt::Vertical);
    vsplitter->addWidget(m_gameStatsWidget);
    vsplitter->addWidget(hsplitter);
    vsplitter->addWidget(m_editPlanesWidget);

    QListWidget *listWidget = new QListWidget;
    listWidget->addItem(tr("Game"));
    listWidget->addItem(tr("Computer choices"));

    QFontMetrics fm = listWidget->fontMetrics();
    int maxWidth = fm.boundingRect("Computer choices").width();
    listWidget->setFixedWidth(maxWidth * 2);

    QStackedLayout* stackedLayout = new QStackedLayout;
    stackedLayout->addWidget(vsplitter);
    stackedLayout->addWidget(m_choiceDebugWidget);

    listWidget->setCurrentRow(0);

    QHBoxLayout* mainLayout = new QHBoxLayout();
    mainLayout->addWidget(listWidget);
    mainLayout->addLayout(stackedLayout);

    setLayout(mainLayout);

    //builds the connections in the graphical user interface
    //connect(round, SIGNAL(initPlayerGrid()),
     //                editPlanesWidget, SLOT(initButtons()));

    connect(m_playerArea, SIGNAL(operationEndet()),
                     m_editPlanesWidget, SLOT(cancel_clicked()));
    connect(m_playerArea,SIGNAL(displayMsg(QString)),
                     m_editPlanesWidget, SLOT(displayMsg(QString)));
    connect(m_playerArea, SIGNAL(enoughPlanes()),
                     m_editPlanesWidget, SLOT(deactivateAddPlane()));
    connect(m_playerArea, SIGNAL(displayStatusMsg(const std::string&)),
                     m_editPlanesWidget, SLOT(displayStatusMsg(const std::string&)));
    connect(m_playerArea, SIGNAL(notEnoughPlanes()),
                     m_editPlanesWidget, SLOT(deactivateDoneButton()));
    connect(m_playerArea, SIGNAL(activateDone()),
                     m_editPlanesWidget, SLOT(activateDoneButton()));
    connect(m_playerArea, SIGNAL(deactivateDone()),
                     m_editPlanesWidget, SLOT(deactivateDoneButton()));
    connect(m_editPlanesWidget, SIGNAL(doneClicked()),
                     m_playerArea, SLOT(changeMode()));
    connect(m_editPlanesWidget, SIGNAL(doneClicked()),
                     this, SLOT(doneClicked()));
	/*
    connect(round, SIGNAL(computerMoveGenerated(GuessPoint)),
                     playerArea, SLOT(showMove(GuessPoint)));
    connect(round, SIGNAL(needPlayerGuess()),
                     computerArea, SLOT(activateGameMode())); */
    connect(m_computerArea, SIGNAL(guessMade(const GuessPoint&)),
                     this, SLOT(receivedPlayerGuess(const GuessPoint&)));
    /*connect(round, SIGNAL(displayStatusMessage(const std::string&)),
                     editPlanesWidget, SLOT(displayStatusMsg(const std::string&)));
    connect(round, SIGNAL(roundEnds(bool)),
                     computerArea, SLOT(roundEndet()));
    connect(round, SIGNAL(roundEnds(bool)),
                     gameStatsWidget, SLOT(roundEndet())); */
    connect(m_gameStatsWidget, SIGNAL(startGame()),
                     this, SLOT(startNewRound()));
    /*connect(round, SIGNAL(initGraphics()),
                     playerArea, SLOT(reset()));
    connect(round, SIGNAL(initGraphics()),
                     computerArea, SLOT(reset()));
    connect(round, SIGNAL(statsUpdated(GameStatistics)),
                     gameStatsWidget, SLOT(updateStats(GameStatistics)));*/

    connect(listWidget, SIGNAL(currentRowChanged(int)),
                stackedLayout, SLOT(setCurrentIndex(int)));
    connect(listWidget, SIGNAL(currentRowChanged(int)),
                this, SLOT(widgetSelected(int)));
    connect(this, SIGNAL(debugWidgetSelected()),
            m_choiceDebugWidget, SLOT(setLogic()));

	m_editPlanesWidget->initButtons();
}

//when a widget is selected
//check to see if the widget that displays the computer strategy
//was selected and emit a signal

void PlanesWView::widgetSelected(int sel)
{
    if(sel == 1)
        emit debugWidgetSelected();
}

//TODO: this should be called in controller
void PlanesWView::doneClicked()
{
	m_round->doneEditing();
	m_playerArea->activateGameMode();
	m_computerArea->activateGameMode();
}

//TODO: implement in controller
void PlanesWView::receivedPlayerGuess(const GuessPoint& gp)
{
	PlayerGuessReaction pgr;
	m_round->playerGuess(gp, pgr);

	if (pgr.m_ComputerMoveGenerated) {
		m_playerArea->showMove(pgr.m_ComputerGuess);
	}
	if (pgr.m_RoundEnds) {
		printf("Round ends\n");
		m_computerArea->roundEndet();
		m_gameStatsWidget->roundEndet();
		m_editPlanesWidget->displayStatusMsg(pgr.m_isPlayerWinner ? "Computer wins!" : "Player wins!");
		m_round->roundEnds();
	}

	m_gameStatsWidget->updateStats(pgr.m_GameStats);
}

void PlanesWView::startNewRound() {
	printf("Start new round\n");
	if (m_round->didRoundEnd()) {
		m_gameStatsWidget->reset();
		m_editPlanesWidget->initButtons();
		m_playerArea->reset();
		m_computerArea->reset();
		m_playerArea->activateEditorMode();
		m_computerArea->activateEditorMode();
		m_round->initRound();
	}
}
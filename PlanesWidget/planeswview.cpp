#include "planeswview.h"
#include <QSplitter>
#include <QListWidget>
#include <QStackedLayout>
#include "planeroundjavafx.h"

PlanesWView::PlanesWView(PlaneGrid *pGrid, PlaneGrid* cGrid, ComputerLogic* cLogic, PlaneRoundJavaFx *rd,QWidget *parent) :
    QWidget(parent), playerGrid(pGrid), computerGrid(cGrid), computerLogic(cLogic), round(rd)
{
    //builds the gamerenderarea objects
    playerArea = new GameRenderArea(playerGrid);
    computerArea = new GameRenderArea(computerGrid);

    //builds the editing control window and the game stats widget
    editPlanesWidget = new EditPlanesControlWidget(playerArea);
    gameStatsWidget = new GameStatsWidget();

    //builds the widget that displays the computer strategy
    choiceDebugWidget = new ChoiceDebugWidget(computerLogic);

    //builds the layout for this view
    QSplitter* hsplitter = new QSplitter(Qt::Horizontal);
    hsplitter->addWidget(playerArea);
    hsplitter->addWidget(computerArea);

    QSplitter* vsplitter = new QSplitter(Qt::Vertical);
    vsplitter->addWidget(gameStatsWidget);
    vsplitter->addWidget(hsplitter);
    vsplitter->addWidget(editPlanesWidget);

    QListWidget *listWidget = new QListWidget;
    listWidget->addItem(tr("Game"));
    listWidget->addItem(tr("Computer choices"));

    QFontMetrics fm = listWidget->fontMetrics();
    int maxWidth = fm.width("Computer choices");
    listWidget->setFixedWidth(maxWidth * 2);

    QStackedLayout* stackedLayout = new QStackedLayout;
    stackedLayout->addWidget(vsplitter);
    stackedLayout->addWidget(choiceDebugWidget);

    listWidget->setCurrentRow(0);

    QHBoxLayout* mainLayout = new QHBoxLayout();
    mainLayout->addWidget(listWidget);
    mainLayout->addLayout(stackedLayout);

    setLayout(mainLayout);

    //builds the connections in the graphical user interface
    //connect(round, SIGNAL(initPlayerGrid()),
     //                editPlanesWidget, SLOT(initButtons()));

    connect(playerArea, SIGNAL(operationEndet()),
                     editPlanesWidget, SLOT(cancel_clicked()));
    connect(playerArea,SIGNAL(displayMsg(QString)),
                     editPlanesWidget, SLOT(displayMsg(QString)));
    connect(playerArea, SIGNAL(enoughPlanes()),
                     editPlanesWidget, SLOT(deactivateAddPlane()));
    connect(playerArea, SIGNAL(displayStatusMsg(const std::string&)),
                     editPlanesWidget, SLOT(displayStatusMsg(const std::string&)));
    connect(playerArea, SIGNAL(notEnoughPlanes()),
                     editPlanesWidget, SLOT(deactivateDoneButton()));
    connect(playerArea, SIGNAL(activateDone()),
                     editPlanesWidget, SLOT(activateDoneButton()));
    connect(playerArea, SIGNAL(deactivateDone()),
                     editPlanesWidget, SLOT(deactivateDoneButton()));
    connect(editPlanesWidget, SIGNAL(doneClicked()),
                     playerArea, SLOT(changeMode()));
    connect(editPlanesWidget, SIGNAL(doneClicked()),
                     this, SLOT(doneClicked()));
	/*
    connect(round, SIGNAL(computerMoveGenerated(GuessPoint)),
                     playerArea, SLOT(showMove(GuessPoint)));
    connect(round, SIGNAL(needPlayerGuess()),
                     computerArea, SLOT(activateGameMode())); */
    connect(computerArea, SIGNAL(guessMade(const GuessPoint&)),
                     this, SLOT(receivedPlayerGuess(const GuessPoint&)));
    /*connect(round, SIGNAL(displayStatusMessage(const std::string&)),
                     editPlanesWidget, SLOT(displayStatusMsg(const std::string&)));
    connect(round, SIGNAL(roundEnds(bool)),
                     computerArea, SLOT(roundEndet()));
    connect(round, SIGNAL(roundEnds(bool)),
                     gameStatsWidget, SLOT(roundEndet())); */
    connect(gameStatsWidget, SIGNAL(startGame()),
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
            choiceDebugWidget, SLOT(setLogic()));
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
	round->doneEditing();
	playerArea->activateGameMode();
	computerArea->activateGameMode();
}

//TODO: implement in controller
void PlanesWView::receivedPlayerGuess(const GuessPoint& gp)
{
	PlayerGuessReaction pgr;
	round->playerGuess(gp, pgr);

	if (pgr.m_ComputerMoveGenerated) {
		playerArea->showMove(pgr.m_ComputerGuess);
	}
	if (pgr.m_RoundEnds) {
		printf("Round ends\n");
		computerArea->roundEndet();
		gameStatsWidget->roundEndet();
		editPlanesWidget->displayStatusMsg(pgr.m_isPlayerWinner ? "Computer wins!" : "Player wins!");
		round->roundEnds();
	}

	gameStatsWidget->updateStats(pgr.m_GameStats);
}

void PlanesWView::startNewRound() {
	printf("Start new round\n");
	if (round->didGameEnd()) {
		gameStatsWidget->reset();
		editPlanesWidget->initButtons();
		playerArea->reset();
		computerArea->reset();
		playerArea->activateEditorMode();
		computerArea->activateEditorMode();
		round->initRound();
	}
}
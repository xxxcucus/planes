#include "leftpane.h"

#include <QLabel>
#include <QComboBox>
#include <QGridLayout>
#include <QPushButton>
#include <QSpacerItem>

LeftPane::LeftPane(GameInfo* gameInfo, QWidget *parent) : QTabWidget(parent), m_GameInfo(gameInfo)
{
    m_PlayerStatsFrame = new GameStatsFrame("Player");
    m_ComputerStatsFrame = new GameStatsFrame("Computer");
    QVBoxLayout* vLayout = new QVBoxLayout();
    vLayout->addWidget(m_PlayerStatsFrame);
    vLayout->addWidget(m_ComputerStatsFrame);
    vLayout->addStretch(5);
    m_GameWidget = new QWidget();
    m_GameWidget->setLayout(vLayout);

    m_BoardEditingWidget = new QWidget();
    m_selectPlaneButton = new QPushButton("Select plane");
    m_rotatePlaneButton = new QPushButton("Rotate plane");
    m_leftPlaneButton = new QPushButton("Plane to left");
    m_rightPlaneButton = new QPushButton("Plane to right");
    m_upPlaneButton = new QPushButton("Plane upwards");
    m_downPlaneButton = new QPushButton("Plane downwards");
    m_doneButton = new QPushButton("Done editing");
    QSpacerItem* spacer = new QSpacerItem(50, 50, QSizePolicy::Expanding, QSizePolicy::Expanding);
    QGridLayout* gridLayout = new QGridLayout();
    gridLayout->addWidget(m_selectPlaneButton, 0, 0, 1, 3);
    gridLayout->addWidget(m_rotatePlaneButton, 1, 0, 1, 3);
    gridLayout->addWidget(m_upPlaneButton, 2, 1);
    gridLayout->addWidget(m_leftPlaneButton, 3, 0);
    gridLayout->addWidget(m_rightPlaneButton, 3, 2);
    gridLayout->addWidget(m_downPlaneButton, 4, 1);
    gridLayout->addWidget(m_doneButton, 5, 0, 1, 3);
    gridLayout->addItem(spacer, 6, 0, 1, 3);
    gridLayout->setRowStretch(6, 5);
    m_BoardEditingWidget->setLayout(gridLayout);

    connect(m_selectPlaneButton, SIGNAL(clicked(bool)), this, SIGNAL(selectPlaneClicked(bool)));
    connect(m_rotatePlaneButton, SIGNAL(clicked(bool)), this, SIGNAL(rotatePlaneClicked(bool)));
    connect(m_doneButton, SIGNAL(clicked(bool)), this, SIGNAL(doneClicked()));
    connect(m_doneButton, SIGNAL(clicked(bool)), this, SLOT(doneClickedSlot()));
    connect(m_upPlaneButton, SIGNAL(clicked(bool)), this, SIGNAL(upPlaneClicked(bool)));
    connect(m_downPlaneButton, SIGNAL(clicked(bool)), this, SIGNAL(downPlaneClicked(bool)));
    connect(m_leftPlaneButton, SIGNAL(clicked(bool)), this, SIGNAL(leftPlaneClicked(bool)));
    connect(m_rightPlaneButton, SIGNAL(clicked(bool)), this, SIGNAL(rightPlaneClicked(bool)));

    m_GameTabIndex = addTab(m_GameWidget, "Round");
    m_EditorTabIndex = addTab(m_BoardEditingWidget, "BoardEditing");

    m_ScoreFrame = new ScoreFrame();
    QVBoxLayout* vLayout1 = new QVBoxLayout();
    vLayout1->addWidget(m_ScoreFrame);
    vLayout1->addStretch(5);
    m_StartGameWidget = new QWidget();
    m_StartGameWidget->setLayout(vLayout1);
    m_GameStartIndex = addTab(m_StartGameWidget, "Start Round");
    connect(m_ScoreFrame, SIGNAL(startNewGame()), this, SIGNAL(startNewGame()));

    activateEditorTab();
}

void LeftPane::activateDoneButton(bool planesOverlap)
{
    m_doneButton->setEnabled(!planesOverlap);
}

void LeftPane::doneClickedSlot()
{
    activateGameTab();
    m_selectPlaneButton->setEnabled(false);
    m_rotatePlaneButton->setEnabled(false);
    m_leftPlaneButton->setEnabled(false);
    m_rightPlaneButton->setEnabled(false);
    m_upPlaneButton->setEnabled(false);
    m_downPlaneButton->setEnabled(false);
    m_doneButton->setEnabled(false);
}

void LeftPane::activateEditingBoard()
{
    activateEditorTab();
    ///activate the buttons in the editor tab
    m_selectPlaneButton->setEnabled(true);
    m_rotatePlaneButton->setEnabled(true);
    m_leftPlaneButton->setEnabled(true);
    m_rightPlaneButton->setEnabled(true);
    m_upPlaneButton->setEnabled(true);
    m_downPlaneButton->setEnabled(true);
    m_doneButton->setEnabled(true);
}

void LeftPane::updateGameStatistics(const GameStatistics &gs)
{
    m_PlayerStatsFrame->updateDisplayedValues(gs.m_playerMoves, gs.m_playerMisses, gs.m_playerHits, gs.m_playerDead);
    m_ComputerStatsFrame->updateDisplayedValues(gs.m_computerMoves, gs.m_computerMisses, gs.m_computerHits, gs.m_computerDead);
    m_ScoreFrame->updateDisplayedValues(gs.m_computerWins, gs.m_playerWins, gs.m_draws);
}

void LeftPane::endRound(bool) {
    activateStartGameTab();
}

void LeftPane::activateGameTab() {
    setCurrentIndex(m_GameTabIndex);
    setTabEnabled(m_EditorTabIndex, false);
    setTabEnabled(m_GameTabIndex, true);
    setTabEnabled(m_GameStartIndex, true);
	m_ScoreFrame->deactivateStartRoundButton();
}

void LeftPane::activateEditorTab() {
    setCurrentIndex(m_EditorTabIndex);
    setTabEnabled(m_EditorTabIndex, true);
    setTabEnabled(m_GameTabIndex, false);
    setTabEnabled(m_GameStartIndex, true);
	m_ScoreFrame->deactivateStartRoundButton();
}

void LeftPane::activateStartGameTab() {
    setCurrentIndex(m_GameStartIndex);
    setTabEnabled(m_EditorTabIndex, false);
    setTabEnabled(m_GameTabIndex, false);
    setTabEnabled(m_GameStartIndex, true);
	m_ScoreFrame->activateStartRoundButton();
}

void LeftPane::setMinWidth()
{
    ///decide the minimum size based on the texts in the labels
    QString textA("Plane upwards");
    QString textB("Plane downwards");
    QString textC("Plane left");
    QString textD("Plane right");

    QFontMetrics fm = fontMetrics();
    setMinimumWidth(((fm.width(textB) + fm.width(textC) + fm.width(textD)) * 170) / 100);
}

void LeftPane::setMinHeight()
{
    QFontMetrics fm = fontMetrics();
    setMinimumHeight(fm.height() * 12);
}

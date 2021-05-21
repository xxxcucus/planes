#include "startnewroundwidget.h"

#include <QVBoxLayout>

StartNewRoundWidget::StartNewRoundWidget(GameInfo *gameInfo, QWidget* parent): QWidget(parent), m_GameInfo(gameInfo) { 
    
    QSpacerItem* spacer = new QSpacerItem(50, 50, QSizePolicy::Expanding, QSizePolicy::Expanding);
    QSpacerItem* spacer2 = new QSpacerItem(50, 50, QSizePolicy::Expanding, QSizePolicy::Expanding);
    
    m_ScoreFrame = new ScoreFrame(m_GameInfo);
    QVBoxLayout* vLayout1 = new QVBoxLayout();
    vLayout1->addItem(spacer);
    vLayout1->addWidget(m_ScoreFrame);
    vLayout1->addItem(spacer2);
    setLayout(vLayout1);
    
    connect(m_ScoreFrame, &ScoreFrame::startNewGame, this, &StartNewRoundWidget::startNewGame);
}

void StartNewRoundWidget::updateDisplayedValues(const GameStatistics& gs) {
    m_ScoreFrame->updateDisplayedValues(gs.m_computerWins, gs.m_playerWins, gs.m_draws);
}

void StartNewRoundWidget::deactivateStartRoundButton() {
    m_ScoreFrame->deactivateStartRoundButton();
}

void StartNewRoundWidget::activateStartRoundButton() {
    m_ScoreFrame->activateStartRoundButton();
}

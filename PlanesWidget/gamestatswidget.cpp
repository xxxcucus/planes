#include "gamestatswidget.h"

GameStatsWidget::GameStatsWidget( QWidget *parent):
    QWidget(parent)
{
    setupUi(this);

    connect(newRoundButton, SIGNAL(clicked()), this, SLOT(startNewRound()));

    reset();

}



void GameStatsWidget::reset()
{
    newRoundButton->setEnabled(true);

    playerMovesSpinBox->setValue(0);
    playerDeadSpinBox  ->setValue(0);
    playerHitsSpinBox->setValue(0);
    playerMissesSpinBox->setValue(0);
    computerMovesSpinBox->setValue(0);
    computerDeadSpinBox->setValue(0);
    computerHitsSpinBox->setValue(0);
    computerMissesSpinBox->setValue(0);

}


void GameStatsWidget::roundEndet()
{
    newRoundButton->setEnabled(true);
}

void GameStatsWidget::updateStats(GameStatistics gs)
{
    playerMovesSpinBox->setValue(gs.m_playerMoves);
    playerDeadSpinBox->setValue(gs.m_playerDead);
    playerHitsSpinBox->setValue(gs.m_playerHits);
    playerMissesSpinBox->setValue(gs.m_playerMisses);
    computerMovesSpinBox->setValue(gs.m_computerMoves);
    computerDeadSpinBox->setValue(gs.m_computerDead);
    computerHitsSpinBox->setValue(gs.m_computerHits);
    computerMissesSpinBox->setValue(gs.m_computerMisses);

    playerScoreSpinBox->setValue(gs.m_playerWins);
    computerScoreSpinBox->setValue(gs.m_computerWins);
}


void GameStatsWidget::startNewRound()
{
    reset();
    emit startGame();
}

#include "playroundwidget.h"

#include <QVBoxLayout>

PlayRoundWidget::PlayRoundWidget(GameInfo* gameInfo, QWidget *parent) 
    : QWidget(parent), m_GameInfo(gameInfo) {

    m_PlayerStatsFrame = new GameStatsFrame("Player");
    m_ComputerStatsFrame = new GameStatsFrame("Computer");
    m_acquireOpponentMovesButton = new QPushButton("Acquire opponent moves");
    m_CancelRoundButton = new QPushButton("Cancel Round");
    QVBoxLayout* vLayout = new QVBoxLayout();
    vLayout->addWidget(m_PlayerStatsFrame);
    vLayout->addWidget(m_ComputerStatsFrame);
    if (!m_GameInfo->getSinglePlayer())
        vLayout->addWidget(m_acquireOpponentMovesButton);
    vLayout->addWidget(m_CancelRoundButton);
    vLayout->addStretch(5);
    setLayout(vLayout);
    
    connect(m_acquireOpponentMovesButton, &QPushButton::clicked, this, &PlayRoundWidget::acquireOpponentMovesClicked);
    connect(m_CancelRoundButton, &QPushButton::clicked, this, &PlayRoundWidget::cancelRoundClicked);
}

void PlayRoundWidget::updateGameStatistics(const GameStatistics& gs) {
    m_PlayerStatsFrame->updateDisplayedValues(gs.m_playerMoves, gs.m_playerMisses, gs.m_playerHits, gs.m_playerDead);
    m_ComputerStatsFrame->updateDisplayedValues(gs.m_computerMoves, gs.m_computerMisses, gs.m_computerHits, gs.m_computerDead);
}

#include "scoreframe.h"

#include <QGridLayout>

ScoreFrame::ScoreFrame(GameInfo* gameInfo, QWidget* parent): QFrame(parent), m_GameInfo(gameInfo)
{
    QString titleText = QString("<b> General Score</b>");
    QLabel* titleLabel = new QLabel();
    titleLabel->setText(titleText);
    titleLabel->setFont(QFont("Timer", 20, QFont::Bold));
    titleLabel->setAlignment(Qt::AlignCenter);
    
    QString computerScoreText = "Computer";
    if (!m_GameInfo->getSinglePlayer())
        computerScoreText = "Opponent";
    
    QLabel* computerScoreTextLabel = new QLabel(computerScoreText);
    QLabel* playerScoreTextLabel = new QLabel("Player: ");
	QLabel* drawsTextLabel = new QLabel("Draws: ");
    m_ComputerScoreLabel = new QLabel("0");
    m_PlayerScoreLabel = new QLabel("0");
	m_DrawsLabel = new QLabel("0");
    m_StartGameButton = new QPushButton("Start New Round");
    QGridLayout* gridLayout1 = new QGridLayout();
    gridLayout1->addWidget(titleLabel, 0, 0, 1, 2, Qt::AlignCenter);
    gridLayout1->addWidget(computerScoreTextLabel, 1, 0);
    gridLayout1->addWidget(playerScoreTextLabel, 2, 0);
	gridLayout1->addWidget(drawsTextLabel, 3, 0);
    gridLayout1->addWidget(m_ComputerScoreLabel, 1, 1, 1, 1, Qt::AlignCenter);
    gridLayout1->addWidget(m_PlayerScoreLabel, 2, 1, 1, 1, Qt::AlignCenter);
	gridLayout1->addWidget(m_DrawsLabel, 3, 1, 1, 1, Qt::AlignCenter);
    gridLayout1->addWidget(m_StartGameButton, 4, 1);
    
    QHBoxLayout* hLayout1 = new QHBoxLayout();
    QSpacerItem* spacer3 = new QSpacerItem(20, 20, QSizePolicy::Expanding, QSizePolicy::Fixed);
    QSpacerItem* spacer4 = new QSpacerItem(20, 20, QSizePolicy::Expanding, QSizePolicy::Fixed);

    hLayout1->addItem(spacer3);
    hLayout1->addLayout(gridLayout1);
    hLayout1->addItem(spacer4);
    
    setLayout(hLayout1);
    setFrameStyle(QFrame::NoFrame);
    connect(m_StartGameButton, SIGNAL(clicked(bool)), this, SIGNAL(startNewGame()));
}

void ScoreFrame::updateDisplayedValues(int computerScore, int playerScore, int draws)
{
    m_ComputerScoreLabel->setText(QString::number(computerScore));
    m_PlayerScoreLabel->setText(QString::number(playerScore));
	m_DrawsLabel->setText(QString::number(draws));
}

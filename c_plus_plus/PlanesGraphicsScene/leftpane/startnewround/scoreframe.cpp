#include "scoreframe.h"

#include <QGridLayout>

ScoreFrame::ScoreFrame(QWidget* parent): QFrame(parent)
{
    QString titleText = QString("<b> General Score</b>");
    QLabel* titleLabel = new QLabel();
    titleLabel->setText(titleText);
    QLabel* computerScoreTextLabel = new QLabel("Computer: ");
    QLabel* playerScoreTextLabel = new QLabel("Player: ");
	QLabel* drawsTextLabel = new QLabel("Draws: ");
    m_ComputerScoreLabel = new QLabel("0");
    m_PlayerScoreLabel = new QLabel("0");
	m_DrawsLabel = new QLabel("0");
    m_StartGameButton = new QPushButton("Start New Round");
    QGridLayout* gridLayout1 = new QGridLayout();
    gridLayout1->addWidget(titleLabel);
    gridLayout1->addWidget(computerScoreTextLabel, 1, 0);
    gridLayout1->addWidget(playerScoreTextLabel, 2, 0);
	gridLayout1->addWidget(drawsTextLabel, 3, 0);
    gridLayout1->addWidget(m_ComputerScoreLabel, 1, 1);
    gridLayout1->addWidget(m_PlayerScoreLabel, 2, 1);
	gridLayout1->addWidget(m_DrawsLabel, 3, 1);
    gridLayout1->addWidget(m_StartGameButton, 4, 0);
    setLayout(gridLayout1);
    setFrameStyle(QFrame::Panel | QFrame::Raised);
    connect(m_StartGameButton, SIGNAL(clicked(bool)), this, SIGNAL(startNewGame()));
}

void ScoreFrame::updateDisplayedValues(int computerScore, int playerScore, int draws)
{
    m_ComputerScoreLabel->setText(QString::number(computerScore));
    m_PlayerScoreLabel->setText(QString::number(playerScore));
	m_DrawsLabel->setText(QString::number(draws));
}

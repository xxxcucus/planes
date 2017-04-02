#include "scoreframe.h"

#include <QGridLayout>

ScoreFrame::ScoreFrame(QWidget* parent): QFrame(parent)
{
    QString titleText = QString("<b> General Score</b>");
    QLabel* titleLabel = new QLabel();
    titleLabel->setText(titleText);
    QLabel* computerScoreTextLabel = new QLabel("Computer Wins: ");
    QLabel* playerScoreTextLabel = new QLabel("Player wins: ");
    m_ComputerScoreLabel = new QLabel("0");
    m_PlayerScoreLabel = new QLabel("0");
    m_StartGameButton = new QPushButton("Start new game");
    QGridLayout* gridLayout1 = new QGridLayout();
    gridLayout1->addWidget(titleLabel);
    gridLayout1->addWidget(computerScoreTextLabel, 1, 0);
    gridLayout1->addWidget(playerScoreTextLabel, 2, 0);
    gridLayout1->addWidget(m_ComputerScoreLabel, 1, 1);
    gridLayout1->addWidget(m_PlayerScoreLabel, 2, 1);
    gridLayout1->addWidget(m_StartGameButton, 3, 0);
    setLayout(gridLayout1);
    setFrameStyle(QFrame::Panel | QFrame::Raised);
}

void ScoreFrame::updateDisplayedValues(int computerScore, int playerScore)
{
    m_ComputerScoreLabel->setText(QString::number(computerScore));
    m_PlayerScoreLabel->setText(QString::number(playerScore));
}

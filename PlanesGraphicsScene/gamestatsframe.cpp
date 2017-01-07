#include "gamestatsframe.h"
#include <QGridLayout>

GameStatsFrame::GameStatsFrame(const QString& title, QWidget* parent): QFrame(parent)
{
    QString titleText = QString("<b>") + title + QString("</b>");
    QLabel* playerNameTextLabel = new QLabel();
    playerNameTextLabel->setText(titleText);
    QLabel* noMovesTextLabel = new QLabel("Number of moves");
    QLabel* noMissesTextLabel = new QLabel("Number of misses");
    QLabel* noHitsTextLabel = new QLabel("Number of hits");
    QLabel* noGuessesTextLabel = new QLabel("Number of planes guessed");

    m_noMovesLabel = new QLabel("0");
    m_noMissesLabel = new QLabel("0");
    m_noHitsLabel = new QLabel("0");
    m_noGuessesLabel = new QLabel("0");

    QGridLayout* gridLayout = new QGridLayout();
    gridLayout->addWidget(playerNameTextLabel, 0, 0, 1, 2);
    gridLayout->addWidget(noMovesTextLabel, 1, 0);
    gridLayout->addWidget(m_noMovesLabel, 1, 1);
    gridLayout->addWidget(noMissesTextLabel, 2, 0);
    gridLayout->addWidget(m_noMissesLabel, 2, 1);
    gridLayout->addWidget(noHitsTextLabel, 3, 0);
    gridLayout->addWidget(m_noHitsLabel, 3, 1);
    gridLayout->addWidget(noGuessesTextLabel, 4, 0);
    gridLayout->addWidget(m_noGuessesLabel, 4, 1);

    setLayout(gridLayout);
    setFrameStyle(QFrame::Panel | QFrame::Raised);
}

#ifndef SCOREFRAME_H
#define SCOREFRAME_H


#include <QFrame>
#include <QLabel>
#include <QPushButton>


///statistics for player or computer
///it is a QFrame containing labels
///the values in labels can be read and set via member variables
class ScoreFrame : public QFrame
{
    Q_OBJECT

public:
    explicit ScoreFrame(QWidget* parent = nullptr);
    void updateDisplayedValues(int computerScore, int playerScore);

public:
    QLabel* m_PlayerScoreLabel = new QLabel("0");
    QLabel* m_ComputerScoreLabel = new QLabel("0");
    QPushButton* m_StartGameButton;
};


#endif // SCOREFRAME_H

#ifndef GAMESTATSFRAME_H
#define GAMESTATSFRAME_H

#include <QFrame>
#include <QLabel>


///statistics for player or computer
///it is a QFrame containing labels
///the values in labels can be read and set via member variables
class GameStatsFrame : public QFrame
{
    Q_OBJECT

public:
    explicit GameStatsFrame(const QString& title, QWidget* parent = nullptr);

    QLabel* m_noMovesLabel = new QLabel("0");
    QLabel* m_noMissesLabel = new QLabel("0");
    QLabel* m_noHitsLabel = new QLabel("0");
    QLabel* m_noGuessesLabel = new QLabel("0");
};

#endif // GAMESTATSFRAME_H

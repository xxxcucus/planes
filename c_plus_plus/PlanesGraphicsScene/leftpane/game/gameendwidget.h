#ifndef __GAME_END_WIDGET__
#define __GAME_END_WIDGET__

#include <QFrame>
#include <QLabel>

class GameEndWidget : public QFrame
{
    Q_OBJECT

public:
    explicit GameEndWidget(QWidget* parent = nullptr);
    //void updateDisplayedValues(int moves, int misses, int hits, int kills); 

public:
    /*QLabel* m_GameName;
    QLabel* m_FirstPlayerName;
    QLabel* m_SecondPlayerName;
    QLabel* m_RoundName;*/
};

#endif

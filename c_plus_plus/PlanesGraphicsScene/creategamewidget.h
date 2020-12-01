#ifndef __CREATE_GAME_WIDGET__
#define __CREATE_GAME_WIDGET__

#include <QFrame>
#include <QLineEdit>


class CreateGameWidget : public QFrame
{
    Q_OBJECT

public:
    explicit CreateGameWidget(QWidget* parent = nullptr);
    //void updateDisplayedValues(int moves, int misses, int hits, int kills); TODO

public:
    QLineEdit* m_GameName;
    /*QLabel* m_FirstPlayerName;
    QLabel* m_SecondPlayerName;
    QLabel* m_RoundName;*/
};








#endif

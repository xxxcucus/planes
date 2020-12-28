#ifndef __GAME_STATUS_WIDGET__
#define __GAME_STATUS_WIDGET__

#include <QFrame>
#include <QLabel>


class GameStatusWidget : public QFrame
{
    Q_OBJECT

public:
    explicit GameStatusWidget(QWidget* parent = nullptr);
    //void updateDisplayedValues(int moves, int misses, int hits, int kills); TODO

public slots:
    void gameCreatedSlot(const QString& gameName, const QString& username);
    void gameConnectedToSlot(const QString& gameName, const QString& firstPlayerName, const QString& secondPlayerName);
    
public:
    QLabel* m_GameName;
    QLabel* m_FirstPlayerName;
    QLabel* m_SecondPlayerName;
    QLabel* m_RoundName;
};





#endif

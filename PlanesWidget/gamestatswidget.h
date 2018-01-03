#ifndef GAMESTATSWIDGET_H
#define GAMESTATSWIDGET_H

#include "ui_GameForm.h"
#include "planeround.h"

//defines the widget that displays the game statistics
//and contains the New Round button
class GameStatsWidget:public QWidget, public Ui::GameForm
{
    Q_OBJECT


public:
    GameStatsWidget( QWidget *parent=0);

    //resets the spin boxes and disables the new round button
    void reset();    


signals:
    void startGame();

public slots:
    void roundEndet();
    void updateStats(GameStatistics gs);
    void startNewRound();

};

#endif // GameStatsWidget_H

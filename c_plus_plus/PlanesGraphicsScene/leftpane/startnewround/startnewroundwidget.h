#ifndef __START_NEW_ROUND_WIDGET__
#define __START_NEW_ROUND_WIDGET__

#include <QWidget>

#include "scoreframe.h"
#include "gamestatistics.h"

class StartNewRoundWidget: public QWidget {
    Q_OBJECT
    
public:
    StartNewRoundWidget(QWidget *parent = 0);
    void updateDisplayedValues(const GameStatistics& gs);
    void deactivateStartRoundButton();
    void activateStartRoundButton();
    
signals:
    void startNewGame();
    
private:
    ScoreFrame* m_ScoreFrame;    
};
















#endif

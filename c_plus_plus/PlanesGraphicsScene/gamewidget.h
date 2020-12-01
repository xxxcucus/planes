#ifndef __GAME_WIDGET__
#define __GAME_WIDGET__

#include <QWidget>
#include "gameinfo.h"

class GameWidget : public QWidget {
    
public:
    GameWidget(GameInfo* gameInfo, QWidget* parent = nullptr);
    
private:
    GameInfo* m_GameInfo;
    
};

#endif

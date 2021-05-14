#ifndef __STATUS_BAR_WIDGET__
#define __STATUS_BAR_WIDGET__

#include <QWidget>
#include <QLabel>
#include "global/globaldata.h"
#include "gameinfo.h"

class StatusBarWidget : public QWidget {
    
public:
    StatusBarWidget(GameInfo* gameInfo, GlobalData* globalData, QWidget* parent = nullptr);

private:    
    GlobalData* m_GlobalData;
    GameInfo* m_GameInfo;
    
    QLabel* m_StatusLabel;
};

#endif

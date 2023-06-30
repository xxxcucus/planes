#ifndef __STATUS_BAR_WIDGET__
#define __STATUS_BAR_WIDGET__

#include <QWidget>
#include <QLabel>
#include "global/globaldata.h"
#include "gameinfo.h"
#include <QLabel>
#include <QPushButton>

class StatusBarWidget : public QWidget {
    Q_OBJECT
public:
    StatusBarWidget(GameInfo* gameInfo, GlobalData* globalData, QWidget* parent = nullptr);

public slots:
    void updateSlot();

signals:
    void logoutPressed();

private:    
    GlobalData* m_GlobalData;
    GameInfo* m_GameInfo;
    
    QLabel* m_StatusLabel;
    QPushButton* m_LogoutPushButton;
};

#endif

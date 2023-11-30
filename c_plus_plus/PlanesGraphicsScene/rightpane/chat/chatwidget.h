#ifndef __CHAT_WIDGET_
#define __CHAT_WIDGET_

#include "playerslistwidget.h"
#include <QStackedWidget>
#include <QTextEdit>
#include <map>

class ChatWidget : public QWidget {
    Q_OBJECT

public:
    ChatWidget(GlobalData* globalData, MultiplayerRound* multiRound, QWidget* parent = nullptr);
    void setActive(bool active);

private:
    PlayersListWidget* m_PlayersListWidget;
    std::map<QString, QTextEdit*> m_ChatSessions;
    QStackedWidget* m_ChatStackedWidget;
    GlobalData* m_GlobalData;
    MultiplayerRound* m_MultiRound;
};

#endif

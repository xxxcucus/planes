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
    PlayersListWidget* m_PlayersListWidget = nullptr;
    std::map<QString, QTextEdit*> m_ChatSessions;
    QStackedWidget* m_ChatStackedWidget = nullptr;
    GlobalData* m_GlobalData = nullptr;
    MultiplayerRound* m_MultiRound = nullptr;
};

#endif

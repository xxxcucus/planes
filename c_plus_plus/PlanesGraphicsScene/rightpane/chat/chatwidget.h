#ifndef __CHAT_WIDGET_
#define __CHAT_WIDGET_

#include "playerslistwidget.h"
#include <QStackedWidget>
#include <QTextEdit>
#include <QLineEdit>
#include <QPushButton>
#include <QSettings>
#include <map>


class ChatWidget : public QWidget {
    Q_OBJECT

public:
    ChatWidget(GlobalData* globalData, MultiplayerRound* multiRound, QSettings* settings, QWidget* parent = nullptr);
    void setActive(bool active);

public slots:
    void openChatWindow(const QString& player);
    void sendMessageToPlayer();
    void chatMessageReceived(const QString& sender, long int senderid, const QString& message);
    void chatConnectionError(const QString& errorMessage);

private:
    PlayersListWidget* m_PlayersListWidget = nullptr;
    std::map<QString, QTextEdit*> m_ChatSessions;
    QString m_CurrentReceiver;
    long int m_CurrentReceiverId = 0L;
    QStackedWidget* m_ChatStackedWidget = nullptr;
    QLineEdit* m_MessageLineEdit = nullptr;
    QPushButton* m_SendMessageButton = nullptr;
    GlobalData* m_GlobalData = nullptr;
    MultiplayerRound* m_MultiRound = nullptr;
    QSettings* m_Settings = nullptr;
};

#endif

#ifndef __CREATE_GAME_WIDGET__
#define __CREATE_GAME_WIDGET__

#include <QFrame>
#include <QLineEdit>
#include <QNetworkReply>
#include <QNetworkAccessManager>
#include <QSettings>

#include "global/globaldata.h"
#include "gameinfo.h"
#include "multiplayerround.h"


class CreateGameWidget : public QFrame
{
    Q_OBJECT

public:
    CreateGameWidget(MultiplayerRound* mrd, QWidget* parent = nullptr);

public slots:
    void createGameSlot();
    void connectToGameSlot();
    
public:
    QLineEdit* m_GameName;
    GlobalData* m_GlobalData;
    GameInfo* m_GameInfo;
    QNetworkAccessManager* m_NetworkManager;
    QSettings* m_Settings;
    MultiplayerRound* m_MultiRound;
};








#endif

#ifndef __CREATE_GAME_WIDGET__
#define __CREATE_GAME_WIDGET__

#include <QFrame>
#include <QLineEdit>
#include <QNetworkReply>
#include <QNetworkAccessManager>
#include <QSettings>
#include <QComboBox>
#include <QLabel>
#include <QTextEdit>

#include "global/globaldata.h"
#include "gameinfo.h"
#include "multiplayerround.h"


class CreateGameWidget : public QFrame
{
    Q_OBJECT

public:
    CreateGameWidget(MultiplayerRound* mrd, QWidget* parent = nullptr);
    QString getGameName();
    
    
signals:
    void connectToGameCalled();
    
public slots:
    void createGameSlot();
    void connectToGameSlot();
    
    void submitButtonClickedSlot();
    void choiceToCreateGameSlot(bool exists, const QString& gameName, const QString& firstPlayerName, const QString& secondPlayerName, const QString& currentRoundId);
    
public:
    QLineEdit* m_GameName;
    QComboBox* m_ChooseActionComboBox;
    QString m_ConnectToGameHelp = QString("You connect to an existing game.The game must have been created by your opponent.After connecting you can directly start playing.");
    QString m_CreateGameHelp = QString("You create a new game. After game creation you have to wait for the opponent to connect to the game. Click \"Refresh\" to verify that the opponent is connected to the game. After the two players are listed in the \"Game Status\" you can start playing.");
    QTextEdit* m_ActionDescriptionLabel;
    bool m_SubmitCalled = false;
    GlobalData* m_GlobalData;
    GameInfo* m_GameInfo;
    QNetworkAccessManager* m_NetworkManager;
    QSettings* m_Settings;
    MultiplayerRound* m_MultiRound;
};








#endif

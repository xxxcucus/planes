#ifndef __MULTIPLAYER_ROUND__
#define __MULTIPLAYER_ROUND__

#if defined MAKE_MULTIPLAYERROUND_LIB
#define MULTIPLAYER_EXPORT Q_DECL_EXPORT
#else
#define MULTIPLAYER_EXPORT Q_DECL_IMPORT
#endif

#include <QObject>
#include <QNetworkAccessManager>
#include <QSettings>
#include <QJsonObject>
#include <QNetworkReply>
#include <QTimer>
#include <QWidget>
#include "abstractplaneround.h"
#include "gameinfo.h"
#include "global/globaldata.h"
#include "communicationobjects/creategamecommobj.h"
#include "communicationobjects/connecttogamecommobj.h"
#include "communicationobjects/refreshgamestatuscommobj.h"
#include "communicationobjects/logincommobj.h"
#include "communicationobjects/registercommobj.h"
#include "communicationobjects/norobotcommobj.h"
#include "communicationobjects/sendplanepositionscommobj.h"
#include "communicationobjects/acquireopponentpositionscommobj.h"
#include "communicationobjects/sendmovecommobj.h"
#include "communicationobjects/requestopponentmovescommobj.h"
#include "communicationobjects/cancelroundcommobj.h"
#include "communicationobjects/startnewroundcommobj.h"
#include "communicationobjects/sendwinnercommobj.h"
#include "communicationobjects/getserverversioncommobj.h"
#include "communicationobjects/logoutcommobj.h"
#include "communicationobjects/playerslistcommobj.h"
#include "communicationobjects/deactivateusercommobj.h"
#include "communicationobjects/receivechatmessagescommobj.h"
#include "communicationobjects/sendchatmessagecommobj.h"
#include <vector>
#include "viewmodels/userwithlastloginviewmodel.h"

class MULTIPLAYER_EXPORT MultiplayerRound : public QObject, public AbstractPlaneRound  {
    Q_OBJECT
    
private:
    
    int m_PlayerMoveIndex = 0;
    int m_ComputerMoveIndex = 0;
    bool m_WinnerFound = false;
    QWidget* m_ParentWidget;
    QNetworkAccessManager* m_NetworkManager;
    GlobalData* m_GlobalData;
    QSettings* m_Settings;
    GameInfo* m_GameInfo;
        
    CreateGameCommObj* m_CreateGameObj;
    ConnectToGameCommObj* m_ConnectToGameObj;
    RefreshGameStatusCommObj* m_RefreshGameStatusCommObj;
    LoginCommObj* m_LoginCommObj;
    RegisterCommObj* m_RegisterCommObj;
    NoRobotCommObj* m_NoRobotCommObj;
    SendPlanePositionsCommObj* m_SendPlanePositionsCommObj;
    AcquireOpponentPositionsCommObj* m_AcquireOpponentPlanePositions;
    SendMoveCommObj* m_SendMoveCommObj;
    //RequestOpponentMovesCommObj* m_RequestOpponentMovesObj;
    CancelRoundCommObj* m_CancelRoundCommObj;
    StartNewRoundCommObj* m_StartNewRoundCommObj;
    SendWinnerCommObj* m_SendWinnerCommObj;
    GetServerVersionCommObj* m_GetServerVersionCommObj;
    LogoutCommObj* m_LogoutCommObj;
    PlayersListCommObj* mPlayersListCommObj;
    DeactivateUserCommObj* m_DeactivateUserCommObj;
    ReceiveChatMessagesCommObj* m_ReceiveChatMessagesCommObj;
    SendChatMessageCommObj* m_SendChatMessageCommObj;
    
    
    std::vector<int> m_NotSentMoves;
    std::vector<int> m_ReceivedMoves;

private slots:    
    void connectedToGameSlot(const QString& gameName, const QString& firstPlayerName, const QString& secondPlayerName, const QString& currentRoundId, bool resetGameScore);
    void gameCreatedSlot(const QString& gameName, const QString& userName, bool resetGameScore);
    
    /**
     * When all the planes were discovered but the last moves were not sent to the server (because too fast clicking)
     * */
    void sendLastMoves();
    
    /**
     * No postponed sends, all the moves were sent. Emit allMovesSent when the player guessed the planes positions
     * **/
    void allMovesSentSlot();

    void completeLogout();
    void userDeactivatedSlot();
    
    void chatMessagesReceived(const std::vector<ReceivedChatMessageViewModel>& messages);

signals:
    void opponentMoveGenerated(const GuessPoint& gp);
    void roundWasCancelled();
    void newRoundStarted();

    void gameCreated(const QString& gameName, const QString& userName);
    void gameConnectedTo(const QString& gameName, const QString& firstPlayerName, const QString& secondPlayerName, const QString& currentRoundId);
    void refreshStatus(bool exists, const QString& gameName, const QString& firstPlayerName, const QString& secondPlayerName, const QString& currentRoundId);
    void loginCompleted();
    void loginFailed();
    void noRobotRegistration(const std::vector<QString>& images, const QString& question, long int requestId);
    void registrationFailed();
    void registrationComplete();
    
    void opponentPlanePositionsReceived();
    void waitForOpponentPlanePositions();
    void winnerSent(bool isPlayerWinner, bool draw);
    void gameStatsUpdated(const GameStatistics& gameStats);
    
    void allMovesSent();
    void logoutCompleted();
    void playersListReceived(const std::vector<UserWithLastLoginViewModel>& playersList);
    void userDeactivated();

    void connectedToChatServer();
    void disconnectedFromChatServer();
    void connectedToChat();
    void chatMessageReceived(const ReceivedChatMessageViewModel& message);
    void chatConnectionError(const QString& error);

public:
    MultiplayerRound(int rows, int cols, int planeNo, QWidget* parentWidget, QNetworkAccessManager* networkManager, GlobalData* globalData, QSettings* settings, GameInfo* gameInfo);
    virtual ~MultiplayerRound();
    void reset() override;
    virtual void initRound() override;

    void playerGuess(const GuessPoint& gp, PlayerGuessReaction& pgr) override;
    void playerGuessIncomplete(int row, int col, GuessPoint::Type& guessRes, PlayerGuessReaction& pgr) override;
    
    virtual void addOpponentMove(GuessPoint& gp, int moveIndex);
    
    long int getRoundId() {
        return m_GlobalData->m_GameData.m_RoundId;
    }
    
    virtual void getPlayerPlaneNo(int pos, Plane& pl);
    virtual bool setComputerPlanes(int plane1_x, int plane1_y, Plane::Orientation plane1_orient, int plane2_x, int plane2_y, Plane::Orientation plane2_orient, int plane3_x, int plane3_y, Plane::Orientation plane3_orient); 
    
    virtual void setRoundCancelled() override;
    
    //Requests to server
    void createGame(const QString& gameName);
    void connectToGame(const QString& gameName);
    void refreshGameStatus(const QString& gameName);
    void login(const QString& username, const QString& password);
    void logout(const QString& username);
    void registerUser(const QString& username, const QString& password);
    void noRobotRegister(const QString& requestId, const QString& answer);
    void sendPlanePositions();
    void acquireOpponentPlanePositions();
    void sendMove(const GuessPoint& gp, int ownMoveIndex);
    void requestOpponentMoves();
    void cancelRound();
    void startNewRound();
    void sendWinner(bool draw, long int winnerId);
    
    void deleteFromNotSentList(int value); 
    void addToNotSentList(int value);
    bool moveAlreadyReceived(int moveIndex);
    void addToReceivedList(int value);

    void testServerVersion();
    void requestLoggedInPlayers(int lastLoginDay);
    void deactivateUser();

    void requestChatMessages();
    void sendMessageThroughChat(const QString& receiver, long int receiverid, const QString& message, long int messageId);

protected:
    MultiplayerRound() {}

private:
    bool validateOpponentMovesReply(const QJsonObject& reply);
    void startNewRound(long int desiredRoundId);
    bool checkRoundEnd(bool& draw, long int& winnerId, bool& isPlayerWinner);
};













#endif

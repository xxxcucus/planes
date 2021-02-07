#ifndef __MULTIPLAYER_ROUND__
#define __MULTIPLAYER_ROUND__

#include <QObject>
#include <QNetworkAccessManager>
#include <QSettings>
#include <QJsonObject>
#include <QNetworkReply>
#include "abstractplaneround.h"
#include "global/globaldata.h"
#include "communicationobjects/creategamecommobj.h"

class MultiplayerRound : public QObject, public AbstractPlaneRound  {
    Q_OBJECT
    
private:
    
    //TODO: computer moves with index so that it also works when replies come in different order as they were sent
    long int m_RoundId = 0;
    int m_PlayerMoveIndex = 0;
    int m_ComputerMoveIndex = 0;
    QNetworkAccessManager* m_NetworkManager;
    GlobalData* m_GlobalData;
    QSettings* m_Settings;
    QNetworkReply* m_PlayerMoveReply = nullptr; //TODO list of network replies
    QNetworkReply* m_OpponentMoveReply = nullptr;
    //TODO: to add game info to check for multi-single player in network operations
    
    bool m_IsSinglePlayer = false;
    
    CreateGameCommObj* m_CreateGameObj;

    
private slots:
    void errorNewMoveClicked(QNetworkReply::NetworkError code);
    void finishedNewMoveClicked();    
    void finishedAcquireOpponentMoves();
    void errorAcquireOpponentMoves(QNetworkReply::NetworkError code);
    
signals:
    void opponentMoveGenerated(const GuessPoint& gp);
    void roundWasCancelled();

    void gameCreated(const QString& gameName, const QString& userName);


public:
    MultiplayerRound(int rows, int cols, int planeNo, QNetworkAccessManager* networkManager, GlobalData* globalData, QSettings* settings);
    void reset() override;
    void initRound() override;

    void playerGuess(const GuessPoint& gp, PlayerGuessReaction& pgr) override;
    void playerGuessIncomplete(int row, int col, GuessPoint::Type& guessRes, PlayerGuessReaction& pgr) override;
    
    void requestOpponentMoves();
    
    void setRoundId(long int id) {
        m_RoundId = id;
    }
    long int getRoundId() {
        return m_RoundId;
    }
    
    void getPlayerPlaneNo(int pos, Plane& pl);
    bool setComputerPlanes(int plane1_x, int plane1_y, Plane::Orientation plane1_orient, int plane2_x, int plane2_y, Plane::Orientation plane2_orient, int plane3_x, int plane3_y, Plane::Orientation plane3_orient); 
    
    //TODO to add method also in normal round
    void roundCancelled();


    //Requests to server
    void createGame(const QString& gameName);

    
private:
    bool validateOpponentMovesReply(const QJsonObject& reply);
    
};













#endif

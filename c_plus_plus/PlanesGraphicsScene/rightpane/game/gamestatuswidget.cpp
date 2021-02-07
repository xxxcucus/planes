#include "gamestatuswidget.h"

#include <QGridLayout>
#include <QPushButton>
#include <QDebug>
#include <QMessageBox>
#include <QTextCodec>
#include "viewmodels/gameviewmodel.h"
#include "communicationtools.h"

GameStatusWidget::GameStatusWidget(GlobalData* globalData, QSettings* settings, QNetworkAccessManager* networkManager, GameInfo* gameInfo, MultiplayerRound* mrd, QWidget* parent) 
    : QFrame(parent), m_GlobalData(globalData), m_Settings(settings), m_NetworkManager(networkManager), m_GameInfo(gameInfo), m_MultiRound(mrd)
{
    QString titleText = QString("<b> Game Status </b>");
    QLabel* titleLabel = new QLabel("");
    titleLabel->setText(titleText);
    
    QLabel* gameNameLabel = new QLabel("Game Name");
    m_GameName = new QLabel("No game created");
    
    QLabel* firstPlayerNameLabel = new QLabel("Player 1");
    m_FirstPlayerName = new QLabel("");
    QLabel* secondPlayerNameLabel = new QLabel("Player 2");
    m_SecondPlayerName = new QLabel("");
    
    QLabel* roundNameLabel = new QLabel("Round Id");
    m_RoundName = new QLabel("No round started");
    
    QPushButton* refreshStatusButton = new QPushButton("Refresh");
    
    QGridLayout* gridLayout = new QGridLayout();
    gridLayout->addWidget(titleLabel, 0, 0, 1, 2);
    gridLayout->addWidget(gameNameLabel, 1, 0);
    gridLayout->addWidget(m_GameName, 1, 1);
    gridLayout->addWidget(firstPlayerNameLabel, 2, 0);
    gridLayout->addWidget(m_FirstPlayerName, 2, 1);
    gridLayout->addWidget(secondPlayerNameLabel, 3, 0);
    gridLayout->addWidget(m_SecondPlayerName, 3, 1);
    gridLayout->addWidget(roundNameLabel, 4, 0);
    gridLayout->addWidget(m_RoundName, 4, 1);
    gridLayout->addWidget(refreshStatusButton, 5, 1);

    setLayout(gridLayout);
    setFrameStyle(QFrame::Panel | QFrame::Raised);

    connect(refreshStatusButton, &QPushButton::clicked, this, &GameStatusWidget::refreshSlot);
    
}

void GameStatusWidget::gameCreatedSlot(const QString& gameName, const QString& username)
{
    m_GameName->setText(gameName);
    m_FirstPlayerName->setText(username);
    m_SecondPlayerName->clear();
    m_RoundName->clear();
}

void GameStatusWidget::gameConnectedToSlot(const QString& gameName, const QString& firstPlayerName, const QString& secondPlayerName, const QString& currentRoundId) 
{
    m_GameName->setText(gameName);
    m_FirstPlayerName->setText(firstPlayerName);
    m_SecondPlayerName->setText(secondPlayerName);
    m_RoundName->setText(currentRoundId);
}

void GameStatusWidget::refreshSlot()
{
    if (m_GameName->text().isEmpty())
        return;


    GameViewModel gameData;
    gameData.m_GameName = m_GameName->text(); //TODO: validation
    gameData.m_GameId = 0;
    gameData.m_UserId = 0;
    gameData.m_Username = QString();

    if (m_RefreshGameStatusReply != nullptr)
        delete m_RefreshGameStatusReply;
    
  
    m_RefreshGameStatusReply = CommunicationTools::buildPostRequestWithAuth("/game/status", m_Settings->value("multiplayer/serverpath").toString(), gameData.toJson(), m_GlobalData->m_UserData.m_AuthToken, m_NetworkManager);

    connect(m_RefreshGameStatusReply, &QNetworkReply::finished, this, &GameStatusWidget::finishedRefreshStatus);
    connect(m_RefreshGameStatusReply, &QNetworkReply::errorOccurred, this, &GameStatusWidget::errorRefreshStatus);
}


void GameStatusWidget::errorRefreshStatus(QNetworkReply::NetworkError code)
{
    if (m_GameInfo->getSinglePlayer())
        return;

    CommunicationTools::treatCommunicationError("refreshing game status ", m_RefreshGameStatusReply);
}

void GameStatusWidget::finishedRefreshStatus()
{
    if (m_GameInfo->getSinglePlayer())
        return;

    if (m_RefreshGameStatusReply->error() != QNetworkReply::NoError) {
        return;
    }
    
    QByteArray reply = m_RefreshGameStatusReply->readAll();
    QString refreshStatusReplyQString = QTextCodec::codecForMib(106)->toUnicode(reply);
    QJsonObject refreshStatusReplyJson = CommunicationTools::objectFromString(refreshStatusReplyQString);
    
    if (!validateRefreshStatusReply(refreshStatusReplyJson)) {
        QMessageBox msgBox;
        msgBox.setText("Game status was not refreshed"); 
        msgBox.exec();

        return;
    }
    
    m_GlobalData->m_GameData.m_GameId = refreshStatusReplyJson.value("id").toString().toLong();
    m_GlobalData->m_GameData.m_RoundId = (long int)refreshStatusReplyJson.value("currentRoundId").toDouble();
    
    QString gameName = refreshStatusReplyJson.value("gameName").toString();
    QString firstPlayerName = refreshStatusReplyJson.value("firstPlayerName").toString();
    QString secondPlayerName = refreshStatusReplyJson.value("secondPlayerName").toString();
    QString currentRoundId = refreshStatusReplyJson.value("currentRoundId").toString();
    
    m_GlobalData->m_GameData.m_GameId = refreshStatusReplyJson.value("id").toString().toLong(); //TODO conversion errors
    m_GlobalData->m_GameData.m_RoundId = refreshStatusReplyJson.value("currentRoundId").toString().toLong();
    m_GlobalData->m_GameData.m_UserId = m_GlobalData->m_UserData.m_UserId;
    long int userId1 = refreshStatusReplyJson.value("firstPlayerId").toString().toLong();
    long int userId2 = refreshStatusReplyJson.value("secondPlayerId").toString().toLong();
    qDebug() << "userId1 " << userId1;
    qDebug() << "userId2 " << userId2;
    qDebug() << "m_GlobalData.m_UserData.m_UserId " << m_GlobalData->m_UserData.m_UserId;
    m_GlobalData->m_GameData.m_OtherUserId = (userId1 == m_GlobalData->m_UserData.m_UserId) ? userId2 : userId1; //TODO validation m_UserData.m_UserId should be sent from server

    m_GameName->setText(gameName);
    m_FirstPlayerName->setText(firstPlayerName);
    m_SecondPlayerName->setText(secondPlayerName);
    m_RoundName->setText(currentRoundId); 
    emit gameStatusRefreshed(gameName, firstPlayerName, secondPlayerName, currentRoundId);
}

bool GameStatusWidget::validateRefreshStatusReply(const QJsonObject& reply) {
    return (reply.contains("id") && reply.contains("firstPlayerName") && reply.contains("secondPlayerName") && reply.contains("gameName") && reply.contains("currentRoundId")
        && reply.contains("firstPlayerId") && reply.contains("secondPlayerId"));
}



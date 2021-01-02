#include "creategamewidget.h"

#include <QPushButton>
#include <QGridLayout>
#include <QLabel>
#include <QTextCodec>
#include <QMessageBox>

#include "viewmodels/gameviewmodel.h"
#include "communicationtools.h"
#include "creategamewidget.h"

CreateGameWidget::CreateGameWidget(GlobalData* globalData, GameInfo* gameInfo, QNetworkAccessManager* networkManager, QSettings* settings, QWidget* parent) 
    : QFrame(parent), m_GlobalData(globalData), m_GameInfo(gameInfo), m_NetworkManager(networkManager), m_Settings(settings)
{
    QString titleText = QString("<b> Create Game </b>");
    QLabel* titleLabel = new QLabel("");
    titleLabel->setText(titleText);
    
    QLabel* gameNameLabel = new QLabel("Game Name");
    m_GameName = new QLineEdit();  //TODO: to add validation
    
    QPushButton* connectToGameButton = new QPushButton("Connect to Game");
    QPushButton* createAndConnectToGameButton = new QPushButton("Create and Connect to Game");
    
    QGridLayout* gridLayout = new QGridLayout();
    gridLayout->addWidget(titleLabel, 0, 0, 1, 2);
    gridLayout->addWidget(gameNameLabel, 1, 0);
    gridLayout->addWidget(m_GameName, 1, 1);
    gridLayout->addWidget(connectToGameButton, 2, 0, 1, 2);
    gridLayout->addWidget(createAndConnectToGameButton, 3, 0, 1, 2);

    setLayout(gridLayout);
    setFrameStyle(QFrame::Panel | QFrame::Raised);
    
    connect(createAndConnectToGameButton, &QPushButton::clicked, this, &CreateGameWidget::createGameSlot);
    connect(connectToGameButton, &QPushButton::clicked, this, &CreateGameWidget::connectToGameSlot);
}


void CreateGameWidget::createGameSlot() {
    if (m_GlobalData->m_UserData.m_UserName.isEmpty()) {
        QMessageBox msgBox;
        msgBox.setText("No user logged in"); 
        msgBox.exec();
        return;
    }

        
    GameViewModel gameData;
    gameData.m_GameName = m_GameName->text(); //TODO: validation
    gameData.m_Username = m_GlobalData->m_UserData.m_UserName; //TODO: validation and trim
    gameData.m_UserId = 0; 

    if (m_CreateGameReply != nullptr)
        delete m_CreateGameReply;
    
    m_GlobalData->m_GameData.reset();
  
    m_CreateGameReply = CommunicationTools::buildPostRequestWithAuth("/game/create", m_Settings->value("multiplayer/serverpath").toString(), gameData.toJson(), m_GlobalData->m_UserData.m_AuthToken, m_NetworkManager);

    connect(m_CreateGameReply, &QNetworkReply::finished, this, &CreateGameWidget::finishedCreateGame);
    connect(m_CreateGameReply, &QNetworkReply::errorOccurred, this, &CreateGameWidget::errorCreateGame);
}

void CreateGameWidget::connectToGameSlot() {
    if (m_GlobalData->m_UserData.m_UserName.isEmpty()) {
        QMessageBox msgBox;
        msgBox.setText("No user logged in"); 
        msgBox.exec();
        return;
    }

        
    GameViewModel gameData;
    gameData.m_GameName = m_GameName->text(); //TODO: validation
    gameData.m_Username = m_GlobalData->m_UserData.m_UserName; //TODO: validation and trim
    gameData.m_UserId = 0; 

    if (m_ConnectToGameReply != nullptr)
        delete m_ConnectToGameReply;
    
    m_GlobalData->m_GameData.reset();
  
    m_ConnectToGameReply = CommunicationTools::buildPostRequestWithAuth("/game/connect", m_Settings->value("multiplayer/serverpath").toString(), gameData.toJson(), m_GlobalData->m_UserData.m_AuthToken, m_NetworkManager);

    connect(m_ConnectToGameReply, &QNetworkReply::finished, this, &CreateGameWidget::finishedConnectToGame);
    connect(m_ConnectToGameReply, &QNetworkReply::errorOccurred, this, &CreateGameWidget::errorConnectToGame);
    
}

void CreateGameWidget::errorCreateGame(QNetworkReply::NetworkError code)
{
    if (m_GameInfo->getSinglePlayer())
        return;

    CommunicationTools::treatCommunicationError("creating game ", m_CreateGameReply);
}

void CreateGameWidget::finishedCreateGame()
{
    if (m_GameInfo->getSinglePlayer())
        return;

    if (m_CreateGameReply->error() != QNetworkReply::NoError) {
        return;
    }
    
    QByteArray reply = m_CreateGameReply->readAll();
    QString createGameReplyQString = QTextCodec::codecForMib(106)->toUnicode(reply);
    QJsonObject createGameReplyJson = CommunicationTools::objectFromString(createGameReplyQString);
 
    if (!validateCreateGameReply(createGameReplyJson)) {
        QMessageBox msgBox;
        msgBox.setText("Create game reply was not recognized"); 
        msgBox.exec();

        return;
    }
    
    QMessageBox msgBox;
    msgBox.setText("Game creation successfull!"); 
    msgBox.exec();               
    m_GlobalData->m_GameData.m_GameId = (long int)createGameReplyJson.value("id").toDouble();
    emit gameCreated(m_GameName->text(), m_GlobalData->m_UserData.m_UserName);
}

void CreateGameWidget::errorConnectToGame(QNetworkReply::NetworkError code)
{
    if (m_GameInfo->getSinglePlayer())
        return;

    CommunicationTools::treatCommunicationError("connecting to game ", m_ConnectToGameReply);
}

void CreateGameWidget::finishedConnectToGame()
{
    if (m_GameInfo->getSinglePlayer())
        return;

    if (m_ConnectToGameReply->error() != QNetworkReply::NoError) {
        return;
    }
    
    QByteArray reply = m_ConnectToGameReply->readAll();
    QString connectToGameReplyQString = QTextCodec::codecForMib(106)->toUnicode(reply);
    QJsonObject connectToGameReplyJson = CommunicationTools::objectFromString(connectToGameReplyQString);
    
    if (!validateCreateGameReply(connectToGameReplyJson)) {
        QMessageBox msgBox;
        msgBox.setText("Connect to game reply was not recognized"); 
        msgBox.exec();

        return;
    }
    
    QMessageBox msgBox;
    msgBox.setText("Connection to game successfull!"); 
    msgBox.exec();               
    
    qDebug() << connectToGameReplyQString;
    m_GlobalData->m_GameData.m_GameId = (long int)connectToGameReplyJson.value("id").toDouble();
    m_GlobalData->m_GameData.m_RoundId = (long int)connectToGameReplyJson.value("currentRoundId").toDouble();
    QString firstPlayerName = connectToGameReplyJson.value("firstPlayerName").toString();
    QString currentRoundId = QString::number(connectToGameReplyJson.value("currentRoundId").toInt()); //TODO send round id as string in server
    emit gameConnectedTo(m_GameName->text(), firstPlayerName, m_GlobalData->m_UserData.m_UserName, currentRoundId);
}


bool CreateGameWidget::validateCreateGameReply(const QJsonObject& reply) {
    return (reply.contains("id") && reply.contains("firstPlayerName") && reply.contains("secondPlayerName") && reply.contains("gameName") && reply.contains("currentRoundId"));
}




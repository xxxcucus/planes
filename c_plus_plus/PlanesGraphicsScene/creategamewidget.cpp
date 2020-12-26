#include "creategamewidget.h"

#include <QPushButton>
#include <QGridLayout>
#include <QLabel>
#include <QTextCodec>
#include <QMessageBox>

#include "gamedata.h"
#include "communicationtools.h"
#include "creategamewidget.h"

CreateGameWidget::CreateGameWidget(UserData* userData, GameInfo* gameInfo, QNetworkAccessManager* networkManager, QSettings* settings, QWidget* parent) 
    : QFrame(parent), m_UserData(userData), m_GameInfo(gameInfo), m_NetworkManager(networkManager), m_Settings(settings)
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
    if (m_UserData->m_UserName.isEmpty()) {
        QMessageBox msgBox;
        msgBox.setText("No user logged in"); 
        msgBox.exec();
        return;
    }

        
    GameData gameData;
    gameData.m_GameName = m_GameName->text(); //TODO: validation
    gameData.m_Username = m_UserData->m_UserName; //TODO: validation and trim
    gameData.m_UserId = 0; 

    if (m_CreateGameReply != nullptr)
        delete m_CreateGameReply;
    
    m_UserData->m_GameId = 0;
  
    m_CreateGameReply = CommunicationTools::buildPostRequestWithAuth("/game/create", m_Settings->value("multiplayer/serverpath").toString(), gameData.toJson(), m_UserData->m_AuthToken, m_NetworkManager);

    connect(m_CreateGameReply, &QNetworkReply::finished, this, &CreateGameWidget::finishedCreateGame);
    connect(m_CreateGameReply, &QNetworkReply::errorOccurred, this, &CreateGameWidget::errorCreateGame);
}

void CreateGameWidget::connectToGameSlot() {
    
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
    m_UserData->m_GameId = (long int)createGameReplyJson.value("id").toDouble();
    emit gameCreated(m_GameName->text(), m_UserData->m_UserName);
}

bool CreateGameWidget::validateCreateGameReply(const QJsonObject& reply) {
    return (reply.contains("id") && reply.contains("firstPlayerName") && reply.contains("secondPlayerName") && reply.contains("gameName"));
}




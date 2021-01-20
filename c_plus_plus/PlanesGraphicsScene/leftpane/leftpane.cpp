#include "leftpane.h"

#include <QLabel>
#include <QComboBox>
#include <QGridLayout>
#include <QPushButton>
#include <QSpacerItem>
#include <QTextCodec>
#include "viewmodels/planespositionsviewmodel.h"
#include "viewmodels/getopponentplanespositionsviewmodel.h"
#include "communicationtools.h"
#include <global/globalgamedata.h>

LeftPane::LeftPane(GameInfo* gameInfo, QNetworkAccessManager* networkManager, GlobalData* globalData, QSettings* settings, MultiplayerRound* mrd, QWidget *parent) 
    : QTabWidget(parent), m_GameInfo(gameInfo), m_NetworkManager(networkManager), m_GlobalData(globalData), m_Settings(settings), m_MultiRound(mrd)
{
    m_PlayerStatsFrame = new GameStatsFrame("Player");
    m_ComputerStatsFrame = new GameStatsFrame("Computer");
    QVBoxLayout* vLayout = new QVBoxLayout();
    vLayout->addWidget(m_PlayerStatsFrame);
    vLayout->addWidget(m_ComputerStatsFrame);
    vLayout->addStretch(5);
    m_GameWidget = new QWidget();
    m_GameWidget->setLayout(vLayout);

    m_BoardEditingWidget = new QWidget();
    m_selectPlaneButton = new QPushButton("Select plane");
    m_rotatePlaneButton = new QPushButton("Rotate plane");
    m_leftPlaneButton = new QPushButton("Plane to left");
    m_rightPlaneButton = new QPushButton("Plane to right");
    m_upPlaneButton = new QPushButton("Plane upwards");
    m_downPlaneButton = new QPushButton("Plane downwards");
    m_doneButton = new QPushButton("Done editing");
    m_acquireOpponentPositionsButton = new QPushButton("Acquired opponent planes positions");
    QSpacerItem* spacer = new QSpacerItem(50, 50, QSizePolicy::Expanding, QSizePolicy::Expanding);
    QGridLayout* gridLayout = new QGridLayout();
    gridLayout->addWidget(m_selectPlaneButton, 0, 0, 1, 3);
    gridLayout->addWidget(m_rotatePlaneButton, 1, 0, 1, 3);
    gridLayout->addWidget(m_upPlaneButton, 2, 1);
    gridLayout->addWidget(m_leftPlaneButton, 3, 0);
    gridLayout->addWidget(m_rightPlaneButton, 3, 2);
    gridLayout->addWidget(m_downPlaneButton, 4, 1);
    gridLayout->addWidget(m_doneButton, 5, 0, 1, 3);
    gridLayout->addWidget(m_acquireOpponentPositionsButton, 6, 0, 1, 3);
    gridLayout->addItem(spacer, 6, 0, 1, 3);
    gridLayout->setRowStretch(6, 5);
    m_BoardEditingWidget->setLayout(gridLayout);

    connect(m_selectPlaneButton, SIGNAL(clicked(bool)), this, SLOT(selectPlaneClickedSlot(bool)));
    connect(m_rotatePlaneButton, SIGNAL(clicked(bool)), this, SLOT(rotatePlaneClickedSlot(bool)));
    //connect(m_doneButton, SIGNAL(clicked(bool)), this, SIGNAL(doneClicked()));
    connect(m_doneButton, SIGNAL(clicked(bool)), this, SLOT(doneClickedSlot()));
    connect(m_upPlaneButton, SIGNAL(clicked(bool)), this, SLOT(upPlaneClickedSlot(bool)));
    connect(m_downPlaneButton, SIGNAL(clicked(bool)), this, SLOT(downPlaneClickedSlot(bool)));
    connect(m_leftPlaneButton, SIGNAL(clicked(bool)), this, SLOT(leftPlaneClickedSlot(bool)));
    connect(m_rightPlaneButton, SIGNAL(clicked(bool)), this, SLOT(rightPlaneClickedSlot(bool)));
    connect(m_acquireOpponentPositionsButton, SIGNAL(clicked(bool)), this, SLOT(acquireOpponentPositionsClickedSlot(bool)));
    
    m_GameTabIndex = addTab(m_GameWidget, "Round");
    m_EditorTabIndex = addTab(m_BoardEditingWidget, "BoardEditing");

    m_ScoreFrame = new ScoreFrame();
    QVBoxLayout* vLayout1 = new QVBoxLayout();
    vLayout1->addWidget(m_ScoreFrame);
    vLayout1->addStretch(5);
    m_StartGameWidget = new QWidget();
    m_StartGameWidget->setLayout(vLayout1);
    m_GameStartIndex = addTab(m_StartGameWidget, "Start Round");
    connect(m_ScoreFrame, SIGNAL(startNewGame()), this, SIGNAL(startNewGame()));

    activateEditorTab();
}

void LeftPane::activateDoneButton(bool planesOverlap)
{
    m_doneButton->setEnabled(!planesOverlap);
}

void LeftPane::doneClickedSlot()
{
    if (m_MultiRound->getRoundId() == 0 && !m_GameInfo->getSinglePlayer()) {
            QMessageBox msgBox;
            msgBox.setText("No round started. Connect to game or start a round!"); 
            msgBox.exec();
            return;
    }
    
    if (!m_GameInfo->getSinglePlayer()) {
        //send configuration to server
        submitDoneClicked();
    }

    if (m_GameInfo->getSinglePlayer()) {
        emit doneClicked();
        activateGameTab();
        m_selectPlaneButton->setEnabled(false);
        m_rotatePlaneButton->setEnabled(false);
        m_leftPlaneButton->setEnabled(false);
        m_rightPlaneButton->setEnabled(false);
        m_upPlaneButton->setEnabled(false);
        m_downPlaneButton->setEnabled(false);
        m_doneButton->setEnabled(false);
    }
}

//TODO : should allow only one time to click on done - after that the controlls should be disabled
void LeftPane::submitDoneClicked()
{
    PlanesPositionsViewModel planesPositionsData;
    planesPositionsData.m_RoundId = m_MultiRound->getRoundId();
    planesPositionsData.m_UserId = m_GlobalData->m_UserData.m_UserId;
    
    Plane pl1;
    Plane pl2;
    Plane pl3;
    
    qDebug() << "Plane 1 " << pl1.row() << " " << pl1.col() << " " << pl1.orientation();
    qDebug() << "Plane 2 " << pl2.row() << " " << pl2.col() << " " << pl2.orientation();
    qDebug() << "Plane 3 " << pl3.row() << " " << pl3.col() << " " << pl3.orientation();
    
    m_MultiRound->getPlayerPlaneNo(0, pl1);
    m_MultiRound->getPlayerPlaneNo(1, pl2);
    m_MultiRound->getPlayerPlaneNo(2, pl3);
    
    planesPositionsData.m_Plane1X = pl1.row();
    planesPositionsData.m_Plane1Y = pl1.col();
    planesPositionsData.m_Plane1Orient = pl1.orientation();
    planesPositionsData.m_Plane2X = pl2.row();
    planesPositionsData.m_Plane2Y = pl2.col();
    planesPositionsData.m_Plane2Orient = pl2.orientation();
    planesPositionsData.m_Plane3X = pl3.row();
    planesPositionsData.m_Plane3Y = pl3.col();
    planesPositionsData.m_Plane3Orient = pl3.orientation();
    
    qDebug() << "Plane 1 " << pl1.row() << " " << pl1.col() << " " << pl1.orientation();
    qDebug() << "Plane 2 " << pl2.row() << " " << pl2.col() << " " << pl2.orientation();
    qDebug() << "Plane 3 " << pl3.row() << " " << pl3.col() << " " << pl3.orientation();
    
    if (m_DoneClickedReply != nullptr)
        delete m_DoneClickedReply;

    m_DoneClickedReply = CommunicationTools::buildPostRequestWithAuth("/round/myplanespositions", m_Settings->value("multiplayer/serverpath").toString(), planesPositionsData.toJson(), m_GlobalData->m_UserData.m_AuthToken, m_NetworkManager);

    connect(m_DoneClickedReply, &QNetworkReply::finished, this, &LeftPane::finishedDoneClicked);
    connect(m_DoneClickedReply, &QNetworkReply::errorOccurred, this, &LeftPane::errorDoneClicked);

}

void LeftPane::errorDoneClicked(QNetworkReply::NetworkError code)
{
    if (m_GameInfo->getSinglePlayer())
        return;

    CommunicationTools::treatCommunicationError("sending plane positions ", m_DoneClickedReply);
}

void LeftPane::finishedDoneClicked()
{
    if (m_GameInfo->getSinglePlayer())
        return;

    if (m_DoneClickedReply->error() != QNetworkReply::NoError) {
        return;
    }
    
    QByteArray reply = m_DoneClickedReply->readAll();
    QString doneClickedReplyQString = QTextCodec::codecForMib(106)->toUnicode(reply);
    QJsonObject doneClickedReplyJson = CommunicationTools::objectFromString(doneClickedReplyQString);
 
    if (!validateDoneClickedReply(doneClickedReplyJson)) {
        QMessageBox msgBox;
        msgBox.setText("Planes positions sending reply was not recognized"); 
        msgBox.exec();

        return;
    }
    
    bool otherPositionsExist = doneClickedReplyJson.value("otherExist").toBool();
    if (otherPositionsExist) {
        //TODO treat errors
        int plane1_x = doneClickedReplyJson.value("plane1_x").toInt();
        int plane1_y = doneClickedReplyJson.value("plane1_y").toInt();
        int plane1_orient = doneClickedReplyJson.value("plane1_orient").toInt(); //TODO to check this
        int plane2_x = doneClickedReplyJson.value("plane2_x").toInt();
        int plane2_y = doneClickedReplyJson.value("plane2_y").toInt();
        int plane2_orient = doneClickedReplyJson.value("plane2_orient").toInt(); //TODO to check this
        int plane3_x = doneClickedReplyJson.value("plane3_x").toInt();
        int plane3_y = doneClickedReplyJson.value("plane3_y").toInt();
        int plane3_orient = doneClickedReplyJson.value("plane3_orient").toInt(); //TODO to check this        
        qDebug() << "Plane 1 from opponent " << plane1_x << " " << plane1_y << " " << plane1_orient;
        qDebug() << "Plane 2 from opponent" << plane2_x << " " << plane2_y << " " << plane2_orient;
        qDebug() << "Plane 3 from opponent" << plane3_x << " " << plane3_y << " " << plane3_orient;
        bool setOk = m_MultiRound->setComputerPlanes(plane1_x, plane1_y, (Plane::Orientation)plane1_orient, plane2_x, plane2_y, (Plane::Orientation)plane2_orient, plane3_x, plane3_y, (Plane::Orientation)plane3_orient);
        if (!setOk) {
            QMessageBox msgBox;
            msgBox.setText("Planes positions from opponent are not valid"); 
            msgBox.exec();
            return;            
        }
        emit doneClicked();
        activateGameTab();
        m_selectPlaneButton->setEnabled(false);
        m_rotatePlaneButton->setEnabled(false);
        m_leftPlaneButton->setEnabled(false);
        m_rightPlaneButton->setEnabled(false);
        m_upPlaneButton->setEnabled(false);
        m_downPlaneButton->setEnabled(false);
        m_doneButton->setEnabled(false);        
    } else {
        m_MultiRound->setCurrentStage(AbstractPlaneRound::GameStages::WaitForOpponentPlanesPositions);
        m_selectPlaneButton->setEnabled(false);
        m_rotatePlaneButton->setEnabled(false);
        m_leftPlaneButton->setEnabled(false);
        m_rightPlaneButton->setEnabled(false);
        m_upPlaneButton->setEnabled(false);
        m_downPlaneButton->setEnabled(false);
        m_doneButton->setEnabled(false);
        m_acquireOpponentPositionsButton->setEnabled(true); //TODO make sure this is consistent everywhere
        
        QMessageBox msgBox;
        msgBox.setText("Your opponent has not decided where he wants to place the planes yet\nPlease click on the \"Acquired opponent positions\" button! "); 
        msgBox.exec();
    }
}

bool LeftPane::validateDoneClickedReply(const QJsonObject& reply) {
   return (reply.contains("otherExist") && reply.contains("plane1_x") && reply.contains("plane1_y") && reply.contains("plane1_orient") && 
        reply.contains("plane2_x") && reply.contains("plane2_y") && reply.contains("plane2_orient") &&
        reply.contains("plane3_x") && reply.contains("plane3_y") && reply.contains("plane3_orient"));
}

void LeftPane::acquireOpponentPositionsClickedSlot(bool c)
{
    GetOpponentsPlanesPositionsViewModel opponentViewModel;
    opponentViewModel.m_RoundId = m_MultiRound->getRoundId();
    opponentViewModel.m_UserId = m_GlobalData->m_GameData.m_OtherUserId; 

    
    if (m_AcquireOpponentPositionsReply != nullptr)
        delete m_AcquireOpponentPositionsReply;

    m_AcquireOpponentPositionsReply = CommunicationTools::buildPostRequestWithAuth("/round/otherplanespositions", m_Settings->value("multiplayer/serverpath").toString(), opponentViewModel.toJson(), m_GlobalData->m_UserData.m_AuthToken, m_NetworkManager);

    connect(m_AcquireOpponentPositionsReply, &QNetworkReply::finished, this, &LeftPane::finishedAcquireOpponentPositions);
    connect(m_AcquireOpponentPositionsReply, &QNetworkReply::errorOccurred, this, &LeftPane::errorAcquireOpponentPositions);
}

void LeftPane::finishedAcquireOpponentPositions() {
    if (m_GameInfo->getSinglePlayer())
        return;

    if (m_AcquireOpponentPositionsReply->error() != QNetworkReply::NoError) {
        return;
    }
    
    QByteArray reply = m_AcquireOpponentPositionsReply->readAll();
    QString acquireOpponentPositionsReplyQString = QTextCodec::codecForMib(106)->toUnicode(reply);
    QJsonObject acquireOpponentPositionsReplyReplyJson = CommunicationTools::objectFromString(acquireOpponentPositionsReplyQString);
 
    if (!validateDoneClickedReply(acquireOpponentPositionsReplyReplyJson)) {
        QMessageBox msgBox;
        msgBox.setText("Acquire opponent positions reply was not recognized"); 
        msgBox.exec();

        return;
    }

    bool otherPositionsExist = acquireOpponentPositionsReplyReplyJson.value("otherExist").toBool();
    if (!otherPositionsExist) {
        QMessageBox msgBox;
        msgBox.setText("Opponents' planes positions are not available yet!"); 
        msgBox.exec();
        return;
    }
        
    int plane1_x = acquireOpponentPositionsReplyReplyJson.value("plane1_x").toInt();
    int plane1_y = acquireOpponentPositionsReplyReplyJson.value("plane1_y").toInt();
    int plane1_orient = acquireOpponentPositionsReplyReplyJson.value("plane1_orient").toInt(); //TODO to check this
    int plane2_x = acquireOpponentPositionsReplyReplyJson.value("plane2_x").toInt();
    int plane2_y = acquireOpponentPositionsReplyReplyJson.value("plane2_y").toInt();
    int plane2_orient = acquireOpponentPositionsReplyReplyJson.value("plane2_orient").toInt(); //TODO to check this
    int plane3_x = acquireOpponentPositionsReplyReplyJson.value("plane3_x").toInt();
    int plane3_y = acquireOpponentPositionsReplyReplyJson.value("plane3_y").toInt();
    int plane3_orient = acquireOpponentPositionsReplyReplyJson.value("plane3_orient").toInt(); //TODO to check this        
    bool setOk = m_MultiRound->setComputerPlanes(plane1_x, plane1_y, (Plane::Orientation)plane1_orient, plane2_x, plane2_y, (Plane::Orientation)plane2_orient, plane3_x, plane3_y, (Plane::Orientation)plane3_orient);
    qDebug() << "Plane 1 from opponent" << plane1_x << " " << plane1_y << " " << plane1_orient;
    qDebug() << "Plane 2 from opponent" << plane2_x << " " << plane2_y << " " << plane2_orient;
    qDebug() << "Plane 3 from opponent" << plane3_x << " " << plane3_y << " " << plane3_orient;

    if (!setOk) {
        QMessageBox msgBox;
        msgBox.setText("Planes positions from opponent are not valid"); 
        msgBox.exec();
        return;            
    }
    
}

void LeftPane::errorAcquireOpponentPositions(QNetworkReply::NetworkError code) {
    if (m_GameInfo->getSinglePlayer())
        return;

    CommunicationTools::treatCommunicationError("acquiring other player planes' positions ", m_DoneClickedReply);
}

void LeftPane::selectPlaneClickedSlot(bool c) {
    if (m_MultiRound->getRoundId() == 0 && !m_GameInfo->getSinglePlayer()) {
            QMessageBox msgBox;
            msgBox.setText("No round started. Connect to game or start a round!"); 
            msgBox.exec();
            return;
    }
    emit selectPlaneClicked(c);
}
    
void LeftPane::rotatePlaneClickedSlot(bool c) {
    if (m_MultiRound->getRoundId() == 0 && !m_GameInfo->getSinglePlayer()) {
        QMessageBox msgBox;
        msgBox.setText("No round started. Connect to game or start a round!"); 
        msgBox.exec();
        return;
    }
    emit rotatePlaneClicked(c);
}
    
void LeftPane::upPlaneClickedSlot(bool c) {
    if (m_MultiRound->getRoundId() == 0 && !m_GameInfo->getSinglePlayer()) {
        QMessageBox msgBox;
        msgBox.setText("No round started. Connect to game or start a round!"); 
        msgBox.exec();
        return;
    }
    emit upPlaneClicked(c);    
}
    
void LeftPane::downPlaneClickedSlot(bool c) {
   if (m_MultiRound->getRoundId() == 0 && !m_GameInfo->getSinglePlayer()) {
        QMessageBox msgBox;
        msgBox.setText("No round started. Connect to game or start a round!"); 
        msgBox.exec();
        return;
    }
    emit downPlaneClicked(c); 
}

void LeftPane::leftPlaneClickedSlot(bool c) {
   if (m_MultiRound->getRoundId() == 0 && !m_GameInfo->getSinglePlayer()) {
        QMessageBox msgBox;
        msgBox.setText("No round started. Connect to game or start a round!"); 
        msgBox.exec();
        return;
    }
    emit leftPlaneClicked(c); 
}

void LeftPane::rightPlaneClickedSlot(bool c) {
   if (m_MultiRound->getRoundId() == 0 && !m_GameInfo->getSinglePlayer()) {
        QMessageBox msgBox;
        msgBox.setText("No round started. Connect to game or start a round!"); 
        msgBox.exec();
        return;
    }
    emit rightPlaneClicked(c);     
}

void LeftPane::activateEditingBoard()
{
    activateEditorTab();
    ///activate the buttons in the editor tab
    m_selectPlaneButton->setEnabled(true);
    m_rotatePlaneButton->setEnabled(true);
    m_leftPlaneButton->setEnabled(true);
    m_rightPlaneButton->setEnabled(true);
    m_upPlaneButton->setEnabled(true);
    m_downPlaneButton->setEnabled(true);
    m_doneButton->setEnabled(true);
}

void LeftPane::updateGameStatistics(const GameStatistics &gs)
{
    m_PlayerStatsFrame->updateDisplayedValues(gs.m_playerMoves, gs.m_playerMisses, gs.m_playerHits, gs.m_playerDead);
    m_ComputerStatsFrame->updateDisplayedValues(gs.m_computerMoves, gs.m_computerMisses, gs.m_computerHits, gs.m_computerDead);
    m_ScoreFrame->updateDisplayedValues(gs.m_computerWins, gs.m_playerWins, gs.m_draws);
}

void LeftPane::endRound(bool) {
    activateStartGameTab();
}

void LeftPane::activateGameTab() {
    setCurrentIndex(m_GameTabIndex);
    setTabEnabled(m_EditorTabIndex, false);
    setTabEnabled(m_GameTabIndex, true);
    setTabEnabled(m_GameStartIndex, true);
	m_ScoreFrame->deactivateStartRoundButton();
}

void LeftPane::activateEditorTab() {
    setCurrentIndex(m_EditorTabIndex);
    setTabEnabled(m_EditorTabIndex, true);
    setTabEnabled(m_GameTabIndex, false);
    setTabEnabled(m_GameStartIndex, true);
	m_ScoreFrame->deactivateStartRoundButton();
}

void LeftPane::activateStartGameTab() {
    setCurrentIndex(m_GameStartIndex);
    setTabEnabled(m_EditorTabIndex, false);
    setTabEnabled(m_GameTabIndex, false);
    setTabEnabled(m_GameStartIndex, true);
	m_ScoreFrame->activateStartRoundButton();
}

void LeftPane::setMinWidth()
{
    ///decide the minimum size based on the texts in the labels
    QString textA("Plane upwards");
    QString textB("Plane downwards");
    QString textC("Plane left");
    QString textD("Plane right");

    QFontMetrics fm = fontMetrics();
    setMinimumWidth(((fm.width(textB) + fm.width(textC) + fm.width(textD)) * 170) / 100);
}

void LeftPane::setMinHeight()
{
    QFontMetrics fm = fontMetrics();
    setMinimumHeight(fm.height() * 12);
}

#include "leftpane.h"

#include <QLabel>
#include <QComboBox>
#include <QGridLayout>
#include <QPushButton>
#include <QSpacerItem>
#include "viewmodels/planespositionsviewmodel.h"
#include "viewmodels/getopponentplanespositionsviewmodel.h"
#include "communicationtools.h"
#include "global/globalgamedata.h"
#include "viewmodels/getopponentemovesviewmodel.h"
#include "viewmodels/cancelroundviewmodel.h"
#include "viewmodels/startnewroundviewmodel.h"

LeftPane::LeftPane(GameInfo* gameInfo, QNetworkAccessManager* networkManager, GlobalData* globalData, QSettings* settings, MultiplayerRound* mrd, QWidget *parent) 
    : QTabWidget(parent), m_GameInfo(gameInfo), m_NetworkManager(networkManager), m_GlobalData(globalData), m_Settings(settings), m_MultiRound(mrd)
{
    m_PlayRoundWidget = new PlayRoundWidget(m_GameInfo); 
    m_BoardEditingWidget = new BoardEditingWidget(m_GameInfo);
    m_StartNewRoundWidget = new StartNewRoundWidget(m_GameInfo);
    m_MainAccountWidget = new MainAccountWidget(m_Settings, m_GlobalData, m_NetworkManager, m_GameInfo, m_MultiRound);
    m_GameWidget = new GameWidget(m_GlobalData, m_MultiRound);
    m_UserProfileFrame = new UserProfileFrame(m_GlobalData, m_MultiRound);

    connect(m_MainAccountWidget, &MainAccountWidget::toGameCreationClicked, this, &LeftPane::activateGameWidget);
    connect(m_GameWidget, &GameWidget::toGameButtonClicked, this, &LeftPane::activateEditingBoard);
    
    connect(m_BoardEditingWidget, &BoardEditingWidget::selectPlaneClicked, this, &LeftPane::selectPlaneClickedSlot);
    connect(m_BoardEditingWidget, &BoardEditingWidget::rotatePlaneClicked, this, &LeftPane::rotatePlaneClickedSlot);
    connect(m_BoardEditingWidget, &BoardEditingWidget::doneClicked, this, &LeftPane::doneClickedSlot);
    connect(m_BoardEditingWidget, &BoardEditingWidget::upPlaneClicked, this, &LeftPane::upPlaneClickedSlot);
    connect(m_BoardEditingWidget, &BoardEditingWidget::downPlaneClicked, this, &LeftPane::downPlaneClickedSlot);
    connect(m_BoardEditingWidget, &BoardEditingWidget::leftPlaneClicked, this, &LeftPane::leftPlaneClickedSlot);
    connect(m_BoardEditingWidget, &BoardEditingWidget::rightPlaneClicked, this, &LeftPane::rightPlaneClickedSlot);
    connect(m_BoardEditingWidget, &BoardEditingWidget::acquireOpponentPositionsClicked, this, &LeftPane::acquireOpponentPositionsClickedSlot);
    connect(m_BoardEditingWidget, &BoardEditingWidget::cancelRoundClicked, this, &LeftPane::cancelRoundClickedSlot);

    connect(m_PlayRoundWidget, &PlayRoundWidget::acquireOpponentMovesClicked, this, &LeftPane::acquireOpponentMovesClickedSlot);
    connect(m_PlayRoundWidget, &PlayRoundWidget::cancelRoundClicked, this, &LeftPane::cancelRoundClickedSlot);

    connect(m_MultiRound, SIGNAL(roundWasCancelled()), this, SLOT(roundWasCancelledSlot()));
    connect(m_MultiRound, &MultiplayerRound::opponentPlanePositionsReceived, this, &LeftPane::activateGameTabDeactivateButtons);
    connect(m_MultiRound, &MultiplayerRound::waitForOpponentPlanePositions, this, &LeftPane::WaitForOpponentPlanesPositionsSlot);
    connect(m_MultiRound, &MultiplayerRound::newRoundStarted, this, &LeftPane::startNewRound);
    connect(m_MultiRound, &MultiplayerRound::winnerSent, this, &LeftPane::endRound);
    connect(m_MultiRound, &MultiplayerRound::gameStatsUpdated, this, &LeftPane::updateGameStatistics);    
    connect(m_MultiRound, &MultiplayerRound::logoutCompleted, this, &LeftPane::logoutCompleted);

    
    connect(m_MultiRound, &MultiplayerRound::allMovesSent, this, &LeftPane::acquireOpponentMovesSlot);
    
    connect(m_StartNewRoundWidget, &StartNewRoundWidget::startNewGame, this, &LeftPane::startNewGameSlot);
    connect(this, &LeftPane::currentChanged, this, &LeftPane::currentTabChangedSlot);

    if (!m_GameInfo->getSinglePlayer()) {
        m_MainAccountWidgetIndex = addTab(m_MainAccountWidget, "Login");
        m_GameWidgetIndex = addTab(m_GameWidget, "ConnectToGame");
    }
    m_GameTabIndex = addTab(m_PlayRoundWidget, "Round");
    m_EditorTabIndex = addTab(m_BoardEditingWidget, "BoardEditing");
    m_GameStartIndex = addTab(m_StartNewRoundWidget, "Start Round");
    m_UserProfileFrameIndex = addTab(m_UserProfileFrame, "User Info");

    if (!m_GameInfo->getSinglePlayer())
        activateAccountWidget();
    else
        activateEditingBoard();
    
    m_AcquireOpponentPlanesPositionsTimer = new QTimer(this);
    connect(m_AcquireOpponentPlanesPositionsTimer, &QTimer::timeout, this, &LeftPane::acquireOpponentPositionsTimeoutSlot);
    
    m_AcquireOpponentMovesTimer = new QTimer(this);
    connect(m_AcquireOpponentMovesTimer, &QTimer::timeout, this, &LeftPane::acquireOpponentMovesTimeoutSlot);
}

void LeftPane::activateDoneButton(bool planesOverlap)
{
    m_BoardEditingWidget->activateDoneButton(planesOverlap);
}

void LeftPane::activateGameTabDeactivateButtons()
{
    emit doneClicked();
    activateGameTab();
    m_BoardEditingWidget->activateGameTab();
}

void LeftPane::doneClickedSlot()
{
    if (m_MultiRound->getRoundId() == 0 && !m_GameInfo->getSinglePlayer()) {
            QMessageBox msgBox(this);
            msgBox.setText("No round started. Connect to game or start a round!"); 
            msgBox.exec();
            return;
    }
    
    if (!m_GameInfo->getSinglePlayer()) {
        //send configuration to server
        submitDoneClicked();
    }

    if (m_GameInfo->getSinglePlayer()) {
        activateGameTabDeactivateButtons();    
    }
}

void LeftPane::submitDoneClicked()
{
    m_MultiRound->sendPlanePositions();
}


void LeftPane::WaitForOpponentPlanesPositionsSlot() {
    m_BoardEditingWidget->waitForOpponentPlanesPositions();
    
    QMessageBox msgBox(this);
    msgBox.setText("Your opponent has not decided where he wants to place the planes yet\nPlease wait or try to acquire them by clicking \n on the \"Acquired opponent positions\" button! "); 
    msgBox.exec();    
    
    m_AcquireOpponentPlanesPositionsTimer->start(5000);
}

void LeftPane::acquireOpponentPositionsClickedSlot(bool c)
{
    m_MultiRound->acquireOpponentPlanePositions();
}

void LeftPane::acquireOpponentPositionsTimeoutSlot()
{
    acquireOpponentPositionsClickedSlot(false);
}

void LeftPane::acquireOpponentMovesTimeoutSlot()
{
    acquireOpponentMovesClickedSlot(false);
}


void LeftPane::acquireOpponentMovesClickedSlot(bool c)
{
    m_MultiRound->requestOpponentMoves();
}

void LeftPane::selectPlaneClickedSlot(bool c) {
    if (m_MultiRound->getRoundId() == 0 && !m_GameInfo->getSinglePlayer()) {
            QMessageBox msgBox(this);
            msgBox.setText("No round started. Connect to game or start a round!"); 
            msgBox.exec();
            return;
    }
    emit selectPlaneClicked(c);
}
    
void LeftPane::rotatePlaneClickedSlot(bool c) {
    if (m_MultiRound->getRoundId() == 0 && !m_GameInfo->getSinglePlayer()) {
        QMessageBox msgBox(this);
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
        QMessageBox msgBox(this);
        msgBox.setText("No round started. Connect to game or start a round!"); 
        msgBox.exec();
        return;
    }
    emit downPlaneClicked(c); 
}

void LeftPane::leftPlaneClickedSlot(bool c) {
   if (m_MultiRound->getRoundId() == 0 && !m_GameInfo->getSinglePlayer()) {
        QMessageBox msgBox(this);
        msgBox.setText("No round started. Connect to game or start a round!"); 
        msgBox.exec();
        return;
    }
    emit leftPlaneClicked(c); 
}

void LeftPane::rightPlaneClickedSlot(bool c) {
   if (m_MultiRound->getRoundId() == 0 && !m_GameInfo->getSinglePlayer()) {
        QMessageBox msgBox(this);
        msgBox.setText("No round started. Connect to game or start a round!"); 
        msgBox.exec();
        return;
    }
    emit rightPlaneClicked(c);     
}

void LeftPane::activateEditingBoard()
{
    activateEditorTab();
    m_BoardEditingWidget->activateEditingBoard();
}

void LeftPane::updateGameStatistics(const GameStatistics &gs)
{
    m_PlayRoundWidget->updateGameStatistics(gs);
    m_StartNewRoundWidget->updateDisplayedValues(gs);
}

void LeftPane::endRound(bool) {
    activateStartGameTab();
}

void LeftPane::activateGameTab() {
    if (!m_GameInfo->getSinglePlayer()) {
        setTabEnabled(m_MainAccountWidgetIndex, false);
        setTabEnabled(m_GameWidgetIndex, false);
    }
    setCurrentIndex(m_GameTabIndex);
    setTabEnabled(m_EditorTabIndex, false);
    setTabEnabled(m_GameTabIndex, true);
    setTabEnabled(m_GameStartIndex, true);
	m_StartNewRoundWidget->deactivateStartRoundButton();
}

void LeftPane::activateEditorTab() {
    if (!m_GameInfo->getSinglePlayer()) {
        setTabEnabled(m_MainAccountWidgetIndex, false);
        setTabEnabled(m_GameWidgetIndex, false);
    }
    setCurrentIndex(m_EditorTabIndex);
    setTabEnabled(m_EditorTabIndex, true);
    setTabEnabled(m_GameTabIndex, false);
    setTabEnabled(m_GameStartIndex, false);
	m_StartNewRoundWidget->deactivateStartRoundButton();
}

void LeftPane::activateStartGameTab() {
    if (!m_GameInfo->getSinglePlayer()) {
        setTabEnabled(m_MainAccountWidgetIndex, true);
        setTabEnabled(m_GameWidgetIndex, true);
    }
    setCurrentIndex(m_GameStartIndex);
    setTabEnabled(m_EditorTabIndex, false);
    setTabEnabled(m_GameTabIndex, false);
    setTabEnabled(m_GameStartIndex, true);
	m_StartNewRoundWidget->activateStartRoundButton();
}

void LeftPane::setMinWidth()
{
    ///decide the minimum size based on the texts in the labels
    QString textA("Plane upwards");
    QString textB("Plane downwards");
    QString textC("Plane left");
    QString textD("Plane right");

    QFontMetrics fm = fontMetrics();
    setMinimumWidth(((fm.boundingRect(textB).width() + fm.boundingRect(textC).width() + fm.boundingRect(textD).width()) * 170) / 100);
}

void LeftPane::setMinHeight()
{
    QFontMetrics fm = fontMetrics();
    setMinimumHeight(fm.height() * 12);
}

void LeftPane::roundWasCancelledSlot()
{
    activateStartGameTab();
}

void LeftPane::cancelRoundClickedSlot(bool b)
{
    if (!m_GameInfo->getSinglePlayer()) {
        m_MultiRound->cancelRound();
    } else {
        activateStartGameTab();
        emit roundWasCancelled();
    }
}

void LeftPane::startNewGameSlot()
{
    if (m_GameInfo->getSinglePlayer()) {
        activateEditingBoard();
        emit newRoundStarted();
        return;
    }
        
    m_MultiRound->startNewRound();
}

void LeftPane::startNewRound()
{
    activateEditingBoard();
    emit newRoundStarted();
}

void LeftPane::activateAccountWidget()
{
    setCurrentIndex(m_MainAccountWidgetIndex);
    setTabEnabled(m_GameWidgetIndex, false);
    setTabEnabled(m_EditorTabIndex, false);
    setTabEnabled(m_GameTabIndex, false);
    setTabEnabled(m_GameStartIndex, false);    
}

void LeftPane::activateGameWidget()
{
    setTabEnabled(m_MainAccountWidgetIndex, true);
    setCurrentIndex(m_GameWidgetIndex);
    setTabEnabled(m_GameWidgetIndex, true);
    setTabEnabled(m_EditorTabIndex, false);
    setTabEnabled(m_GameTabIndex, false);
    setTabEnabled(m_GameStartIndex, false);    
}

void LeftPane::currentTabChangedSlot()
{
    if (!m_GameInfo->getSinglePlayer()) {
        m_GameWidget->currentTabChanged();
    }
    
    if (m_AcquireOpponentPlanesPositionsTimer != nullptr)
        m_AcquireOpponentPlanesPositionsTimer->stop();
    
    if (m_AcquireOpponentMovesTimer != nullptr)
        m_AcquireOpponentMovesTimer->stop();
}

void LeftPane::acquireOpponentMovesSlot()
{
    m_AcquireOpponentMovesTimer->start(5000);
}
void LeftPane::logoutCompleted()
{
    if (m_AcquireOpponentPlanesPositionsTimer != nullptr)
        m_AcquireOpponentPlanesPositionsTimer->stop();

    if (m_AcquireOpponentMovesTimer != nullptr)
        m_AcquireOpponentMovesTimer->stop();

    m_GameWidget->stopRefreshStatusTimer();
}


#include "gamestatuswidget.h"

#include <QGridLayout>
#include <QPushButton>
#include <QDebug>
#include <QMessageBox>
#include <QTextCodec>
#include "viewmodels/gameviewmodel.h"
#include "communicationtools.h"

GameStatusWidget::GameStatusWidget(MultiplayerRound* mrd, QWidget* parent) 
    : QFrame(parent), m_MultiRound(mrd)
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
    connect(m_MultiRound, &MultiplayerRound::refreshStatus, this, &GameStatusWidget::gameConnectedToSlot);
    connect(m_MultiRound, &MultiplayerRound::loginCompleted, this, &GameStatusWidget::clearDataSlot);
    connect(m_MultiRound, &MultiplayerRound::loginFailed, this, &GameStatusWidget::clearDataSlot);
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
    QString gameName = m_GameName->text().trimmed();
    if (gameName.isEmpty()) {
        QMessageBox msgBox;
        msgBox.setText("Game name cannot be empty"); 
        msgBox.exec();

        return;
    }

    m_MultiRound->refreshGameStatus(gameName);    
}

void GameStatusWidget::clearDataSlot()
{
    m_GameName->setText("No game created");
    m_FirstPlayerName->clear();
    m_SecondPlayerName->clear();
    m_RoundName->setText("No round started");;
}

QString GameStatusWidget::getGameName() {
    return m_GameName->text().trimmed();
}


#include "gamestatuswidget.h"

#include <QGridLayout>
#include <QDebug>

GameStatusWidget::GameStatusWidget(QWidget* parent) : QFrame(parent)
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
    
    QLabel* roundNameLabel = new QLabel("Round Name");
    m_RoundName = new QLabel("No round started");
    
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

    setLayout(gridLayout);
    setFrameStyle(QFrame::Panel | QFrame::Raised);
}

void GameStatusWidget::gameCreatedSlot(const QString& gameName, const QString& username)
{
    m_GameName->setText(gameName);
    m_FirstPlayerName->setText(username);
}

void GameStatusWidget::gameConnectedToSlot(const QString& gameName, const QString& firstPlayerName, const QString& secondPlayerName) 
{
    qDebug() << gameName << " - " << firstPlayerName << "-" << secondPlayerName;
    m_GameName->setText(gameName);
    m_FirstPlayerName->setText(firstPlayerName);
    m_SecondPlayerName->setText(secondPlayerName);
}

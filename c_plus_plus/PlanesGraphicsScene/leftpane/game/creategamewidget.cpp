#include "creategamewidget.h"

#include <QPushButton>
#include <QGridLayout>
#include <QLabel>
#include <QTextCodec>
#include <QMessageBox>

#include "viewmodels/gameviewmodel.h"
#include "communicationtools.h"
#include "creategamewidget.h"

CreateGameWidget::CreateGameWidget(MultiplayerRound* mrd, QWidget* parent) : QFrame(parent), m_MultiRound(mrd)
{
    QString titleText = QString("<b> Create Game </b>");
    QLabel* titleLabel = new QLabel("");
    titleLabel->setText(titleText);
    
    QLabel* gameNameLabel = new QLabel("Game Name");
    m_GameName = new QLineEdit();
    
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

    QString gameName = m_GameName->text().trimmed();
    if (gameName.isEmpty()) {
        QMessageBox msgBox;
        msgBox.setText("Game name cannot be empty!"); 
        msgBox.exec();
        return;
    }
    m_MultiRound->createGame(m_GameName->text());
}

void CreateGameWidget::connectToGameSlot() {

    QString gameName = m_GameName->text().trimmed();
    if (gameName.isEmpty()) {
        QMessageBox msgBox;
        msgBox.setText("Game name cannot be empty!"); 
        msgBox.exec();
        return;
    }

    m_MultiRound->connectToGame(m_GameName->text()); 
}





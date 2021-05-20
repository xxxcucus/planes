#include "creategamewidget.h"

#include <QPushButton>
#include <QGridLayout>
#include <QTextCodec>
#include <QMessageBox>

#include "viewmodels/gameviewmodel.h"
#include "communicationtools.h"
#include "creategamewidget.h"

CreateGameWidget::CreateGameWidget(MultiplayerRound* mrd, QWidget* parent) : QFrame(parent), m_MultiRound(mrd)
{
    QString titleText = QString("<b> Game </b>");
    QLabel* titleLabel = new QLabel("");
    titleLabel->setText(titleText);
    titleLabel->setFont(QFont("Timer", 20, QFont::Bold));
    titleLabel->setAlignment(Qt::AlignCenter);
    
    QLabel* gameNameLabel = new QLabel("Game Name");
    m_GameName = new QLineEdit();
    
    QPushButton* submitButton = new QPushButton("Submit");
        
    QGridLayout* gridLayout = new QGridLayout();
    gridLayout->addWidget(gameNameLabel, 1, 0);
    gridLayout->addWidget(m_GameName, 2, 0, 1, 2);
    gridLayout->addWidget(submitButton, 3, 1, 1, 1);    
    
    QHBoxLayout* hLayout1 = new QHBoxLayout();
    QSpacerItem* spacer3 = new QSpacerItem(20, 20, QSizePolicy::Expanding, QSizePolicy::Fixed);
    QSpacerItem* spacer4 = new QSpacerItem(20, 20, QSizePolicy::Expanding, QSizePolicy::Fixed);
    
    hLayout1->addItem(spacer3);
    hLayout1->addLayout(gridLayout);
    hLayout1->addItem(spacer4);

    setLayout(hLayout1);
    setFrameStyle(QFrame::NoFrame);
    
    connect(submitButton, &QPushButton::clicked, this, &CreateGameWidget::submitButtonClickedSlot);
    connect(m_MultiRound, &MultiplayerRound::refreshStatus, this, &CreateGameWidget::choiceToCreateGameSlot);
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
    
    emit connectToGameCalled();

    QString gameName = m_GameName->text().trimmed();
    if (gameName.isEmpty()) {
        QMessageBox msgBox;
        msgBox.setText("Game name cannot be empty!"); 
        msgBox.exec();
        return;
    }

    m_MultiRound->connectToGame(m_GameName->text()); 
}


void CreateGameWidget::submitButtonClickedSlot()
{
    m_SubmitCalled = true;
    m_MultiRound->refreshGameStatus(m_GameName->text().trimmed());
}


QString CreateGameWidget::getGameName()
{
    return m_GameName->text().trimmed();
}


void CreateGameWidget::choiceToCreateGameSlot(bool exists, const QString& gameName, const QString& firstPlayerName, const QString& secondPlayerName, const QString& currentRoundId) {
    
    if (!m_SubmitCalled)
        return;
    m_SubmitCalled = false;
    if (!exists) {
        QMessageBox msgBox;
        msgBox.setText("You may create a new game with this name"); 
        QPushButton* createButton = msgBox.addButton("Create New Game", QMessageBox::YesRole);
        QPushButton* cancelButton = msgBox.addButton("Cancel", QMessageBox::NoRole);
        msgBox.exec();
        if (msgBox.clickedButton() == createButton)
            createGameSlot();
    } else if (firstPlayerName == secondPlayerName) {
        QMessageBox msgBox;
        msgBox.setText("You may connect to the game created by " + firstPlayerName); 
        QPushButton* connectButton = msgBox.addButton("Connect to Game", QMessageBox::YesRole);
        QPushButton* cancelButton = msgBox.addButton("Cancel", QMessageBox::NoRole);
        msgBox.exec();
        if (msgBox.clickedButton() == connectButton)
            connectToGameSlot();        
    } else {
        QMessageBox msgBox;
        msgBox.setText("It is not possible to create or connect to a a game with this name"); 
        msgBox.exec();
    }
}

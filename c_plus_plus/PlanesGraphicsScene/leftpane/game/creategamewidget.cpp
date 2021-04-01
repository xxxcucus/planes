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
    QString titleText = QString("<b> Create Game </b>");
    QLabel* titleLabel = new QLabel("");
    titleLabel->setText(titleText);
    
    QLabel* gameNameLabel = new QLabel("Game Name");
    m_GameName = new QLineEdit();
    
    QPushButton* submitButton = new QPushButton("Perform action");
    
    QLabel* chooseActionLabel = new QLabel("Choose action");
    m_ActionDescriptionLabel = new QTextEdit(m_CreateGameHelp);
    m_ActionDescriptionLabel->setReadOnly(true);
    
    m_ChooseActionComboBox = new QComboBox();
	m_ChooseActionComboBox->addItem("Create Game");
	m_ChooseActionComboBox->addItem("Connect to Game");
	m_ChooseActionComboBox->setCurrentIndex(0);
    
    QGridLayout* gridLayout = new QGridLayout();
    gridLayout->addWidget(titleLabel, 0, 0, 1, 2);
    gridLayout->addWidget(gameNameLabel, 1, 0);
    gridLayout->addWidget(m_GameName, 1, 1);
    gridLayout->addWidget(chooseActionLabel, 2, 0, 1, 1);
    gridLayout->addWidget(m_ChooseActionComboBox, 2, 1, 1, 1);
    gridLayout->addWidget(m_ActionDescriptionLabel, 3, 0, 2, 2);
    gridLayout->addWidget(submitButton, 5, 1, 1, 1);
    setLayout(gridLayout);
    setFrameStyle(QFrame::Panel | QFrame::Raised);
    
    connect(submitButton, &QPushButton::clicked, this, &CreateGameWidget::submitButtonClickedSlot);
    connect(m_ChooseActionComboBox, QOverload<int>::of(&QComboBox::currentIndexChanged), this, &CreateGameWidget::actionChangedSlot);
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


void CreateGameWidget::submitButtonClickedSlot()
{
    switch(m_ChooseActionComboBox->currentIndex()) {
        case 0:
            createGameSlot();
            break;
        case 1:
            connectToGameSlot();
            break;
        
    }
}


void CreateGameWidget::actionChangedSlot()
{
    switch(m_ChooseActionComboBox->currentIndex()) {
        case 0:
            m_ActionDescriptionLabel->setText(m_CreateGameHelp);
            break;
        case 1:
            m_ActionDescriptionLabel->setText(m_ConnectToGameHelp);
            break;
    }
}

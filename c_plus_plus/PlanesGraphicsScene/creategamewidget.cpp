#include "creategamewidget.h"

#include <QPushButton>
#include <QGridLayout>
#include <QLabel>

CreateGameWidget::CreateGameWidget(QWidget* parent) : QFrame(parent)
{
    QString titleText = QString("<b> Create Game </b>");
    QLabel* titleLabel = new QLabel("");
    titleLabel->setText(titleText);
    
    QLabel* gameNameLabel = new QLabel("Game Name");
    m_GameName = new QLineEdit();  //TODO: to add validation
    
    QPushButton* createGameButton = new QPushButton("Create Game");
    QPushButton* connectToGameButton = new QPushButton("Connect to Game");
    QPushButton* createAndConnectToGameButton = new QPushButton("Create and Connect to Game");
    
    QGridLayout* gridLayout = new QGridLayout();
    gridLayout->addWidget(titleLabel, 0, 0, 1, 2);
    gridLayout->addWidget(gameNameLabel, 1, 0);
    gridLayout->addWidget(m_GameName, 1, 1);
    gridLayout->addWidget(createGameButton, 2, 0, 1, 2);
    gridLayout->addWidget(connectToGameButton, 3, 0, 1, 2);
    gridLayout->addWidget(createAndConnectToGameButton, 4, 0, 1, 2);

    setLayout(gridLayout);
    setFrameStyle(QFrame::Panel | QFrame::Raised);
}

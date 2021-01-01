#include "gameendwidget.h"

#include <QPushButton>
#include <QVBoxLayout>

GameEndWidget::GameEndWidget(QWidget* parent)
{
    QString titleText = QString("<b> Disconnect from Game or Round </b>");
    QLabel* titleLabel = new QLabel("");
    titleLabel->setText(titleText);
    
    QPushButton* disconnectFromGameButton = new QPushButton("Disconnect from Game");
    QPushButton* disconnectFromRound = new QPushButton("Disconnect from Round");
    
    QVBoxLayout* verticalLayout = new QVBoxLayout();
    verticalLayout->addWidget(titleLabel);
    verticalLayout->addWidget(disconnectFromGameButton);
    verticalLayout->addWidget(disconnectFromRound);

    setLayout(verticalLayout);
    setFrameStyle(QFrame::Panel | QFrame::Raised);

}

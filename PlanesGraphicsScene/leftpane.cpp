#include "leftpane.h"

#include <QLabel>
#include <QComboBox>
#include <QGridLayout>
#include <QPushButton>

LeftPane::LeftPane(QWidget *parent) : QTabWidget(parent)
{
    m_PlayerStatsFrame = new GameStatsFrame("Player");
    m_ComputerStatsFrame = new GameStatsFrame("Computer");
    QVBoxLayout* vLayout = new QVBoxLayout();
    vLayout->addWidget(m_PlayerStatsFrame);
    vLayout->addWidget(m_ComputerStatsFrame);
    vLayout->addStretch(5);
    m_GameWidget = new QWidget();
    m_GameWidget->setLayout(vLayout);

    QWidget* editBoardWidget = new QWidget();
    QPushButton* addPlaneButton = new QPushButton("Add plane");
    QPushButton* movePlaneButton = new QPushButton("Move plane");
    QPushButton* selectPlaneButton = new QPushButton("Select plane");
    QPushButton* rotatePlaneButton = new QPushButton("Rotate plane");
    QPushButton* deletePlaneButton = new QPushButton("Delete plane");
    QPushButton* cancelButton = new QPushButton("Cancel");
    QPushButton* doneButton = new QPushButton("Done editing");
    QVBoxLayout* vLayout1 = new QVBoxLayout();
    vLayout1->addWidget(addPlaneButton);
    vLayout1->addWidget(movePlaneButton);
    vLayout1->addWidget(rotatePlaneButton);
    vLayout1->addWidget(selectPlaneButton);
    vLayout1->addWidget(deletePlaneButton);
    vLayout1->addWidget(cancelButton);
    vLayout1->addWidget(doneButton);
    vLayout1->addStretch(5);
    editBoardWidget->setLayout(vLayout1);

    addTab(m_GameWidget, "Game");
    addTab(editBoardWidget, "BoardEditing");
}

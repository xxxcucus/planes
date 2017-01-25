#include "leftpane.h"

#include <QLabel>
#include <QComboBox>
#include <QGridLayout>
#include <QPushButton>
#include <QSpacerItem>

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
    QPushButton* selectPlaneButton = new QPushButton("Select plane");
    QPushButton* rotatePlaneButton = new QPushButton("Rotate plane");
    QPushButton* leftPlaneButton = new QPushButton("Plane to left");
    QPushButton* rightPlaneButton = new QPushButton("Plane to right");
    QPushButton* upPlaneButton = new QPushButton("Plane upwards");
    QPushButton* downPlaneButton = new QPushButton("Plane downwards");
    QPushButton* doneButton = new QPushButton("Done editing");
    QSpacerItem* spacer = new QSpacerItem(50, 50, QSizePolicy::Expanding, QSizePolicy::Expanding);
    QGridLayout* gridLayout = new QGridLayout();
    gridLayout->addWidget(selectPlaneButton, 0, 0, 1, 3);
    gridLayout->addWidget(rotatePlaneButton, 1, 0, 1, 3);
    gridLayout->addWidget(upPlaneButton, 2, 1);
    gridLayout->addWidget(leftPlaneButton, 3, 0);
    gridLayout->addWidget(rightPlaneButton, 3, 2);
    gridLayout->addWidget(downPlaneButton, 4, 1);
    gridLayout->addWidget(doneButton, 5, 0, 1, 3);
    gridLayout->addItem(spacer, 6, 0, 1, 3);
    gridLayout->setRowStretch(6, 5);
    editBoardWidget->setLayout(gridLayout);

    m_GameTabIndex = addTab(m_GameWidget, "Game");
    m_EditorTabIndex = addTab(editBoardWidget, "BoardEditing");
}

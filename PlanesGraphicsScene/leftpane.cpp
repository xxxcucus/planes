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

    m_BoardEditingWidget = new QWidget();
    m_selectPlaneButton = new QPushButton("Select plane");
    m_rotatePlaneButton = new QPushButton("Rotate plane");
    m_leftPlaneButton = new QPushButton("Plane to left");
    m_rightPlaneButton = new QPushButton("Plane to right");
    m_upPlaneButton = new QPushButton("Plane upwards");
    m_downPlaneButton = new QPushButton("Plane downwards");
    m_doneButton = new QPushButton("Done editing");
    QSpacerItem* spacer = new QSpacerItem(50, 50, QSizePolicy::Expanding, QSizePolicy::Expanding);
    QGridLayout* gridLayout = new QGridLayout();
    gridLayout->addWidget(m_selectPlaneButton, 0, 0, 1, 3);
    gridLayout->addWidget(m_rotatePlaneButton, 1, 0, 1, 3);
    gridLayout->addWidget(m_upPlaneButton, 2, 1);
    gridLayout->addWidget(m_leftPlaneButton, 3, 0);
    gridLayout->addWidget(m_rightPlaneButton, 3, 2);
    gridLayout->addWidget(m_downPlaneButton, 4, 1);
    gridLayout->addWidget(m_doneButton, 5, 0, 1, 3);
    gridLayout->addItem(spacer, 6, 0, 1, 3);
    gridLayout->setRowStretch(6, 5);
    m_BoardEditingWidget->setLayout(gridLayout);

    connect(m_selectPlaneButton, SIGNAL(clicked(bool)), this, SIGNAL(selectPlaneClicked(bool)));
    connect(m_rotatePlaneButton, SIGNAL(clicked(bool)), this, SIGNAL(rotatePlaneClicked(bool)));
    connect(m_doneButton, SIGNAL(clicked(bool)), this, SIGNAL(doneClicked(bool)));
    connect(m_doneButton, SIGNAL(clicked(bool)), this, SLOT(doneClickedSlot()));
    connect(m_upPlaneButton, SIGNAL(clicked(bool)), this, SIGNAL(upPlaneClicked(bool)));
    connect(m_downPlaneButton, SIGNAL(clicked(bool)), this, SIGNAL(downPlaneClicked(bool)));
    connect(m_leftPlaneButton, SIGNAL(clicked(bool)), this, SIGNAL(leftPlaneClicked(bool)));
    connect(m_rightPlaneButton, SIGNAL(clicked(bool)), this, SIGNAL(rightPlaneClicked(bool)));

    m_GameTabIndex = addTab(m_GameWidget, "Game");
    m_EditorTabIndex = addTab(m_BoardEditingWidget, "BoardEditing");
    activateEditorTab();
}

void LeftPane::activateDoneButton(bool planesOverlap)
{
    m_doneButton->setEnabled(!planesOverlap);
}

void LeftPane::doneClickedSlot()
{
    activateGameTab();
    m_selectPlaneButton->setEnabled(false);
    m_rotatePlaneButton->setEnabled(false);
    m_leftPlaneButton->setEnabled(false);
    m_rightPlaneButton->setEnabled(false);
    m_upPlaneButton->setEnabled(false);
    m_downPlaneButton->setEnabled(false);
    m_doneButton->setEnabled(false);
}

void LeftPane::activateEditingBoard()
{
    activateEditorTab();
    ///activate the buttons in the editor tab
    m_selectPlaneButton->setEnabled(true);
    m_rotatePlaneButton->setEnabled(true);
    m_leftPlaneButton->setEnabled(true);
    m_rightPlaneButton->setEnabled(true);
    m_upPlaneButton->setEnabled(true);
    m_downPlaneButton->setEnabled(true);
    m_doneButton->setEnabled(true);
}

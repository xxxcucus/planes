#include "boardeditingwidget.h"

#include <QSpacerItem>
#include <QGridLayout>

BoardEditingWidget::BoardEditingWidget(GameInfo* gameInfo, QWidget *parent) 
    : QWidget(parent), m_GameInfo(gameInfo) {
        
    m_selectPlaneButton = new QPushButton("Select plane");
    m_rotatePlaneButton = new QPushButton("Rotate plane");
    m_leftPlaneButton = new QPushButton("Plane to left");
    m_rightPlaneButton = new QPushButton("Plane to right");
    m_upPlaneButton = new QPushButton("Plane upwards");
    m_downPlaneButton = new QPushButton("Plane downwards");
    m_doneButton = new QPushButton("Done editing");
    m_acquireOpponentPositionsButton = new QPushButton("Acquire opponent planes positions");
    m_CancelRoundButton = new QPushButton("Cancel Round");
    QSpacerItem* spacer = new QSpacerItem(50, 50, QSizePolicy::Expanding, QSizePolicy::Expanding);
    QGridLayout* gridLayout = new QGridLayout();
    gridLayout->addWidget(m_selectPlaneButton, 0, 0, 1, 3);
    gridLayout->addWidget(m_rotatePlaneButton, 1, 0, 1, 3);
    gridLayout->addWidget(m_upPlaneButton, 2, 1);
    gridLayout->addWidget(m_leftPlaneButton, 3, 0);
    gridLayout->addWidget(m_rightPlaneButton, 3, 2);
    gridLayout->addWidget(m_downPlaneButton, 4, 1);
    gridLayout->addWidget(m_doneButton, 5, 0, 1, 3);
    gridLayout->addWidget(m_CancelRoundButton, 6, 0, 1, 3);
    if (!m_GameInfo->getSinglePlayer())
        gridLayout->addWidget(m_acquireOpponentPositionsButton, 7, 0, 1, 3);
    //gridLayout->addItem(spacer, 6, 0, 1, 3);
    gridLayout->setRowStretch(6, 5);
    setLayout(gridLayout);

    
    connect(m_selectPlaneButton, &QPushButton::clicked, this, &BoardEditingWidget::selectPlaneClicked);
    connect(m_rotatePlaneButton, &QPushButton::clicked, this, &BoardEditingWidget::rotatePlaneClicked);
    //connect(m_doneButton, SIGNAL(clicked(bool)), this, SIGNAL(doneClicked()));
    connect(m_doneButton, &QPushButton::clicked, this, &BoardEditingWidget::doneClicked);
    connect(m_upPlaneButton, &QPushButton::clicked, this, &BoardEditingWidget::upPlaneClicked);
    connect(m_downPlaneButton, &QPushButton::clicked, this, &BoardEditingWidget::downPlaneClicked);
    connect(m_leftPlaneButton, &QPushButton::clicked, this, &BoardEditingWidget::leftPlaneClicked);
    connect(m_rightPlaneButton, &QPushButton::clicked, this, &BoardEditingWidget::rightPlaneClicked);
    connect(m_acquireOpponentPositionsButton, &QPushButton::clicked, this, &BoardEditingWidget::acquireOpponentPositionsClicked);        
}


void BoardEditingWidget::waitForOpponentPlanesPositions()
{
    m_selectPlaneButton->setEnabled(false);
    m_rotatePlaneButton->setEnabled(false);
    m_leftPlaneButton->setEnabled(false);
    m_rightPlaneButton->setEnabled(false);
    m_upPlaneButton->setEnabled(false);
    m_downPlaneButton->setEnabled(false);
    m_doneButton->setEnabled(false);
    m_acquireOpponentPositionsButton->setEnabled(true);
}

void BoardEditingWidget::activateEditingBoard()
{
     ///activate the buttons in the editor tab
    m_selectPlaneButton->setEnabled(true);
    m_rotatePlaneButton->setEnabled(true);
    m_leftPlaneButton->setEnabled(true);
    m_rightPlaneButton->setEnabled(true);
    m_upPlaneButton->setEnabled(true);
    m_downPlaneButton->setEnabled(true);
    m_doneButton->setEnabled(true);
}

void BoardEditingWidget::activateDoneButton(bool planesOverlap)
{
    m_doneButton->setEnabled(!planesOverlap);
}

void BoardEditingWidget::activateGameTab()
{
    m_selectPlaneButton->setEnabled(false);
    m_rotatePlaneButton->setEnabled(false);
    m_leftPlaneButton->setEnabled(false);
    m_rightPlaneButton->setEnabled(false);
    m_upPlaneButton->setEnabled(false);
    m_downPlaneButton->setEnabled(false);
    m_doneButton->setEnabled(false);
}


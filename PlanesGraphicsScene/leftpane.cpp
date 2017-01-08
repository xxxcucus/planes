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
    m_GameWidget = new QWidget();
    m_GameWidget->setLayout(vLayout);

    QWidget* editBoardWidget = new QWidget();
    QPushButton* addPlaneButton = new QPushButton("Add plane");
    QPushButton* movePlaneButton = new QPushButton("Move plane");
    QPushButton* rotatePlaneButton = new QPushButton("Rotate plane");
    QPushButton* deletePlaneButton = new QPushButton("Delete plane");
    QPushButton* cancelButton = new QPushButton("Cancel");
    QPushButton* doneButton = new QPushButton("Done editing");
    QGridLayout* gridLayout = new QGridLayout();
    gridLayout->addWidget(addPlaneButton, 0, 0);
    gridLayout->addWidget(movePlaneButton, 1, 0);
    gridLayout->addWidget(rotatePlaneButton, 2, 0);
    gridLayout->addWidget(deletePlaneButton, 3, 0);
    gridLayout->addWidget(cancelButton, 4, 0);
    gridLayout->addWidget(doneButton, 5, 0);
    editBoardWidget->setLayout(gridLayout);

    addTab(m_GameWidget, "Game");
    addTab(editBoardWidget, "BoardEditing");
}

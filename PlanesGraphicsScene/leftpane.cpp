#include "leftpane.h"

#include <QLabel>
#include <QComboBox>
#include <QGridLayout>

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
    addTab(m_GameWidget, "Game");
    addTab(editBoardWidget, "BoardEditing");
}

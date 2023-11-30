#include "chatwidget.h"

#include <QHBoxLayout>

ChatWidget::ChatWidget(GlobalData* globalData, MultiplayerRound* multiround, QWidget* parent) {
    m_PlayersListWidget = new PlayersListWidget(m_GlobalData, m_MultiRound);
    m_ChatStackedWidget = new QStackedWidget();

    QHBoxLayout* hLayout = new QHBoxLayout();
    hLayout->addWidget(m_PlayersListWidget);
    hLayout->addWidget(m_ChatStackedWidget);
    setLayout(hLayout);
}

void ChatWidget::setActive(bool active) {
    m_PlayersListWidget->setActive(active);
}

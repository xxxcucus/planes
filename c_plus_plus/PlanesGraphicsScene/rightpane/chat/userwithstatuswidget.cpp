#include "userwithstatuswidget.h"

#include <QHBoxLayout>
#include <QLabel>
#include <QFont>
#include <QFontMetrics>

UserWithStatusWidget::UserWithStatusWidget(const QString& name, bool online, int width, QWidget* parent) :
    m_Name(name), m_Online(online), QWidget(parent) {
    QHBoxLayout* hLayout = new QHBoxLayout();
    QLabel* nameLabel = new QLabel();
    nameLabel->setText(m_Name);
    QLabel* onlineLabel = new QLabel();
    onlineLabel->setText(m_Online ? "Online" : "Offline");
    hLayout->addWidget(nameLabel);
    hLayout->addStretch(4);
    hLayout->addWidget(onlineLabel);
    //hLayout->setSizeConstraint(QLayout::SetFixedSize);
    setLayout(hLayout);
    QFont font("times", 24);
    QFontMetrics fm(font);
    int pixelsHigh = fm.height();
    setFixedHeight(pixelsHigh * 110 / 100);
    setFixedWidth(width);

}

QString UserWithStatusWidget::getName() const {
    return m_Name;
}

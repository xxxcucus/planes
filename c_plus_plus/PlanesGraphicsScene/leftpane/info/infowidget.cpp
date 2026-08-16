#include "infowidget.h"

#include <QHBoxLayout>

InfoWidget::InfoWidget(QWidget* parent): QWidget(parent)
{
    m_TextEdit = new QTextEdit();

    QHBoxLayout* layout = new QHBoxLayout();
    layout->addWidget(m_TextEdit);
    setLayout(layout);
}

void InfoWidget::addMessage(const QString& msg)
{
    m_TextEdit->append(msg);
}

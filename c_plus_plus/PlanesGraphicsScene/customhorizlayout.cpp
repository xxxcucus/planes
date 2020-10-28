#include "customhorizlayout.h"

int CustomHorizLayout::count() const
{
    return m_ItemsList.size();
}

QLayoutItem* CustomHorizLayout::itemAt(int idx) const
{
    // QList::value() performs index checking, and returns 0 if we are
    // outside the valid range
    return m_ItemsList.value(idx);
}

QLayoutItem* CustomHorizLayout::takeAt(int idx)
{
    // QList::take does not do index checking
    return idx >= 0 && idx < m_ItemsList.size() ? m_ItemsList.takeAt(idx) : 0;
}

void CustomHorizLayout::addItem(QLayoutItem* item)
{
    if (count() < 2)
        m_ItemsList.append(item);
}

CustomHorizLayout::~CustomHorizLayout()
{
     QLayoutItem *item;
     while ((item = takeAt(0)))
         delete item;
}

void CustomHorizLayout::setGeometry(const QRect &r)
{
    QLayout::setGeometry(r);

    ///only two widgets can lie in this layout
    if (count() != 2)
        return;

    QList<int> scalHoriz;

    scalHoriz.append(m_LeftWidgetSize);
    scalHoriz.append(100 - m_LeftWidgetSize);

    int w = r.width() - (count() + 1) * spacing();
    int i = 0;
    int curX = 0;
    while (i < count()) {
        QLayoutItem *o = m_ItemsList.at(i);
        int wtemp = (w * scalHoriz[i]) / 100;
        int htemp = r.height();
        if (i == 0) {
            wtemp = std::max(wtemp, o->minimumSize().width());
            htemp = std::max(r.height() / 2, o->minimumSize().height());
        } else {
            wtemp = std::max(r.width() - curX - spacing(), o->minimumSize().width());
        }
        QRect geom(r.x() + curX + (i + 1) * spacing(), r.y(), wtemp, htemp);
        o->setGeometry(geom);
        ++i;
        curX += wtemp;
    }
}

QSize CustomHorizLayout::sizeHint() const
{
    QSize s(0,0);
    int n = m_ItemsList.count();
    if (n > 0)
        s = QSize(1000, 600); //start with a nice default size
    int i = 0;
    while (i < n) {
        QLayoutItem *o = m_ItemsList.at(i);
        s = s.expandedTo(o->sizeHint());
        ++i;
    }
    return s + n*QSize(spacing(), spacing());
}

QSize CustomHorizLayout::minimumSize() const
{
    QSize s(0,0);
    int n = m_ItemsList.count();
    int i = 0;
    while (i < n) {
        QLayoutItem *o = m_ItemsList.at(i);
        s = s.expandedTo(o->minimumSize());
        ++i;
    }
    return s + n*QSize(spacing(), spacing());
}

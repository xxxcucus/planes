#ifndef CUSTOMHORIZLAYOUT_H
#define CUSTOMHORIZLAYOUT_H

#include <QList>
#include <QLayout>

class CustomHorizLayout : public QLayout
{
public:
    CustomHorizLayout(int leftSize, QWidget *parent = nullptr): QLayout(parent), m_LeftWidgetSize(leftSize) {}
    ~CustomHorizLayout();

    QSize sizeHint() const;
    QSize minimumSize() const;

    void addItem(QLayoutItem *item);
    int count() const;
    QLayoutItem *itemAt(int) const;
    QLayoutItem *takeAt(int);
    void setGeometry(const QRect &rect);

private:
    QList<QLayoutItem*> m_ItemsList;
    int m_LeftWidgetSize = 0;
};

#endif // CUSTOMHORIZLAYOUT_H

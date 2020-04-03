#ifndef CUSTOMHORIZLAYOUT_H
#define CUSTOMHORIZLAYOUT_H

#include <QList>
#include <QLayout>

class CustomHorizLayout : public QLayout
{
public:
    CustomHorizLayout(QWidget *parent): QLayout(parent) {}
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
};

#endif // CUSTOMHORIZLAYOUT_H

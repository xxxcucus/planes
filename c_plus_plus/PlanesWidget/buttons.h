#ifndef BUTTONS_H
#define BUTTONS_H

#include <QAbstractButton>

//these are the next and previous buttons in the page displaying the computer strategy

class NextMoveButton: public QAbstractButton
{
protected:
    int m_originalWidth;
    int m_originalHeight;

    int m_width;
    int m_height;

public:
    NextMoveButton(QWidget* parent=0);

    virtual QSize sizeHint() const;
protected:
    virtual void paintEvent(QPaintEvent* e);
};

class PreviousMoveButton: public NextMoveButton
{
public:
    PreviousMoveButton(QWidget* parent=0);

protected:
    virtual void paintEvent(QPaintEvent* e);
};


#endif // BUTTONS_H

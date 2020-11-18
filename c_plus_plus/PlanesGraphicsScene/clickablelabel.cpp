#include "clickablelabel.h"

ClickableLabel::ClickableLabel(QWidget* parent, Qt::WindowFlags f)
    : QLabel(parent) {
    
}

ClickableLabel::~ClickableLabel() {}

void ClickableLabel::mousePressEvent(QMouseEvent* event) {
    emit clicked();
}

void ClickableLabel::setSelected(bool selected)
{
    if (selected)
        setStyleSheet("#"+objectName()+" { border: 5px solid red; }");
    else
        setStyleSheet("#"+objectName()+" { border: 0px; }");
}


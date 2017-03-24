#ifndef ANIMATEDTEXTITEM_H
#define ANIMATEDTEXTITEM_H

#include <QObject>
#include <QGraphicsTextItem>
#include <QDebug>

/**
 * @brief The AnimatedTextItem class displays a moving text in one of the used QGraphicsViews
 */

class AnimatedTextItem : public QGraphicsTextItem
{
    Q_OBJECT
    Q_PROPERTY(QPoint m_ScenePosition READ position WRITE setPosition)

public:
    AnimatedTextItem(const QString& text) : QGraphicsTextItem(text), m_DisplayedText(text) {}

    inline QPoint position() { return m_ScenePosition; }
    inline void setPosition(QPoint position) {
        setPos(position.x(), position.y());
        m_ScenePosition = position;
        qDebug() << "set position " << position.x() << " " << position.y();
    }


private:
    QString m_DisplayedText;
    ///position in the scene
    QPoint m_ScenePosition;
};

#endif // ANIMATEDTEXTITEM_H

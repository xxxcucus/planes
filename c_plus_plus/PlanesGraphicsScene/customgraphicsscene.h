#ifndef CUSTOMGRAPHICSSCENE_H
#define CUSTOMGRAPHICSSCENE_H

#include <QGraphicsScene>
#include <QGraphicsSceneMouseEvent>

class CustomGraphicsScene : public QGraphicsScene
{
    Q_OBJECT
public:
    CustomGraphicsScene(QObject* parent = nullptr) : QGraphicsScene(parent) {}
    void mousePressEvent(QGraphicsSceneMouseEvent *mouseEvent);

signals:
    void gridSquareClicked(int, int);
};

#endif // CUSTOMGRAPHICSSCENE_H

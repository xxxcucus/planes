#include "customgraphicsscene.h"

#include "gridsquare.h"

void CustomGraphicsScene::mousePressEvent(QGraphicsSceneMouseEvent *mouseEvent)
{
    QGraphicsScene::mousePressEvent(mouseEvent); //Call the ancestor
    QGraphicsItem*  item = itemAt(mouseEvent->scenePos(), QTransform()); //Get the item at the position
    if (item) //If there is an item at that position
    {
        GridSquare* gs = dynamic_cast<GridSquare*>(item);
        if (gs) {
            //qDebug() << "Row : " << gs->getRow();
            //qDebug() << "Col : " << gs->getCol();
            emit gridSquareClicked(gs->getRow(), gs->getCol());
        }
    }
}

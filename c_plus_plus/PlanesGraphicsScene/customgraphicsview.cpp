#include "customgraphicsview.h"

void CustomGraphicsView::resizeEvent(QResizeEvent* event)
{
    fitInView(0, 0, m_SceneSizeX, m_SceneSizeY, Qt::KeepAspectRatio);
    QGraphicsView::resizeEvent(event);
}

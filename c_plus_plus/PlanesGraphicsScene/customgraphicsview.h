#ifndef CUSTOMGRAPHICSVIEW_H
#define CUSTOMGRAPHICSVIEW_H

#include <QGraphicsView>

class CustomGraphicsView : public QGraphicsView
{
public:
    CustomGraphicsView(QGraphicsScene* scene, QWidget* parent = nullptr) : QGraphicsView(scene, parent) {}

    void resizeEvent(QResizeEvent* event) override;
    inline void setSceneSize(int sizeX, int sizeY) {
        m_SceneSizeX = sizeX;
        m_SceneSizeY = sizeY;
    }

private:
    int m_SceneSizeX = -1;
    int m_SceneSizeY = -1;
};

#endif // CUSTOMGRAPHICSVIEW_H

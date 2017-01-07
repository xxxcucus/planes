#ifndef RIGHTPANE_H
#define RIGHTPANE_H

#include <QTabWidget>

class QGraphicsScene;
class QGraphicsView;

class RightPane : public QTabWidget
{
    Q_OBJECT
public:
    explicit RightPane(QWidget* parent = nullptr);

private:
    QGraphicsScene* m_Scene;
    QGraphicsView* m_View;

private:
    void defineHelpWindow(QWidget* w);
};

#endif // RIGHTPANE_H

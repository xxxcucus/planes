#ifndef PLANESGSLEFTPANE_H
#define PLANESGSLEFTPANE_H

#include <QTabWidget>
#include "gamestatsframe.h"

class LeftPane : public QTabWidget
{
    Q_OBJECT
public:
    explicit LeftPane(QWidget *parent = 0);

signals:

public slots:

private:
    GameStatsFrame* m_PlayerStatsFrame;
    GameStatsFrame* m_ComputerStatsFrame;

    QWidget* m_GameWidget;
    QWidget* m_BoardEditingWidget;
};

#endif // PLANESGSLEFTPANE_H

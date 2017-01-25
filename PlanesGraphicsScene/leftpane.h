#ifndef PLANESGSLEFTPANE_H
#define PLANESGSLEFTPANE_H

#include <QDebug>
#include <QTabWidget>
#include "gamestatsframe.h"

class LeftPane : public QTabWidget
{
    Q_OBJECT
public:
    explicit LeftPane(QWidget *parent = 0);

    inline void activateGameTab() { setCurrentIndex(m_GameTabIndex);  }
    inline void activateEditorTab() { setCurrentIndex(m_EditorTabIndex); }

signals:

public slots:

private:
    GameStatsFrame* m_PlayerStatsFrame;
    GameStatsFrame* m_ComputerStatsFrame;

    QWidget* m_GameWidget;
    QWidget* m_BoardEditingWidget;

    int m_GameTabIndex = -1;
    int m_EditorTabIndex = -1;
};

#endif // PLANESGSLEFTPANE_H

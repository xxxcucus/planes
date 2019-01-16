#ifndef EDITORWINDOW_H
#define EDITORWINDOW_H

#include <QWidget>
#include "gamerenderarea.h"

class QPushButton;
class QLabel;
class QGridLayout;
class QSpinBox;
class QHBoxLayout;
class QVBoxLayout;


class EditorWindow: public QWidget
{
    Q_OBJECT

public:
    EditorWindow(GameRenderArea *renderArea, QWidget *parent=0);
    GameRenderArea* getRenderArea() {return m_RenderArea; }

private:
    int m_score;

    GameRenderArea *m_RenderArea;
    QGridLayout *formLayout;
    QLabel *titleLabel;
    QLabel *scoreLabel;
    QSpinBox *scoreSpinBox;
    QHBoxLayout *titleLayout;
    QVBoxLayout *chessLayout;
    QLabel *spacerLabel;

public slots:
    void updateWins();

};

#endif // EDITORWINDOW_H

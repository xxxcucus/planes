#include "editorwindow.h"

#include <QSpinBox>
#include <QLabel>
#include <QGridLayout>
#include <QVBoxLayout>
#include <QHBoxLayout>

EditorWindow::EditorWindow(GameRenderArea *renderArea, QWidget *parent):
    QWidget(parent),
    m_score(0)
{
    m_RenderArea = renderArea;
    titleLabel = new QLabel();
    scoreLabel = new QLabel(tr("Score"));
    scoreSpinBox = new QSpinBox();
    scoreSpinBox->setValue(0);
    scoreSpinBox->setReadOnly(true);
    spacerLabel = new QLabel();

    if(m_RenderArea->isComputer())
        titleLabel->setText(tr("Computer"));
    else
        titleLabel->setText(tr("Player"));

    formLayout = new QGridLayout;
    titleLayout = new QHBoxLayout();
    chessLayout = new QVBoxLayout();

    titleLayout->addStretch(3);
    titleLayout->addWidget(titleLabel);
    titleLayout->addWidget(scoreLabel);
    titleLayout->addWidget(scoreSpinBox);
    titleLayout->addStretch(5);
    chessLayout->addLayout(titleLayout);
    chessLayout->addWidget(renderArea);
    //formLayout->addWidget(spacerLabel,0,0,1,3);
    formLayout->addLayout(chessLayout,0,0,1,3);


    setLayout(formLayout);


}

void EditorWindow::updateWins()
{
    m_score++;
    scoreSpinBox->setValue(m_score);
    update();
}

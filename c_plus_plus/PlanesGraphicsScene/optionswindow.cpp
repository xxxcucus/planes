#include "optionswindow.h"
#include "gamedifficultyoptions.h"

#include <QVBoxLayout>
#include <QHBoxLayout>

OptionsWindow::OptionsWindow(QWidget* parent) : QWidget(parent) {

	QHBoxLayout* hlayout = new QHBoxLayout();
	GameDifficultyOptions* gameDifficulty = new GameDifficultyOptions();
	hlayout->addWidget(gameDifficulty);
	hlayout->addStretch(5);

	QVBoxLayout* vlayout = new QVBoxLayout();
	vlayout->addLayout(hlayout);
	vlayout->addStretch(5);

	setLayout(vlayout);
}

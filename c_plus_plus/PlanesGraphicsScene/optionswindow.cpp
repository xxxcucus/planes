#include "optionswindow.h"
#include "gamedifficultyoptions.h"

#include <QVBoxLayout>
#include <QHBoxLayout>


OptionsWindow::OptionsWindow(PlaneRound* pr,  QSettings* settings, GameInfo* gameInfo, QWidget* parent) : QWidget(parent), m_PlaneRound(pr), m_Settings(settings), m_GameInfo(gameInfo) {

	QHBoxLayout* hlayout = new QHBoxLayout();
	GameDifficultyOptions* gameDifficulty = new GameDifficultyOptions(m_PlaneRound, m_Settings);
	hlayout->addWidget(gameDifficulty);
	hlayout->addStretch(5);

	QVBoxLayout* vlayout = new QVBoxLayout();
	vlayout->addLayout(hlayout);
	vlayout->addStretch(5);

	setLayout(vlayout);
}

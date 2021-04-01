#include "optionswindow.h"
#include "gamedifficultyoptions.h"
#include "serversettingsoptions.h"

#include <QVBoxLayout>
#include <QHBoxLayout>


OptionsWindow::OptionsWindow(PlaneRound* pr,  QSettings* settings, GameInfo* gameInfo, QWidget* parent) : QWidget(parent), m_PlaneRound(pr), m_Settings(settings), m_GameInfo(gameInfo) {

	QHBoxLayout* hlayout1 = new QHBoxLayout();
	GameDifficultyOptions* gameDifficulty = new GameDifficultyOptions(m_PlaneRound, m_Settings);
	hlayout1->addWidget(gameDifficulty);
	hlayout1->addStretch(5);

    QHBoxLayout* hlayout2 = new QHBoxLayout();
    ServerSettingsOptions* servesettings = new ServerSettingsOptions(m_Settings);
    hlayout2->addWidget(servesettings);
    hlayout2->addStretch(5);
    
	QVBoxLayout* vlayout = new QVBoxLayout();
	vlayout->addLayout(hlayout1);
    vlayout->addLayout(hlayout2);
	vlayout->addStretch(5);

	setLayout(vlayout);
}

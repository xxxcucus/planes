#ifndef __GAME_DIFFICULTY_OPTIONS_
#define __GAME_DIFFICULTY_OPTIONS_

#include <QFrame>
#include <QComboBox>
#include <QCheckBox>
#include <QSettings>
#include "planeround.h"

class GameDifficultyOptions : public QFrame {
	Q_OBJECT
public:
	GameDifficultyOptions(PlaneRound* pr, QSettings* settings, QWidget* parent = nullptr);

private slots:
	void setComputerSkill(int idx);
	void setShowAfterKill(int state);

private:
	PlaneRound* m_PlaneRound;
	int m_CurrentSkill = 0;
	bool m_CurrentShowPlane = false;
	QComboBox* m_ComputerSkillComboBox;
	QCheckBox* m_ShowPlaneAfterKillCheckBox;
	QSettings* m_Settings;
};







#endif
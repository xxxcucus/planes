#include "gamedifficultyoptions.h"

#include <QLabel>
#include <QComboBox>
#include <QCheckBox>
#include <QGridLayout>

GameDifficultyOptions::GameDifficultyOptions(QWidget* parent) : QFrame(parent) {
	QString titleText = QString("<b> Game Difficulty</b>");
	QLabel* titleLabel = new QLabel();
	titleLabel->setText(titleText);
	QLabel* computerSkillTextLabel = new QLabel("Computer Skill: ");
	QComboBox* computerSkillComboBox = new QComboBox();
	computerSkillComboBox->addItem("Easy");
	computerSkillComboBox->addItem("Medium");
	computerSkillComboBox->addItem("Difficult");
	QCheckBox* showPlaneAfterKillCheckBox = new QCheckBox("Show Plane After Kill");

	QGridLayout* gridLayout1 = new QGridLayout();
	gridLayout1->addWidget(titleLabel);
	gridLayout1->addWidget(computerSkillTextLabel, 1, 0);
	gridLayout1->addWidget(computerSkillComboBox, 1, 1);
	gridLayout1->addWidget(showPlaneAfterKillCheckBox, 2, 0, 1, 2);
	setLayout(gridLayout1);
	setFrameStyle(QFrame::Panel | QFrame::Raised);
	setSizePolicy(QSizePolicy::Fixed, QSizePolicy::Fixed);

}


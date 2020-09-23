#include "gamedifficultyoptions.h"

#include <QLabel>
#include <QGridLayout>
#include <QMessageBox>
#include <QDebug>

GameDifficultyOptions::GameDifficultyOptions(PlaneRound* pr, QSettings* settings, QWidget* parent) : QFrame(parent), m_PlaneRound(pr), m_Settings(settings) {
	m_CurrentSkill = m_Settings->value("gamedifficulty/computerskill").toInt();
	m_CurrentShowPlane = m_Settings->value("gamedifficulty/showkilledplane").toBool();
	//qDebug() << "Show plane from settings " << m_Settings->value("gamedifficulty/showkilledplane") << endl;
	
	QString titleText = QString("<b> Game Difficulty</b>");
	QLabel* titleLabel = new QLabel();
	titleLabel->setText(titleText);
	QLabel* computerSkillTextLabel = new QLabel("Computer Skill: ");
	m_ComputerSkillComboBox = new QComboBox();
	m_ComputerSkillComboBox->addItem("Easy");
	m_ComputerSkillComboBox->addItem("Medium");
	m_ComputerSkillComboBox->addItem("Difficult");
	m_ComputerSkillComboBox->setCurrentIndex(m_CurrentSkill);
	setComputerSkill(m_CurrentSkill);
	m_ShowPlaneAfterKillCheckBox = new QCheckBox("Show Plane After Kill");
	m_ShowPlaneAfterKillCheckBox->setChecked(m_CurrentShowPlane);
	setShowAfterKill(m_CurrentShowPlane ? Qt::Checked : Qt::Unchecked);

	QGridLayout* gridLayout1 = new QGridLayout();
	gridLayout1->addWidget(titleLabel);
	gridLayout1->addWidget(computerSkillTextLabel, 1, 0);
	gridLayout1->addWidget(m_ComputerSkillComboBox, 1, 1);
	gridLayout1->addWidget(m_ShowPlaneAfterKillCheckBox, 2, 0, 1, 2);
	setLayout(gridLayout1);
	setFrameStyle(QFrame::Panel | QFrame::Raised);
	setSizePolicy(QSizePolicy::Fixed, QSizePolicy::Fixed);

	connect(m_ComputerSkillComboBox, SIGNAL(currentIndexChanged(int)), this, SLOT(setComputerSkill(int)));
	connect(m_ShowPlaneAfterKillCheckBox, SIGNAL(stateChanged(int)), this, SLOT(setShowAfterKill(int)));
}

void GameDifficultyOptions::setComputerSkill(int idx) {
	if (m_PlaneRound->setComputerSkill(idx)) {
		m_CurrentSkill = idx;
		m_Settings->setValue("gamedifficulty/computerskill", idx);
	} else {
		QMessageBox msgBox;
		msgBox.setText("Cannot set option during the game.");
		msgBox.exec();
		m_ComputerSkillComboBox->setCurrentIndex(m_CurrentSkill);
	}
}

void GameDifficultyOptions::setShowAfterKill(int state) {

	bool value = false;
	switch (state) {
		case Qt::Unchecked:
			value = false;
			break;
		case Qt::Checked:
			value = true;
			break;
	};

	if (m_PlaneRound->setShowPlaneAfterKill(value)) {
		m_CurrentShowPlane = value;
		//m_Settings->setValue("gamedifficulty/showkilledplane", value);
		qDebug() << "Set show after kill " << value << endl;
	} else {
		QMessageBox msgBox;
		msgBox.setText("Cannot set option during the game.");
		msgBox.exec();
		m_ShowPlaneAfterKillCheckBox->setChecked(value);
	}
}
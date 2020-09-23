#ifndef _OPTIONSWINDOW__
#define _OPTIONSWINDOW__
#include <QWidget>
#include <QSettings>
#include "planeround.h"

class OptionsWindow : public QWidget
{
	Q_OBJECT
public:
	OptionsWindow(PlaneRound* pr, QSettings* settings, QWidget* parent = nullptr);

private:
	PlaneRound* m_PlaneRound;
	QSettings* m_Settings;
};

#endif


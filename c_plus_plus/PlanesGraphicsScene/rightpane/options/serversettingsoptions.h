#ifndef __SERVER_SETTINGS_OPTIONS__
#define __SERVER_SETTINGS_OPTIONS__

#include <QFrame>
#include <QLineEdit>
#include <QSettings>
#include "multiplayerround.h"

class ServerSettingsOptions : public QFrame {
	Q_OBJECT
public:
	ServerSettingsOptions(QSettings* settings, QWidget* parent = nullptr);

private slots:
	void setPathToServer();

private:
	QSettings* m_Settings;
    QLineEdit* m_PathToServerLineEdit;
};















#endif

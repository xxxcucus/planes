#ifndef __ACCOUNT_WIDGET__
#define __ACCOUNT_WIDGET__

#include <QStackedWidget>
#include <QNetworkAccessManager>
#include <QSettings>
#include "mainaccountwidget.h"
#include "norobotwidget.h"
#include "userdata.h"


class AccountWidget : public QStackedWidget {
    Q_OBJECT
    
public:
    AccountWidget(QSettings* settings, UserData* userData, QNetworkAccessManager* networkManager, QWidget* parent = nullptr);


private:
    MainAccountWidget* m_MainAccountWidget;
    NoRobotWidget* m_NoRobotWidget;
    UserData* m_UserData;
    QNetworkAccessManager* m_NetworkManager;
    QSettings* m_Settings;
};

#endif

#ifndef __ACCOUNT_WIDGET__
#define __ACCOUNT_WIDGET__

#include <QStackedWidget>
#include "mainaccountwidget.h"
#include "norobotwidget.h"
#include "userdata.h"


class AccountWidget : public QStackedWidget {
    Q_OBJECT
    
public:
    AccountWidget(UserData* userData, QWidget* parent = nullptr);


private:
    MainAccountWidget* m_MainAccountWidget;
    NoRobotWidget* m_NoRobotWidget;
    UserData* m_UserData;
};

#endif

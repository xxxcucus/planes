#ifndef __ACCOUNT_WIDGET__
#define __ACCOUNT_WIDGET__

#include <QStackedWidget>
#include "mainaccountwidget.h"
#include "norobotwidget.h"


class AccountWidget : public QStackedWidget {
    Q_OBJECT
    
public:
    AccountWidget(QWidget* parent = nullptr);


private:
    MainAccountWidget* m_MainAccountWidget;
    NoRobotWidget* m_NoRobotWidget;
    
};

#endif

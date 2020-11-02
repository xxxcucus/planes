#ifndef _MAIN_ACCOUNT_WIDGET__
#define _MAIN_ACCOUNT_WIDGET__

#include <QWidget>
#include "userdata.h"

class MainAccountWidget : public QWidget {
    Q_OBJECT
    
public:    
    MainAccountWidget(UserData* userData, QWidget* parent = nullptr);

private:
    UserData* m_UserData;
    
};


#endif

#ifndef _MAIN_ACCOUNT_WIDGET__
#define _MAIN_ACCOUNT_WIDGET__

#include <QWidget>
#include <QNetworkAccessManager>
#include "userdata.h"

class MainAccountWidget : public QWidget {
    Q_OBJECT
    
public:    
    MainAccountWidget(UserData* userData, QNetworkAccessManager* m_NetworkManager, QWidget* parent = nullptr);

private:
    UserData* m_UserData;
    QNetworkAccessManager* m_NetworkManager;
    
};


#endif

#ifndef __ACCOUNT_WIDGET__
#define __ACCOUNT_WIDGET__

#include <QWidget>

/***
 * Main Widget for managing the user account:
 * It includes a user profile frame on the left 
 * and a stacked widget on the right.
 * The stacked widget includes a register / login form
 * and a no robot proof system
 * ***/

class AccountWidget : public QWidget {
    
public:
    AccountWidget(QWidget* parent = nullptr);


};

#endif

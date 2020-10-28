#include "accountwidget.h"
#include <QFrame>
#include <QHBoxLayout>
#include "customhorizlayout.h"
#include "userprofileframe.h"
#include "loginregisterstackedwidget.h"

AccountWidget::AccountWidget(QWidget* parent) : QWidget(parent)
{
    CustomHorizLayout* cLayout = new CustomHorizLayout(50, this);
    UserProfileFrame* userProfileFrame = new UserProfileFrame();
    LoginRegisterStackedWidget* loginStackedWidget = new LoginRegisterStackedWidget();
    cLayout->addWidget(userProfileFrame);
    cLayout->addWidget(loginStackedWidget);
    setLayout(cLayout);
}

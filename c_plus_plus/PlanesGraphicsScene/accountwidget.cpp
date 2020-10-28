#include "accountwidget.h"
#include <QFrame>
#include <QHBoxLayout>
#include "customhorizlayout.h"
#include "userprofileframe.h"
#include "loginregisterstackedwidget.h"

AccountWidget::AccountWidget(QWidget* parent) : QWidget(parent)
{
    CustomHorizLayout* cLayout = new CustomHorizLayout(50, this);
    
    QWidget* leftPane = new QWidget();
    QVBoxLayout* vLayout = new QVBoxLayout();
    UserProfileFrame* userProfileFrame = new UserProfileFrame();
    vLayout->addWidget(userProfileFrame);
    QSpacerItem* spacer = new QSpacerItem(50, 50, QSizePolicy::Expanding, QSizePolicy::Expanding);
    vLayout->addItem(spacer);
    leftPane->setLayout(vLayout);
    LoginRegisterStackedWidget* loginStackedWidget = new LoginRegisterStackedWidget();
    cLayout->addWidget(leftPane);
    cLayout->addWidget(loginStackedWidget);
    setLayout(cLayout);
}

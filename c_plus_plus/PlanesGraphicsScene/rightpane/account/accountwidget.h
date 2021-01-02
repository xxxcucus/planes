#ifndef __ACCOUNT_WIDGET__
#define __ACCOUNT_WIDGET__

#include <QStackedWidget>
#include <QNetworkAccessManager>
#include <QSettings>
#include <QJsonObject>
#include <vector>
#include "mainaccountwidget.h"
#include "norobotwidget.h"
#include "global/globaldata.h"
#include "gameinfo.h"


class AccountWidget : public QStackedWidget {
    Q_OBJECT
    
public:
    AccountWidget(QSettings* settings, GlobalData* globalData, QNetworkAccessManager* networkManager, GameInfo* gameInfo, QWidget* parent = nullptr);

    
private slots:
    void noRobotRegistrationSlot(const std::vector<QString>& images, const QJsonObject& registrationReplyJson);
    void registrationComplete();
    
private:
    MainAccountWidget* m_MainAccountWidget;
    NoRobotWidget* m_NoRobotWidget;
    GlobalData* m_GlobalData;
    QNetworkAccessManager* m_NetworkManager;
    QSettings* m_Settings;
    GameInfo* m_GameInfo;
};

#endif

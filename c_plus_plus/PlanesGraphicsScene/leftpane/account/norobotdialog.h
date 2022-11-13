#ifndef _NO_ROBOT_DIALOG__
#define _NO_ROBOT_DIALOG__

#include <QDialog>
#include <QCloseEvent>
#include "norobotwidget.h"

class NoRobotDialog: public QDialog {
    Q_OBJECT

public:
    NoRobotDialog(QNetworkAccessManager* networkManager, QSettings* settings, GlobalData* globalData, GameInfo* gameInfo, MultiplayerRound* mrd, QWidget* parent = nullptr);
    
    void setImages(const std::vector<QString>& images);
    void setQuestion(const QString& category);
    void setRequestId(const QString& id);

private slots:
    void closeDialog();

private:
    void closeEvent(QCloseEvent* event) override;

private:
    NoRobotWidget* m_NoRobotWidget = nullptr;
    QNetworkAccessManager* m_NetworkManager;
    QSettings* m_Settings;
    GlobalData* m_GlobalData;
    GameInfo* m_GameInfo;
    MultiplayerRound* m_MultiRound;
};

#endif

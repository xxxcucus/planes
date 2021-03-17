#ifndef _NO_ROBOT_WIDGET__
#define _NO_ROBOT_WIDGET__

#include <QWidget>
#include <QLabel>
#include <QPushButton>
#include <QNetworkAccessManager>
#include <QSettings>
#include <QNetworkReply>
#include "clickablelabel.h"
#include "global/globaldata.h"
#include "gameinfo.h"
#include "multiplayerround.h"

class NoRobotWidget: public QWidget {
    Q_OBJECT

public:
    NoRobotWidget(QNetworkAccessManager* networkManager, QSettings* settings, GlobalData* globalData, GameInfo* gameInfo, MultiplayerRound* mrd, QWidget* parent = nullptr);
    
    void setImages(const std::vector<QString>& images);
    void setQuestion(const QString& category);
    void setRequestId(const QString& id);
    
    void resizeEvent(QResizeEvent * event) override;
    
private slots:
    void imageClicked(int imageIndex);
    void submitAnswer();    
    
private:
    void displayAndScaleImages();
    
signals:
    void noRobotSubmit();
    
private:
    std::vector<ClickableLabel*> m_Labels;
    QLabel* m_QuestionLabel;
    QPushButton* m_SubmitButton;
    QString m_RequestId;
    std::vector<bool> m_Answer;
    int m_ImagesCount = 9;
    std::map<QString, QString> m_PhotosMap;
    std::vector<QString> m_Images;    

    bool m_ResizedOnce = false;
    
    QNetworkAccessManager* m_NetworkManager;
    QSettings* m_Settings;
    GlobalData* m_GlobalData;
    GameInfo* m_GameInfo;
    MultiplayerRound* m_MultiRound;
};

#endif

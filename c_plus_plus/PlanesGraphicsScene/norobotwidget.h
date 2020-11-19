#ifndef _NO_ROBOT_WIDGET__
#define _NO_ROBOT_WIDGET__

#include <QWidget>
#include <QLabel>
#include <QPushButton>
#include <QNetworkAccessManager>
#include <QSettings>
#include <QNetworkReply>
#include "clickablelabel.h"
#include "userdata.h"

class NoRobotWidget: public QWidget {
    Q_OBJECT

public:
    NoRobotWidget(QNetworkAccessManager* networkManager, QSettings* settings, UserData* userData, QWidget* parent = nullptr);
    
    void setImages(const std::vector<QString>& images);
    void setQuestion(const QString& category);
    void setRequestId(const QString& id);
    
    void resizeEvent(QResizeEvent * event) override;
    
private slots:
    void imageClicked(int imageIndex);
    void submitAnswer();    
    void errorRegister(QNetworkReply::NetworkError code);
    void finishedRegister();

signals:
    void registrationComplete();
    
private:
    void displayAndScaleImages();
    
private:
    std::vector<ClickableLabel*> m_Labels;
    QLabel* m_QuestionLabel;
    QPushButton* m_SubmitButton;
    QString m_RequestId;
    std::vector<bool> m_Answer;
    int m_ImagesCount = 9;
    std::map<QString, QString> m_PhotosMap;
    std::vector<QString> m_Images;    
    
    QNetworkAccessManager* m_NetworkManager;
    QSettings* m_Settings;
    UserData* m_UserData;
    
    QNetworkReply* m_RegistrationReply = nullptr;
};

#endif

#include "norobotwidget.h"

#include <QDebug>
#include <QGridLayout>
#include <QDir>
#include <QFileInfoList>
#include <QMessageBox>
#include <QTextCodec>
#include "norobotdata.h"
#include "communicationtools.h"

NoRobotWidget::NoRobotWidget(QNetworkAccessManager* networkManager, QSettings* settings, UserData* userData, QWidget* parent) 
    : QWidget(parent), m_NetworkManager(networkManager), m_Settings(settings), m_UserData(userData) {
    QGridLayout* gridLayout = new QGridLayout();
    
    m_Labels = std::vector<ClickableLabel*>(m_ImagesCount);
    m_Answer = std::vector<bool>(m_ImagesCount, false);

    
    for (int i = 0; i < m_ImagesCount; i++) {
        m_Labels[i] = new ClickableLabel();
        m_Labels[i]->setFrameShape(QFrame::NoFrame);
        m_Labels[i]->setLineWidth(5);
        connect(m_Labels[i], &ClickableLabel::clicked, [this, i] { imageClicked(i); });
        gridLayout->addWidget(m_Labels[i], i / 3, i % 3);
    }

    m_QuestionLabel = new QLabel();
    m_SubmitButton = new QPushButton("Submit");
    connect(m_SubmitButton, &QPushButton::clicked, this, &NoRobotWidget::submitAnswer);
    
    QVBoxLayout* vLayout = new QVBoxLayout(); 
    vLayout->addLayout(gridLayout);
    vLayout->addWidget(m_QuestionLabel);
    vLayout->addWidget(m_SubmitButton);
    
    setLayout(vLayout);
    
    qDebug() << "Current dir " << QDir::currentPath();
    QDir dog_dir = QDir::current();
    dog_dir.cd("dog_photos_scaled");
    qDebug() << "Dogs " << dog_dir.absolutePath();
    QDir cat_dir = QDir::current();
    cat_dir.cd("cat_photos_scaled");
    qDebug() << "Cats " << cat_dir.absolutePath();
    
    QStringList dog_folders = dog_dir.entryList(QDir::Dirs);
    QStringList cat_folders = cat_dir.entryList(QDir::Dirs);
    
    for (auto f : dog_folders) {
        QDir f_folder = dog_dir;
        f_folder.cd(f);
        QFileInfoList files = f_folder.entryInfoList(QDir::Files);
        if (files.size() != 1) {
            qDebug() << "Error folder " << f <<  " " << files.size();
            continue;
        }
        m_PhotosMap[QDir(f).dirName()] = files[0].absoluteFilePath();
    }

    for (auto f : cat_folders) {
        QDir f_folder = cat_dir;
        f_folder.cd(f);
        QFileInfoList files = f_folder.entryInfoList(QDir::Files);
        if (files.size() != 1) {
            qDebug() << "Error folder " << f << " " << files.size();
            continue;
        }
        m_PhotosMap[QDir(f).dirName()] = files[0].absoluteFilePath();
    }
    
    for (auto photo : m_PhotosMap) {
        qDebug() << photo.first << " " << photo.second;
    }
    
}

void NoRobotWidget::setImages(const std::vector<QString>& images)
{
    if (images.size() != m_ImagesCount) {
        qDebug() << "Images number does not match";
        return;
    }

    for (int i = 0; i < m_ImagesCount; i++)
        m_Answer[i] = false;
    
    for (int i = 0; i < m_ImagesCount; i++) {
        QString path = m_PhotosMap[images[i]];
        qDebug() << "Working with path " << path;
        QPixmap pix(m_PhotosMap[images[i]]);
        pix = pix.scaledToWidth(100); //TODO: to optimize scaling
        if (m_Answer[i])
            m_Labels[i]->setFrameShape(QFrame::Box);
        else
            m_Labels[i]->setFrameShape(QFrame::NoFrame);        
        m_Labels[i]->setPixmap(pix);
    }
}

void NoRobotWidget::setQuestion(const QString& category)
{
    m_QuestionLabel->setText("Please select images corresponding to category " + category);
}

void NoRobotWidget::imageClicked(int imageIndex)
{
    m_Answer[imageIndex] = !m_Answer[imageIndex];
    if (m_Answer[imageIndex])
        m_Labels[imageIndex]->setFrameShape(QFrame::Box);
    else
        m_Labels[imageIndex]->setFrameShape(QFrame::NoFrame);
    update();
    
    qDebug() << "Image clicked " << imageIndex;
}

void NoRobotWidget::submitAnswer()
{
    NoRobotData requestData;
    requestData.m_requestId = m_RequestId;
    
    QString answer;
    for (int i = 0; i < m_ImagesCount; i++) {
        answer += m_Answer[i] ? "1" : "0";
    }
    requestData.m_answer = answer;
    
    qDebug() << "Request id " << requestData.m_requestId;
    qDebug() << "Answer " << requestData.m_answer;

    if (m_RegistrationReply != nullptr)
        delete m_RegistrationReply;

    m_RegistrationReply = CommunicationTools::buildPostRequest("/users/registration_confirm", m_Settings->value("multiplayer/serverpath").toString(), requestData.toJson(), m_NetworkManager);

    connect(m_RegistrationReply, &QNetworkReply::finished, this, &NoRobotWidget::finishedRegister);
    connect(m_RegistrationReply, &QNetworkReply::errorOccurred, this, &NoRobotWidget::errorRegister);
}

void NoRobotWidget::setRequestId(const QString& id)
{
    m_RequestId = id;
}

void NoRobotWidget::errorRegister(QNetworkReply::NetworkError code)
{
    QMessageBox msgBox;
    msgBox.setText("Error when performing robot validation " + QString::number(code)); //TODO: show error string
    msgBox.exec();
    //TODO: switch to login window
}

void NoRobotWidget::finishedRegister()
{
    QByteArray reply = m_RegistrationReply->readAll();
    QString registrationReplyQString = QTextCodec::codecForMib(106)->toUnicode(reply);
    QJsonObject registrationReplyJson = CommunicationTools::objectFromString(registrationReplyQString);
        
    QMessageBox msgBox;
    msgBox.setText("User created " + registrationReplyJson.value("username").toString()); //TODO: show error string
    msgBox.exec();

    m_UserData->m_UserName = registrationReplyJson.value("username").toString(); //TODO: save token
    emit registrationComplete();
}

#include "norobotwidget.h"

#include <QDebug>
#include <QGridLayout>
#include <QDir>
#include <QFileInfoList>
#include <QMessageBox>
#include <QTextCodec>
#include "viewmodels/norobotviewmodel.h"
#include "communicationtools.h"

NoRobotWidget::NoRobotWidget(QNetworkAccessManager* networkManager, QSettings* settings, GlobalData* globalData, GameInfo* gameInfo, MultiplayerRound* mrd, QWidget* parent) 
    : QWidget(parent), m_NetworkManager(networkManager), m_Settings(settings), m_GlobalData(globalData), m_GameInfo(gameInfo), m_MultiRound(mrd) {
    QGridLayout* gridLayout = new QGridLayout();
    
    m_Labels = std::vector<ClickableLabel*>(m_ImagesCount);
    m_Answer = std::vector<bool>(m_ImagesCount, false);
    m_Images = std::vector<QString>();

    
    for (int i = 0; i < m_ImagesCount; i++) {
        m_Labels[i] = new ClickableLabel();
        m_Labels[i]->setFrameShape(QFrame::NoFrame);
        QString objectName = "clabel"+QString::number(i);
        m_Labels[i]->setObjectName(objectName);
        
        connect(m_Labels[i], &ClickableLabel::clicked, [this, i] { imageClicked(i); });
        gridLayout->addWidget(m_Labels[i], i / 3, i % 3, Qt::AlignCenter);
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
    dog_dir.cd("img");
    dog_dir.cd("dog_photos_scaled");
    qDebug() << "Dogs " << dog_dir.absolutePath();
    QDir cat_dir = QDir::current();
    cat_dir.cd("img");
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
    
    m_Images = images;
    displayAndScaleImages();
}

void NoRobotWidget::displayAndScaleImages()
{
    qDebug() << m_Images.size();
    if (m_Images.size() != m_ImagesCount)
        return;
    
    for (int i = 0; i < m_ImagesCount; i++) {
        QString path = m_PhotosMap[m_Images[i]];
        qDebug() << "Working with path " << path;
        QPixmap pix(m_PhotosMap[m_Images[i]]);
        
        int availWidth = width() / 3;
        int availHeight = height() * 5 / 6 / 3;
        
        if (pix.width() > pix.height()) {
            pix = pix.scaledToWidth(availWidth);
        } else {
            pix = pix.scaledToHeight(availHeight);
        }
        
        //pix = pix.scaledToWidth(100); //TODO: to optimize scaling
        if (m_Answer[i]) {
            m_Labels[i]->setSelected(true);
        } else {
            m_Labels[i]->setSelected(false);
        }
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
        m_Labels[imageIndex]->setSelected(true);
    else
        m_Labels[imageIndex]->setSelected(false);
    update();
    
    qDebug() << "Image clicked " << imageIndex;
}

void NoRobotWidget::submitAnswer()
{
    QString answer;
    for (int i = 0; i < m_ImagesCount; i++) {
        answer += m_Answer[i] ? "1" : "0";
    }
    m_MultiRound->noRobotRegister(m_RequestId, answer);
}

void NoRobotWidget::setRequestId(const QString& id)
{
    m_RequestId = id;
}

void NoRobotWidget::resizeEvent(QResizeEvent* event)
{
    displayAndScaleImages();    
    QWidget::resizeEvent(event);
}
    


#include "norobotwidget.h"

#include <QDebug>
#include <QGridLayout>
#include <QDir>
#include <QFileInfoList>
#include <QMessageBox>
#include <QTextCodec>
#include <QResizeEvent>
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
    
    //qDebug() << "Current dir " << QDir::currentPath();    
}

void NoRobotWidget::setImages(const std::vector<QString>& images)
{
    if (images.size() != m_ImagesCount) {
        //qDebug() << "Images number does not match";
        return;
    }

    for (int i = 0; i < m_ImagesCount; i++)
        m_Answer[i] = false;
    
    m_Images = images;
    displayAndScaleImages();
}

void NoRobotWidget::displayAndScaleImages()
{
    //qDebug() << m_Images.size();
    if (m_Images.size() != m_ImagesCount)
        return;
    
    for (int i = 0; i < m_ImagesCount; i++) {
        QString path = m_PhotosMap[m_Images[i]];
        //qDebug() << "Working with path " << path;
        
        QString imgPath = ":/" + m_Images[i] + ".png";
        QPixmap pix(imgPath);
        
        int availWidth = width() / 3;
        int availHeight = height() * 5 / 6 / 3;
        
        if (pix.width() > pix.height()) {
            pix = pix.scaledToWidth(availWidth);
        } else {
            pix = pix.scaledToHeight(availHeight);
        }
        
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
    QString selectImageText = "Please select images corresponding to category " + category;
    
    m_QuestionLabel->setText(selectImageText);
    m_QuestionLabel->setFont(QFont("Timer", 20, QFont::Bold));
    m_QuestionLabel->setAlignment(Qt::AlignCenter);
}

void NoRobotWidget::imageClicked(int imageIndex)
{
    m_Answer[imageIndex] = !m_Answer[imageIndex];
    if (m_Answer[imageIndex])
        m_Labels[imageIndex]->setSelected(true);
    else
        m_Labels[imageIndex]->setSelected(false);
    update();
    
    //qDebug() << "Image clicked " << imageIndex;
}

void NoRobotWidget::submitAnswer()
{
    QString answer;
    for (int i = 0; i < m_ImagesCount; i++) {
        answer += m_Answer[i] ? "1" : "0";
    }
    m_MultiRound->noRobotRegister(m_RequestId, answer);
    emit noRobotSubmit();
}

void NoRobotWidget::setRequestId(const QString& id)
{
    m_RequestId = id;
}

void NoRobotWidget::resizeEvent(QResizeEvent* event)
{
    if (!m_ResizedOnce) {
        m_ResizedOnce = true;
        displayAndScaleImages();
    }
    QWidget::resizeEvent(event);
}
    


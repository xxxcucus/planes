#include "serversettingsoptions.h"

#include <QGridLayout>
#include <QLabel>
#include <QPushButton>

ServerSettingsOptions::ServerSettingsOptions(QSettings* settings, QWidget* parent): QFrame(parent), m_Settings(settings)
{
    QGridLayout* gLayout = new QGridLayout();
    QString titleText = QString("<b> Server Settings (for multiplayer version) </b>");
	QLabel* titleLabel = new QLabel();
	titleLabel->setText(titleText);
    QLabel* serverPathLabel = new QLabel("Path to web server");
    m_PathToServerLineEdit = new QLineEdit(m_Settings->value("multiplayer/serverpath").toString());
    
    QLabel* chatServerPathLabel = new QLabel("Path to chat server");
    m_PathToChatServerLineEdit = new QLineEdit(m_Settings->value("multiplayer/chatserverpath").toString());
    QPushButton* submitButton = new QPushButton("Save settings");

    gLayout->addWidget(titleLabel);
    gLayout->addWidget(serverPathLabel, 1, 0, 1, 1);
    gLayout->addWidget(m_PathToServerLineEdit, 2, 0, 1, 1);
    gLayout->addWidget(chatServerPathLabel, 3, 0, 1, 1);
    gLayout->addWidget(m_PathToChatServerLineEdit, 4, 0, 1, 1);
    gLayout->addWidget(submitButton, 5, 2, 1, 1);
    
    setLayout(gLayout);
    
    setFrameStyle(QFrame::Panel | QFrame::Raised);
	setSizePolicy(QSizePolicy::Fixed, QSizePolicy::Fixed);
    
    connect(submitButton, &QPushButton::clicked,this, &ServerSettingsOptions::setPathToServer);
}


void ServerSettingsOptions::setPathToServer()
{
    m_Settings->setValue("multiplayer/serverpath", m_PathToServerLineEdit->text().trimmed());
}

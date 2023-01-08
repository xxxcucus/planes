#include "norobotdialog.h"
#include "norobotwidget.h"
#include <QBoxLayout>
#include <QDebug>

NoRobotDialog::NoRobotDialog(QNetworkAccessManager* networkManager, QSettings* settings, GlobalData* globalData, GameInfo* gameInfo, MultiplayerRound* mrd, QWidget* parent) : 
    QDialog(parent),  m_NetworkManager(networkManager), m_Settings(settings), m_GlobalData(globalData), m_GameInfo(gameInfo), m_MultiRound(mrd) {
    setAttribute(Qt::WA_DeleteOnClose);
    m_NoRobotWidget = new NoRobotWidget(m_NetworkManager, m_Settings, m_GlobalData, m_GameInfo, m_MultiRound);
    setWindowModality(Qt::WindowModal);
    setLayoutDirection(Qt::LayoutDirection::LeftToRight);
    resize(800, 600);
    QBoxLayout* mainDialogLayout = new QBoxLayout(QBoxLayout::LeftToRight);
    mainDialogLayout->addWidget(m_NoRobotWidget);
    //mainDialogLayout.setMargin (0);
    setLayout(mainDialogLayout);
    connect(m_NoRobotWidget, &NoRobotWidget::noRobotSubmit, this, &NoRobotDialog::closeDialog);
}

void NoRobotDialog::setImages(const std::vector<QString>& images) {
    if (m_NoRobotWidget != nullptr)
        m_NoRobotWidget->setImages(images);
}
void NoRobotDialog::setQuestion(const QString& question) {
    if (m_NoRobotWidget != nullptr)
        m_NoRobotWidget->setQuestion(question);
}
void NoRobotDialog::setRequestId(const QString& id) {
    if (m_NoRobotWidget != nullptr)
        m_NoRobotWidget->setRequestId(id);
}

void NoRobotDialog::closeDialog() {
    qDebug() << "close dialog";
    close();
}

void NoRobotDialog::closeEvent(QCloseEvent* event)
{
    qDebug() << "close event";
    event->accept();
}
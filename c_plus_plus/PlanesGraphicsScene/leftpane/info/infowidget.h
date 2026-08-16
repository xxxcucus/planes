#ifndef INFOWIDGET_H
#define INFOWIDGET_H


#include <QWidget>
#include <QTextEdit>



class InfoWidget : public QWidget
{
    Q_OBJECT

public:
    explicit InfoWidget(QWidget* parent = nullptr);
    void addMessage(const QString& msg);


private:
    QTextEdit* m_TextEdit;
    
};


#endif // INFOWIDGET_H

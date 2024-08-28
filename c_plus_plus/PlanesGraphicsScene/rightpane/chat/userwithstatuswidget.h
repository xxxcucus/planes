#ifndef _USER_WITH_STATUS_WIDGET__
#define _USER_WITH_STATUS_WIDGET__
#include <QWidget>

class UserWithStatusWidget : public QWidget {
  Q_OBJECT

public:
    UserWithStatusWidget(const QString& name, bool online, int width, QWidget* parent = nullptr);

    QString getName() const;

private:
    QString m_Name;
    bool m_Online = false;
};

#endif

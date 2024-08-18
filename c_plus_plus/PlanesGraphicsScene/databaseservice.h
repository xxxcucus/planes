#include <QtSql>
#include <QSqlDatabase>
#include <QCoreApplication>
#include "viewmodels/receivedchatmessageviewmodel.h"

class DatabaseService {

public:
    DatabaseService();
    ~DatabaseService();
    bool openDb();

    bool addChatMessage(const ReceivedChatMessageViewModel& message);


private:
    QSqlDatabase m_SqliteDb;
    const QString m_DbPath = QCoreApplication::applicationDirPath() + "/planes.db";
};

#include <QtSql>
#include <QSqlDatabase>
#include <QCoreApplication>

class DatabaseService {

public:
    DatabaseService();
    ~DatabaseService();
    bool openDb();


private:
    QSqlDatabase m_SqliteDb;
    const QString m_DbPath = QCoreApplication::applicationDirPath() + "/planes.db";
};

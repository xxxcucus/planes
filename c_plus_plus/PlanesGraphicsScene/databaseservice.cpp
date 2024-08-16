#include "databaseservice.h"
#include <QDebug>

DatabaseService::DatabaseService() {
    m_SqliteDb = QSqlDatabase::addDatabase("QSQLITE");
    m_SqliteDb.setDatabaseName(m_DbPath);
}

DatabaseService::~DatabaseService() {
    m_SqliteDb.close();
}

bool DatabaseService::openDb() {
    bool ok = false;
    if (m_SqliteDb.open()) {
        qDebug() << "Sqlite database " << m_DbPath << " is open";
    }
    return ok;
}

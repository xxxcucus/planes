#include "databaseservice.h"
#include <QDebug>
#include <QSqlQuery>

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

bool DatabaseService::addChatMessage(const ReceivedChatMessageViewModel& message) {
    //TODO: check if messages exist for the same username and other user id or the same user id and other user name

    QSqlQuery query(m_SqliteDb);
    query.prepare("INSERT INTO ChatMessage (id, sender_id, sender_name, receiver_id, receiver_name, message, created_at) VALUES (0, :sender_id, :sender_name, :receiver_id, :receiver_name, :message, :createdAt)");
    query.bindValue(":sender_id", (int)message.m_SenderId);
    query.bindValue(":sender_name", message.m_SenderName);
    query.bindValue(":receiver_id", (int)message.m_ReceiverId);
    query.bindValue(":receiver_name", message.m_ReceiverName);
    query.bindValue(":message", message.m_Message);
    query.bindValue(":createdAt", message.m_CreatedAt);

    bool saveOK = query.exec();

    if (!saveOK)
        qDebug() << query.lastError();

    return saveOK;
}

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

bool DatabaseService::deleteOldMessages(int daysBefore) {
    QSqlQuery query(m_SqliteDb);
    query.prepare("DELETE FROM ChatMessage WHERE created_at <= date('now',':days day')");
    query.bindValue(":days", -daysBefore);

    bool deleteOK = query.exec();
    if (!deleteOK)
        qDebug() << query.lastError();

    return deleteOK;
}

std::vector<ReceivedChatMessageViewModel> DatabaseService::getMessages(const QString& username, long int userid) {
    std::vector<ReceivedChatMessageViewModel> retVal;

    QSqlQuery query(m_SqliteDb);
    query.prepare("SELECT sender_id, sender_name, message, created_at FROM ChatMessage WHERE receiver_id = :id and receiver_name = :name ORDER BY created_at ASC");
    query.bindValue(":id", (int)userid);
    query.bindValue(":name", username);
    query.exec();

    while (query.next()) {
        int sender_id = query.value(0).toInt();
        QString sender_name = query.value(1).toString();
        QString message = query.value(2).toString();
        QDateTime dateTime = query.value(3).toDateTime();

        qDebug() << sender_id << " " << sender_name << " " << message << " " << dateTime.toString();

        ReceivedChatMessageViewModel viewModel;
        viewModel.m_SenderId = sender_id;
        viewModel.m_SenderName = sender_name;
        viewModel.m_Message = message;
        viewModel.m_CreatedAt = dateTime;
        viewModel.m_ReceiverId = userid;
        viewModel.m_ReceiverName = username;

        retVal.push_back(viewModel);
    }

    return retVal;
}

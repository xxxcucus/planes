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


bool DatabaseService::addChatMessage(const ReceivedChatMessageViewModel& message, long int recorder_id, const QString& recorder_name) {
    QSqlQuery query(m_SqliteDb);
    query.prepare("INSERT INTO ChatMessage (sender_id, sender_name, receiver_id, receiver_name, recorder_id, recorder_name, message, created_at) VALUES (:sender_id, :sender_name, :receiver_id, :receiver_name, :recorder_id, :recorder_name, :message, :createdAt)");
    query.bindValue(":sender_id", (int)message.m_SenderId);
    query.bindValue(":sender_name", message.m_SenderName);
    query.bindValue(":receiver_id", (int)message.m_ReceiverId);
    query.bindValue(":receiver_name", message.m_ReceiverName);
    query.bindValue(":recorder_id", (int)recorder_id);
    query.bindValue(":recorder_name", recorder_name);
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
    query.prepare("SELECT sender_id, sender_name, message, created_at, receiver_id, receiver_name FROM ChatMessage WHERE ((receiver_id = :id and receiver_name = :name) or (sender_id = :id and sender_name = :name)) and recorder_id = :id and recorder_name = :name ORDER BY created_at ASC");
    query.bindValue(":id", (int)userid);
    query.bindValue(":name", username);
    query.exec();

    while (query.next()) {
        int sender_id = query.value(0).toInt();
        QString sender_name = query.value(1).toString();
        QString message = query.value(2).toString();
        QDateTime dateTime = query.value(3).toDateTime();
        QString receiver_name = query.value(5).toString();
        int receiver_id = query.value(4).toInt();

        //qDebug() << sender_id << " " << sender_name << " " << receiver_id << " " << receiver_name << " " << message << " " << dateTime.toString();

        ReceivedChatMessageViewModel viewModel;
        viewModel.m_SenderId = sender_id;
        viewModel.m_SenderName = sender_name;
        viewModel.m_Message = message;
        viewModel.m_CreatedAt = dateTime;
        viewModel.m_ReceiverId = receiver_id;
        viewModel.m_ReceiverName = receiver_name;

        retVal.push_back(viewModel);
    }

    return retVal;
}

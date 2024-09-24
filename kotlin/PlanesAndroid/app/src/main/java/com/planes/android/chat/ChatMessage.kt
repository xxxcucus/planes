package com.planes.android.chat

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "ChatMessages")
data class ChatMessage(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val m_Id: Int,

    @ColumnInfo(name = "sender_id")
    val m_SenderId: Int,

    @ColumnInfo(name = "sender_name")
    val m_SenderName: String,

    @ColumnInfo(name = "receiver_id")
    val m_ReceiverId: Int,

    @ColumnInfo(name = "receiver_name")
    val m_ReceiverName: String,

    @ColumnInfo(name = "recorder_id")
    val m_RecorderId: Int,

    @ColumnInfo(name = "recorder_name")
    val m_RecorderName: String,

    @ColumnInfo(name = "message")
    val m_Message: String,

    @ColumnInfo(name = "created_at")
    val m_CreatedAt: Date
)
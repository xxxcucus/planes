package com.planes.android.chat

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "NewMessages")
data class NewMessagesFlag(
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

    @ColumnInfo(name = "new_messages")
    val m_NewMessages: Boolean,
)

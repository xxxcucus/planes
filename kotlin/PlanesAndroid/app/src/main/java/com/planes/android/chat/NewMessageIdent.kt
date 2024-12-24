package com.planes.android.chat

data class NewMessageIdent(
    val senderName: String,
    val senderId: Long,
    val receiverName: String,
    val receiverId: Long) {
}
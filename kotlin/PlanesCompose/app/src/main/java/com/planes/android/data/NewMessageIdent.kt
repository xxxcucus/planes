package com.planes.android.data

data class NewMessageIdent(
    val senderName: String,
    val senderId: Long,
    val receiverName: String,
    val receiverId: Long) {
}
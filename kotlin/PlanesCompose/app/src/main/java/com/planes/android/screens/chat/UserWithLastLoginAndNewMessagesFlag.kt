package com.planes.android.screens.chat


data class UserWithLastLoginAndNewMessagesFlag(
    val m_UserName : String,
    val m_UserId: String,
    val m_LastLogin: String,
    val m_NewMessagesFlag: Boolean
)
package com.planes.android.chat

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = arrayOf(ChatMessage::class), version = 1)

abstract class PlanesDatabase : RoomDatabase() {
    abstract fun chatDao(): ChatDao
}
package com.planes.android.chat

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = arrayOf(ChatMessage::class), version = 1)
@TypeConverters(Converters::class)

abstract class PlanesDatabase : RoomDatabase() {
    abstract fun chatDao(): ChatDao
}
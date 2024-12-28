package com.planes.android.chat

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = arrayOf(ChatMessage::class, NewMessagesFlag::class),
    version = 2,
    autoMigrations = arrayOf(AutoMigration(from = 1, to = 2))
    )
@TypeConverters(Converters::class)

abstract class PlanesDatabase : RoomDatabase() {
    abstract fun chatDao(): ChatDao
    abstract fun newMessagesDao(): NewMessagesDao
}
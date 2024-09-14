package com.planes.android.chat

import android.content.Context
import androidx.room.Room

class DatabaseService internal constructor(private val m_Context: Context) {

    private val db : PlanesDatabase
    init {
        db = Room.databaseBuilder(
            m_Context,
            PlanesDatabase::class.java, "planes.db"
        ).build()
    }
}
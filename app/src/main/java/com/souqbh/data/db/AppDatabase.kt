package com.souqbh.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.souqbh.data.db.entity.CountryCode


@Database(entities = [(CountryCode::class)], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun appDao(): AppDao
}
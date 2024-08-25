package com.ahmer.accounts.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ahmer.accounts.database.dao.AdminDao
import com.ahmer.accounts.database.dao.PersonDao
import com.ahmer.accounts.database.dao.TransDao
import com.ahmer.accounts.database.entity.PersonsEntity
import com.ahmer.accounts.database.entity.TransEntity

@Database(
    entities = [
        PersonsEntity::class,
        TransEntity::class,
    ],
    version = 1,
    exportSchema = false,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun adminDao(): AdminDao
    abstract fun personDao(): PersonDao
    abstract fun transDao(): TransDao
}
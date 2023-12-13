package com.ahmer.accounts.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ahmer.accounts.database.dao.AdminDao
import com.ahmer.accounts.database.dao.ExpenseDao
import com.ahmer.accounts.database.dao.PersonDao
import com.ahmer.accounts.database.dao.TransDao
import com.ahmer.accounts.database.entity.ExpenseEntity
import com.ahmer.accounts.database.entity.PersonsEntity
import com.ahmer.accounts.database.entity.TransEntity

@Database(
    entities = [
        ExpenseEntity::class,
        PersonsEntity::class,
        TransEntity::class,
    ],
    version = 1,
    exportSchema = false,
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun adminDao(): AdminDao
    abstract fun expenseDao(): ExpenseDao
    abstract fun personDao(): PersonDao
    abstract fun transDao(): TransDao
}
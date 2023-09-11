package com.ahmer.accounts.dl

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ahmer.accounts.database.AppDatabase
import com.ahmer.accounts.database.dao.AdminDao
import com.ahmer.accounts.database.dao.TransDao
import com.ahmer.accounts.database.dao.UserDao
import com.ahmer.accounts.database.repository.TransRepository
import com.ahmer.accounts.database.repository.TransRepositoryImp
import com.ahmer.accounts.database.repository.UserRepository
import com.ahmer.accounts.database.repository.UserRepositoryImp
import com.ahmer.accounts.preferences.PreferencesManager
import com.ahmer.accounts.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providesDatabase(
        @ApplicationContext context: Context,
        //usersProvider: Provider<UserDao>,
        //transProvider: Provider<TransDao>
    ): AppDatabase = Room.databaseBuilder(
        context.applicationContext, AppDatabase::class.java, Constants.DATABASE_NAME
    ).setJournalMode(RoomDatabase.JournalMode.TRUNCATE) //For backup in single file
        .fallbackToDestructiveMigration()
        //.addCallback(DbCallback(usersProvider, transProvider))
        .build()

    @Provides
    @Singleton
    fun providesDataStore(@ApplicationContext context: Context): PreferencesManager =
        PreferencesManager(context)

    @Provides
    @Singleton
    fun providesAdminDao(database: AppDatabase): AdminDao = database.adminDao()

    @Provides
    @Singleton
    fun providesTransDao(database: AppDatabase): TransDao = database.transDao()

    @Provides
    @Singleton
    fun providesUsersDao(database: AppDatabase): UserDao = database.usersDao()

    @Provides
    @Singleton
    fun providesTransRepository(transDao: TransDao): TransRepository = TransRepositoryImp(transDao)

    @Provides
    @Singleton
    fun providesUserRepository(userDao: UserDao): UserRepository = UserRepositoryImp(userDao)
}
package com.souqbh.di.module

import android.content.Context
import androidx.room.Room
import com.souqbh.data.db.AppDatabase
import com.souqbh.utils.Prefs
import com.souqbh.utils.constants.AppConstants
import dagger.Module
import dagger.Provides

@Module
class LocalDataModule {

    /**
     * Provides the database name.
     * @return the database name
     */
    @Provides
    fun provideDatabaseName(): String = AppConstants.DB_NAME

    /**
     * Provides the AppDatabase instance.
     * @param dbName the db name used to instantiate the AppDatabase
     * @param context the application context
     * @return the Post service implementation.
     */
    @Provides
    fun provideAppDatabase(dbName: String, context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, dbName)
            .allowMainThreadQueries()
            .build()
    }

    /**
     * Provides the preferences.
     * @param context the context used to instantiate the preference
     * @return the preference object.
     */
    @Provides
    fun providePrefs(context: Context): Prefs = Prefs.getInstance(context)!!

}
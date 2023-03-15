package com.volie.lastearthquakes.di

import android.content.Context
import androidx.room.Room
import com.volie.lastearthquakes.db.EarthquakeDao
import com.volie.lastearthquakes.db.EarthquakeDatabase
import com.volie.lastearthquakes.util.Constant.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context
    ): EarthquakeDatabase {
        return Room.databaseBuilder(
            context,
            EarthquakeDatabase::class.java,
            DATABASE_NAME
        ).build()
    }

    @Singleton
    @Provides
    fun provideAerthquakeDao(database: EarthquakeDatabase): EarthquakeDao {
        return database.getEarthquakeDao()
    }
}
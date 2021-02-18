package com.x.coroutineskot

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton


@Module
@InstallIn(ApplicationComponent::class)
class DbModule {

    @Provides
    @Singleton
     fun provideDatabase(@ApplicationContext application: Context): AppDatabase {
        return Room.databaseBuilder(
            application, AppDatabase::class.java, "tour.db")
          //  .addMigrations(MIGRATION_1_2)
            .allowMainThreadQueries().build()
    }

    /**Add new column to star a content entity */
    val MIGRATION_1_2: Migration = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL(
                "ALTER TABLE MarkerEntity "
                        + " ADD COLUMN markerOrder INTEGER DEFAULT 0 NOT NULL"
            )
        }
    }

    @Provides
    @Singleton
     fun provideDao(appDatabase: AppDatabase): TourDao {
        return appDatabase.tourDao()
    }



}

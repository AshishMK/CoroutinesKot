package com.x.coroutineskot

import androidx.room.Database
import androidx.room.RoomDatabase
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent


@Database(entities = [TourDataEntity::class,TodoEntity::class], version = 1, exportSchema = false)

abstract class AppDatabase : RoomDatabase() {

    abstract fun tourDao(): TourDao

}

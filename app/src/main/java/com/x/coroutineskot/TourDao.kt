package com.x.coroutineskot

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
/**
 * Data Access Object for database table transaction
 * */
@Dao
interface TourDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContents(entry: TourDataEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveToDos(entry: List<TodoEntity>): LongArray
    @Query("SELECT * FROM `TodoEntity` order by id ")
    fun getTodos(): Flow<List<TodoEntity>>

    @Query("SELECT * FROM `TourDataEntity` order by id ")
    fun getTours(): Flow<List<TourDataEntity>>

    @Query("SELECT * FROM `TourDataEntity` where id = :id")
    fun getTour(id: Long): TourDataEntity

    @Query("SELECT * FROM `TourDataEntity`  order by id desc limit 1")
    fun getLastTour(): TourDataEntity


}
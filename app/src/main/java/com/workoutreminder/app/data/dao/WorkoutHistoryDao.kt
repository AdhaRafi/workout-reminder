package com.workoutreminder.app.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.workoutreminder.app.data.entity.WorkoutHistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutHistoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistory(history: WorkoutHistoryEntity): Long

    @Query("SELECT * FROM workout_history WHERE completedAt >= :sinceMillis ORDER BY completedAt DESC")
    fun getHistoriesSince(sinceMillis: Long): Flow<List<WorkoutHistoryEntity>>

    @Query("SELECT * FROM workout_history ORDER BY completedAt DESC")
    fun getAllHistories(): Flow<List<WorkoutHistoryEntity>>

    @Query("SELECT COUNT(*) FROM workout_history WHERE completedAt >= :sinceMillis")
    fun getRecentHistoryCount(sinceMillis: Long): Flow<Int>

    @Query("DELETE FROM workout_history WHERE id = :id")
    suspend fun deleteHistoryById(id: Int)
}

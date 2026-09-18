package com.workoutreminder.app.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.workoutreminder.app.data.dao.ReminderDao
import com.workoutreminder.app.data.dao.TaskDao
import com.workoutreminder.app.data.dao.WorkoutHistoryDao
import com.workoutreminder.app.data.entity.ReminderEntity
import com.workoutreminder.app.data.entity.TaskEntity
import com.workoutreminder.app.data.entity.WorkoutHistoryEntity

@Database(
    entities = [ReminderEntity::class, WorkoutHistoryEntity::class, TaskEntity::class],
    version = 4,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun reminderDao(): ReminderDao
    abstract fun workoutHistoryDao(): WorkoutHistoryDao
    abstract fun taskDao(): TaskDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "workout_reminder_db"
                )
                    .fallbackToDestructiveMigration(true)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

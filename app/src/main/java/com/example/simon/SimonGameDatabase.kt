package com.example.simon

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [SimonGame::class], version = 2, exportSchema = false)
abstract class SimonGameDatabase : RoomDatabase() {
    abstract val simonGameDao:SimonGameDao

    companion object {
        @Volatile
        private var INSTANCE: SimonGameDatabase? = null
        fun getInstance(context: Context): SimonGameDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        SimonGameDatabase::class.java,
                        "SimonGameDB"
                    ).build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}
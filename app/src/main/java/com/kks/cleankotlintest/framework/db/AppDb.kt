package com.kks.cleankotlintest.framework.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.kks.cleankotlintest.BuildConfig
import com.kks.cleankotlintest.framework.db.dao.MovieDao
import com.kks.cleankotlintest.presentation.model.MovieVO

/**
 * Created by kaungkhantsoe on 19/05/2021.
 **/
@Database(
    entities = [MovieVO::class],
    exportSchema = false,
    version = 4
)
abstract class AppDb : RoomDatabase() {
    abstract fun MovieDao(): MovieDao

    companion object {
        @Volatile
        private var INSTANCE: AppDb? = null

        fun getDatabase(context: Context): AppDb {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDb::class.java,
                    BuildConfig.DATABASE_NAME
                )
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}

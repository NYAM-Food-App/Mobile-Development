package com.example.nyam.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.nyam.data.local.dao.HistoryDao
import com.example.nyam.data.local.dao.RecipesDao
import com.example.nyam.data.local.entity.HistoryEntity
import com.example.nyam.data.local.entity.RecipesEntity

@Database(
    entities = [RecipesEntity::class, HistoryEntity::class],
    version = 1,
    exportSchema = false
)
abstract class NyamDatabase : RoomDatabase() {

    abstract fun recipesDao(): RecipesDao
    abstract fun historyDao(): HistoryDao

    companion object {
        @Volatile
        private var instance: NyamDatabase? = null
        fun getInstance(context: Context): NyamDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    NyamDatabase::class.java, "NyamDatabase.db"
                ).build()
            }
    }
}
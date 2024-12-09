package com.example.nyam.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.nyam.data.local.entity.HistoryEntity

@Dao
interface HistoryDao {

    @Query("SELECT * FROM history ORDER BY id DESC")
    fun getHistory(): LiveData<List<HistoryEntity>>

    @Query("SELECT * FROM history WHERE id =:id ORDER  BY `id` DESC")
    fun getDetailHistory(id: Int): LiveData<HistoryEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistory(recipes: List<HistoryEntity>)


    @Query("DELETE FROM history ")
    suspend fun deleteAllHistory()
}
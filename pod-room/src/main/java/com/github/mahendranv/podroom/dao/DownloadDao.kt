package com.github.mahendranv.podroom.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.github.mahendranv.podroom.entity.Download
import kotlinx.coroutines.flow.Flow

@Dao
interface DownloadDao {

    @Upsert
    fun upsert(download: Download)

    @Query("DELETE from download where id = :id")
    fun delete(id: Long)

    @Query("SELECT * from download")
    fun fetchAll(): Flow<List<Download>>

}
package com.github.mahendranv.podroom.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.github.mahendranv.podroom.entity.Episode
import kotlinx.coroutines.flow.Flow

@Dao
interface EpisodeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(episode: Episode)

    @Transaction
    suspend fun insertAll(episodes: List<Episode>) {
        episodes.forEach { episode -> insert(episode) }
    }

    @Query("SELECT * FROM episodes WHERE channel_id = :channelId ORDER BY pub_date DESC")
    fun getEpisodesByChannelId(channelId: Long): Flow<List<Episode>>

    @Query("SELECT * FROM episodes ORDER BY pub_date DESC")
    fun getAllEpisodes(): Flow<List<Episode>>

}
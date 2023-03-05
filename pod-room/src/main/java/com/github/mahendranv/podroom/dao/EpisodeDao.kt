package com.github.mahendranv.podroom.dao

import android.util.Log
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
    suspend fun insert(episode: Episode): Long

    @Transaction
    suspend fun insertAll(episodes: List<Episode>): Int {
        var counter = 0
        episodes.forEach { episode ->
            val id = insert(episode)
            if (id != -1L) {
                counter++
            }
        }
        Log.d(TAG, "insertAll: $counter out of ${episodes.size} episodes inserted")
        return counter
    }

    @Query("SELECT * FROM episodes WHERE channel_id = :channelId ORDER BY pub_date DESC")
    fun getEpisodesByChannelId(channelId: Long): Flow<List<Episode>>

    @Query("SELECT * FROM episodes ORDER BY pub_date DESC")
    fun getAllEpisodes(): Flow<List<Episode>>

    companion object {

        private const val TAG = "EpisodeDao"
    }
}
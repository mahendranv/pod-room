package com.github.mahendranv.podroom.dao

import android.util.Log
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.github.mahendranv.podroom.entity.Episode
import kotlinx.coroutines.flow.Flow

@Dao
interface EpisodeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(episode: Episode): Long

    @Update
    suspend fun update(episode: Episode)

    @Transaction
    suspend fun insertAll(episodes: List<Episode>): Int {
        var insertions = 0
        var updates = 0
        episodes.filter {
            it.streamUrl.trim().isNotEmpty()
        }.forEach { episode ->
            val existingEntry = getIDByStream(episode.streamUrl)
            if (existingEntry == null) {
                insert(episode)
                insertions++
            } else {
                Log.d(TAG, "duplicate: [$existingEntry]" + episode.title + " > " + episode.streamUrl)
                update(episode.copy(id = existingEntry))
                updates++
            }
        }
        Log.d(
            TAG, "insertAll: [inserts: $insertions, updates: $updates, " +
                    "skipped = ${episodes.size - (insertions + updates)}]"
        )
        return insertions + updates
    }

    @Query("SELECT * FROM episodes WHERE channel_id = :channelId ORDER BY pub_date DESC")
    fun getEpisodesByChannelId(channelId: Long): Flow<List<Episode>>

    @Query("SELECT * FROM episodes ORDER BY pub_date DESC")
    fun getAllEpisodes(): Flow<List<Episode>>

    @Query("SELECT * FROM episodes where id = :id")
    fun getEpisodeById(id: Long): Episode?

    /**
     * Returns pk if the given stream has been already inserted.
     */
    @Query("SELECT id FROM episodes where stream_url = :stream")
    fun getIDByStream(stream: String): Long?

    companion object {

        private const val TAG = "EpisodeDao"
    }
}
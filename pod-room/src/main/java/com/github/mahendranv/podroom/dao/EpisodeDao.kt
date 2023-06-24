package com.github.mahendranv.podroom.dao

import android.util.Log
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.github.mahendranv.podroom.entity.Episode
import com.github.mahendranv.podroom.views.EpisodeDetails
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

    // EPISODE details
    @Query("$SELECTION_EPISODE_DETAILS WHERE pd.id = :podcastId ORDER BY :order")
    fun getEpisodesByPodcast(
        podcastId: Int,
        order: String = "DESC"
    ): PagingSource<Int, EpisodeDetails>

    @Query("$SELECTION_EPISODE_DETAILS ORDER BY :order")
    fun getAllEpisodeDetails(order: String = "DESC"): PagingSource<Int, EpisodeDetails>

    /**
     * Returns pk if the given stream has been already inserted.
     */
    @Query("SELECT id FROM episodes where stream_url = :stream")
    fun getIDByStream(stream: String): Long?

    companion object {

        private const val TAG = "EpisodeDao"

        internal const val SELECTION_EPISODE_DETAILS = """
    SELECT e.id, e.title, e.description, e.stream_url, pd.title as pod_name, pd.image, 
        e.season, e.episode, e.explicit, e.pub_date, e.duration, e.duration_in_seconds, 
        e.channel_id, COALESCE(d.progress, 0) AS download_progress, 
        e.link, COALESCE(pe.position, -1) AS position, 
        COALESCE(pe.complete, 0) AS downloaded 
    FROM episodes as e 
    LEFT JOIN download as d ON e.id = d.id 
    LEFT JOIN player_entries AS pe ON e.id = pe.id 
    LEFT JOIN podcasts AS pd ON pd.id = e.channel_id
        """
    }
}
package com.github.mahendranv.podroom.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.github.mahendranv.podroom.entity.Podcast
import kotlinx.coroutines.flow.Flow

@Dao
interface PodcastDao {

    /**
     * **Internal use only**: Use [insertPodcast] where biz logic exist to resolve conflicts.
     */
    @Insert
    fun insert(podcast: Podcast): Long

    @Update
    fun update(podcast: Podcast)

    @Transaction
    fun insertPodcast(podcast: Podcast): Long {
        if (podcast.feedUrl.isBlank()) {
            throw java.lang.IllegalStateException("podcast feed url cannot be empty, $podcast")
        } else {
            val existingId = findPodcastId(podcast.feedUrl)
            return if (existingId == null) {
                insert(podcast)
            } else {
                update(podcast.copy(id = existingId))
                existingId
            }
        }
    }

    @Query("select * from podcasts")
    fun fetchAll(): Flow<List<Podcast>>

    @Query("select id from podcasts where feed_url = :feedUrl")
    fun findPodcastId(feedUrl: String): Long?

    @Query("select * from podcasts where id =:id")
    fun getPodcast(id: Long): Podcast?

}
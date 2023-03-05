package com.github.mahendranv.podroom.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.github.mahendranv.podroom.entity.Podcast
import kotlinx.coroutines.flow.Flow

@Dao
interface PodcastDao {

    @Upsert
    fun insertPodcast(podcast: Podcast): Long

    @Query("select * from podcasts")
    fun fetchAll(): Flow<List<Podcast>>

    @Query("select * from podcasts where feed_url = :feedUrl")
    fun findPodcast(feedUrl: String): Podcast?

}
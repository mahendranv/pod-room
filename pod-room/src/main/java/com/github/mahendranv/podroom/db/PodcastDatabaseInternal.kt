package com.github.mahendranv.podroom.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.github.mahendranv.podroom.adapter.DateConverter
import com.github.mahendranv.podroom.dao.EpisodeDao
import com.github.mahendranv.podroom.dao.PodcastDao
import com.github.mahendranv.podroom.entity.Episode
import com.github.mahendranv.podroom.entity.Podcast

@Database(entities = [Podcast::class, Episode::class], version = 1, exportSchema = true)
@TypeConverters(DateConverter::class)
abstract class PodcastDatabaseInternal : RoomDatabase() {

    abstract fun getEpisodeDao(): EpisodeDao

    abstract fun getPodcastDao(): PodcastDao
}
package com.github.mahendranv.podroom.db

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.github.mahendranv.podroom.adapter.DateConverter
import com.github.mahendranv.podroom.dao.EpisodeDao
import com.github.mahendranv.podroom.dao.PlayerDao
import com.github.mahendranv.podroom.dao.PodcastDao
import com.github.mahendranv.podroom.entity.Episode
import com.github.mahendranv.podroom.entity.PlaybackPosition
import com.github.mahendranv.podroom.entity.PlayerEntry
import com.github.mahendranv.podroom.entity.Podcast

@Database(
    entities = [Podcast::class, Episode::class, PlayerEntry::class, PlaybackPosition::class],
    version = 2,
    exportSchema = true,
    autoMigrations = [
        AutoMigration(from = 1, to = 2)
    ]
)
@TypeConverters(DateConverter::class)
abstract class PodcastDatabaseInternal : RoomDatabase() {

    abstract fun getEpisodeDao(): EpisodeDao

    abstract fun getPodcastDao(): PodcastDao

    abstract fun getPlayerDao(): PlayerDao
}
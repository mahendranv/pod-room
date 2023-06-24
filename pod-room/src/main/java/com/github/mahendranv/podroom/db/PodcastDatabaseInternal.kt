package com.github.mahendranv.podroom.db

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.github.mahendranv.podroom.adapter.DateConverter
import com.github.mahendranv.podroom.dao.DownloadDao
import com.github.mahendranv.podroom.dao.EpisodeDao
import com.github.mahendranv.podroom.dao.PlayerDao
import com.github.mahendranv.podroom.dao.PodcastDao
import com.github.mahendranv.podroom.entity.Download
import com.github.mahendranv.podroom.entity.Episode
import com.github.mahendranv.podroom.entity.PlaybackPosition
import com.github.mahendranv.podroom.entity.PlayerEntry
import com.github.mahendranv.podroom.entity.Podcast
import com.github.mahendranv.podroom.views.EpisodeDetails

@Database(
    entities = [Podcast::class, Episode::class, PlayerEntry::class, PlaybackPosition::class,
        Download::class],
    views = [EpisodeDetails::class],
    version = 3,
    exportSchema = true,
    autoMigrations = [
        AutoMigration(from = 1, to = 2),
        AutoMigration(from = 2, to = 3),
    ]
)
@TypeConverters(DateConverter::class)
abstract class PodcastDatabaseInternal : RoomDatabase() {

    abstract fun getEpisodeDao(): EpisodeDao

    abstract fun getPodcastDao(): PodcastDao

    abstract fun getPlayerDao(): PlayerDao

    abstract fun getDownloadDao(): DownloadDao
}
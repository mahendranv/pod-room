package com.github.mahendranv.podroom.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "episodes",
    foreignKeys = [
        ForeignKey(
            entity = Channel::class,
            parentColumns = ["id"],
            childColumns = ["channel_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Episode(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "description")
    val description: String,
    @ColumnInfo(name = "link")
    val link: String,
    @ColumnInfo(name = "guid")
    val guid: String,
    @ColumnInfo(name = "season")
    val season: Int,
    @ColumnInfo(name = "episode")
    val episode: Int,
    @ColumnInfo(name = "explicit")
    val explicit: Boolean,
    @ColumnInfo(name = "pub_date")
    val pubDate: Long,
    @ColumnInfo(name = "duration_in_seconds")
    val durationInSeconds: Long,
    @ColumnInfo(name = "duration")
    val duration: String,
    @ColumnInfo(name = "stream_url")
    val streamUrl: String,
    @ColumnInfo(name = "stream_type")
    val streamType: String,
    @ColumnInfo(name = "stream_size")
    val streamSize: Int,
    @ColumnInfo(name = "channel_id")
    val channelId: Int
)
package com.github.mahendranv.podroom.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "podcasts",
    indices = [
        Index(value = ["feed_url"], unique = true)
    ]
)
data class Podcast(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "link") val link: String,
    @ColumnInfo(name = "feed_url") val feedUrl: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "category") val category: String,
    @ColumnInfo(name = "image") val image: String,
    @ColumnInfo(name = "explicit") val explicit: Boolean,
    @ColumnInfo(name = "last_build_date") val lastBuildDate: Long
)
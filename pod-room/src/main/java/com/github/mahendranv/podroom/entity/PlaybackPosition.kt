package com.github.mahendranv.podroom.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "playback_position",
    foreignKeys = [
        ForeignKey(
            entity = Episode::class,
            parentColumns = ["id"],
            childColumns = ["id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("id", unique = true)
    ]
)
data class PlaybackPosition(

    @PrimaryKey
    @ColumnInfo("id")
    val id: Long,

    @ColumnInfo("playback_position", defaultValue = "0")
    val playbackPosition: Long
)

package com.github.mahendranv.podroom.entity

import androidx.annotation.IntRange
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "download",
    indices = [Index("id", unique = true)],
    foreignKeys = [
        ForeignKey(
            entity = Episode::class,
            parentColumns = ["id"],
            childColumns = ["id"],
            onDelete = ForeignKey.CASCADE
        )]
)
data class Download(
    @PrimaryKey val id: Long,
    @IntRange(from = 0, to = 100)
    @ColumnInfo(name = "progress") val progress: Int = 0,
    @ColumnInfo(name = "completed_at") val completedAt: Long = 0,
    @ColumnInfo(name = "file_path") val filePath: String = ""
)

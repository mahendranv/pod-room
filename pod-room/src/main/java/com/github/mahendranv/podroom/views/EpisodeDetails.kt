package com.github.mahendranv.podroom.views

import androidx.room.ColumnInfo
import androidx.room.DatabaseView
import com.github.mahendranv.podroom.dao.EpisodeDao

@DatabaseView(
    viewName = "episode_details",
    value = EpisodeDao.SELECTION_EPISODE_DETAILS
)
data class EpisodeDetails(
    @ColumnInfo(name = "id")
    val id: Long,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "description")
    val description: String,
    @ColumnInfo(name = "stream_url")
    val streamUrl: String,
    @ColumnInfo(name = "pod_name")
    val podName: String,
    @ColumnInfo(name = "image")
    val image: String,
    @ColumnInfo(name = "season")
    val season: Int,
    @ColumnInfo(name = "episode")
    val episode: Int,
    @ColumnInfo(name = "explicit")
    val explicit: Boolean,
    @ColumnInfo(name = "pub_date")
    val pubDate: Long,
    @ColumnInfo(name = "duration")
    val duration: String,
    @ColumnInfo(name = "duration_in_seconds")
    val durationInSeconds: Int,
    @ColumnInfo(name = "channel_id")
    val channelId: Long,
    @ColumnInfo("download_progress")
    val downloadProgress: Int,
    @ColumnInfo("link")
    val link: String,
    @ColumnInfo("position")
    val position: Int,
    @ColumnInfo("downloaded")
    val downloaded: Boolean
)
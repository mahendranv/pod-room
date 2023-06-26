package com.github.mahendranv.podroom.dao

import androidx.paging.PagingSource
import androidx.sqlite.db.SimpleSQLiteQuery
import com.github.mahendranv.podroom.views.EpisodeDetails

class EpisodeCustomQueries(private val episodeDao: EpisodeDao) {

    fun getAllEpisodes(order: Order): PagingSource<Int, EpisodeDetails> {
        val query = "$SELECTION_EPISODE_DETAILS ORDER BY e.pub_date ${order.name}"
        val roomQuery = SimpleSQLiteQuery(query)
        return episodeDao.getEpisodeDetails(roomQuery)
    }

    fun getEpisodesOfPodcast(podcastId: Long, order: Order): PagingSource<Int, EpisodeDetails> {
        val query = "$SELECTION_EPISODE_DETAILS where pd.id = $podcastId ORDER BY e.pub_date ${order.name}"
        val roomQuery = SimpleSQLiteQuery(query)
        return episodeDao.getEpisodeDetails(roomQuery)
    }

    companion object {

        internal const val SELECTION_EPISODE_DETAILS = """
    SELECT e.id, e.title, e.description, e.stream_url, pd.title as pod_name, pd.image, 
        e.season, e.episode, e.explicit, e.pub_date, e.duration, e.duration_in_seconds, 
        e.channel_id, COALESCE(d.progress, 0) AS download_progress, 
        e.link, COALESCE(pe.position, -1) AS position, 
        COALESCE(pe.complete, 0) AS downloaded 
    FROM episodes as e 
    LEFT JOIN download as d ON e.id = d.id 
    LEFT JOIN player_entries AS pe ON e.id = pe.id 
    LEFT JOIN podcasts AS pd ON pd.id = e.channel_id
        """
    }
}
package com.github.mahendranv.podroom.sdk

import androidx.paging.PagingSource
import com.github.mahendranv.podroom.dao.EpisodeCustomQueries
import com.github.mahendranv.podroom.dao.EpisodeDao
import com.github.mahendranv.podroom.dao.Order
import com.github.mahendranv.podroom.entity.Episode
import com.github.mahendranv.podroom.views.EpisodeDetails
import kotlinx.coroutines.flow.Flow

/**
 * Episode related read operations.
 */
class EpisodeStore(
    private val episodeDao: EpisodeDao,
    private val episodeCustomQueries: EpisodeCustomQueries
) {

    /**
     * Returns all episodes.
     */
    fun getAllEpisodes() = episodeDao.getAllEpisodes()

    /**
     * Returns episodes of specific podcast.
     */
    fun getEpisodesOfPodcast(podcastId: Long): Flow<List<Episode>> {
        return episodeDao.getEpisodesByChannelId(podcastId)
    }

    /**
     *  Returns a [PagingSource] for all episodes ordered by publish date.
     *
     *  @param publishDateOrder the order.
     */
    fun getAllEpisodesPaged(publishDateOrder: Order): PagingSource<Int, EpisodeDetails> {
        return episodeCustomQueries.getAllEpisodes(publishDateOrder)
    }

    /**
     * Returns [PagingSource] for episodes from single podcast.
     *
     * @param podcastId Podcast ID
     * @param publishDateOrder order for episodes.
     */
    fun getPodcastEpisodesPaged(podcastId: Long, publishDateOrder: Order): PagingSource<Int, EpisodeDetails> {
        return episodeCustomQueries.getEpisodesOfPodcast(podcastId, publishDateOrder)
    }

}
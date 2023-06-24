package com.github.mahendranv.podroom

import androidx.paging.PagingSource
import com.github.mahendranv.podroom.dao.EpisodeDao
import com.github.mahendranv.podroom.entity.Download
import com.github.mahendranv.podroom.sdk.DownloadStore
import com.github.mahendranv.podroom.sdk.PlayerStore
import com.github.mahendranv.podroom.views.EpisodeDetails
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class EpisodeDetailsTest : BaseTest() {

    private lateinit var episodeDao: EpisodeDao
    private lateinit var downloadStore: DownloadStore
    private lateinit var playerStore: PlayerStore

    @Before
    override fun setup() {
        super.setup()
        runBlocking {
            podRoom.getSyncer().syncPodcast(TestResources.FEED_FRAGMENTED)
        }
        episodeDao = podRoom.getEpisodeDao()
        playerStore = podRoom.getPlayer()
        downloadStore = podRoom.getDownloads()
    }

    private suspend fun fetchPage(params: PagingSource.LoadParams<Int>): PagingSource.LoadResult.Page<Int, EpisodeDetails> {
        val pagingSource = episodeDao.getAllEpisodeDetails()
        val result: PagingSource.LoadResult<Int, EpisodeDetails> = pagingSource.load(params)
        assertTrue(result is PagingSource.LoadResult.Page)
        return result as PagingSource.LoadResult.Page<Int, EpisodeDetails>
    }

    @Test
    fun `fetch pages verify details columns`() = runBlocking {
        val itemKey = 0
        val params = PagingSource.LoadParams.Refresh(itemKey, 10, placeholdersEnabled = false)

        val result = fetchPage(params)
        val list = result.data
        assertEquals(10, list.size)
        val actual = list.first()
        assertEquals(Fragmented.Episode1.title, actual.title)
        assertEquals(Fragmented.Episode1.duration, actual.duration)
        assertEquals(Fragmented.Episode1.episode, actual.episode)
        assertEquals(0, actual.downloadProgress)
        assertEquals(PlayerStore.POSITION_NONE, actual.position)

        val episodeId = actual.id
        downloadStore.saveProgress(Download(id = episodeId, progress = 77))
        playerStore.enqueue(episodeId)

        val refreshed = fetchPage(params)
        val updatedEpisode = refreshed.data.first()
        assertEquals(77, updatedEpisode.downloadProgress)
        assertEquals(1, updatedEpisode.position)
    }

    @Test
    fun `fetch pages using next key`() = runBlocking {
        episodeDao.getAllEpisodeDetails()
        var itemKey: Int? = null
        val result = fetchPage(PagingSource.LoadParams.Refresh(itemKey, 10, placeholdersEnabled = false))
        val pageItems1 = result.data
        assertEquals(10, pageItems1.size)
        itemKey = result.nextKey!!

        val pageItems2 = fetchPage(PagingSource.LoadParams.Append(itemKey, 5, placeholdersEnabled = false)).data
        assertEquals(5, pageItems2.size)
    }
}
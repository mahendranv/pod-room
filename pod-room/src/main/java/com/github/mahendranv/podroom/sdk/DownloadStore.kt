package com.github.mahendranv.podroom.sdk

import com.github.mahendranv.podroom.dao.DownloadDao
import com.github.mahendranv.podroom.entity.Download
import kotlinx.coroutines.flow.Flow

/**
 * SDK front for download operations. This provides means to persist / retrieve the download progress
 * and file path **DOES NOT** facilitate download itself.
 */
class DownloadStore(private val downloadDao: DownloadDao) {

    /**
     * Fetches all downloads.
     */
    fun fetchAll(): Flow<List<Download>> = downloadDao.fetchAll()

    /**
     * Insert / Update the download meta data to db.
     */
    fun saveProgress(download: Download) {
        downloadDao.upsert(download)
    }

    /**
     * Convenient method to finalize the download entity details. This is fills in the current time
     * and file download path for future usage.
     * @param id Episode id.
     * @param filePath path where the file is available to consume.
     */
    fun markAsComplete(id: Long, filePath: String) {
        downloadDao.upsert(
            Download(
                id = id,
                progress = 100,
                completedAt = System.currentTimeMillis(),
                filePath = filePath
            )
        )
    }

    /**
     * Deletes the entry from db. **This does not delete the file itself**.
     * @param id the episode id.
     */
    fun delete(id: Long) {
        downloadDao.delete(id)
    }
}
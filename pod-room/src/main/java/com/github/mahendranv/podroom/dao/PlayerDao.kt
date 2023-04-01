package com.github.mahendranv.podroom.dao

import android.util.Log
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.github.mahendranv.podroom.entity.PlayerEntry

@Dao
interface PlayerDao {

    /**
     * Inserts entry in player DB.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entry: PlayerEntry)

    @Query("SELECT * from player_entries WHERE position BETWEEN :from AND :to")
    fun fetchInRange(from: Int, to: Int): List<PlayerEntry>

    @Query("SELECT position from player_entries ORDER BY position DESC LIMIT 1")
    fun lastPosition(): Int?

    @Query("SELECT position from player_entries ORDER BY position LIMIT 1")
    fun firstPosition(): Int?

    @Query("SELECT position from player_entries WHERE id = :episodeId")
    fun getPosition(episodeId: Long): Int?

    @Query("DELETE from player_entries WHERE id = :episodeId")
    fun delete(episodeId: Long)

    @Query("DELETE from player_entries")
    fun clearAll()

    @Transaction
    fun shift(from: Int, to: Int, direction: ShiftDirection) {
        val chunk = fetchInRange(from, to)
        chunk.forEach { entry ->
            insert(entry.shift(direction))
        }
    }

    // Player operations
    @Transaction
    fun enqueue(id: Long) {
        val lastPosition = lastPosition() ?: 0
        insert(
            PlayerEntry(
                id = id,
                position = lastPosition + 1,
                complete = false
            )
        )
    }

    /**
     * Places the given episode in mentioned position.
     */
    private fun tuckEpisode(id: Long, tuckPosition: Int) {
        val lastPosition = lastPosition()
        if (lastPosition != null) {
            // Push down the rest of the entries
            shift(from = tuckPosition, to = lastPosition, direction = ShiftDirection.DOWN)
        }
        // Insert new episode next to the current song
        insert(
            PlayerEntry(
                id = id,
                position = tuckPosition,
                complete = false
            )
        )
    }

    @Transaction
    fun play(id: Long) {
        tuckEpisode(id, 1)
    }

    @Transaction
    fun playNext(id: Long, currentEpisodeId: Long) {
        val currentPosition = getPosition(currentEpisodeId)
        if (currentPosition == null) {
            Log.w(TAG, "Cannot find current playing episode, placing in index: 1")
            tuckEpisode(id, 1)
            return
        }
        val insertionPoint = currentPosition + 1
        tuckEpisode(id, insertionPoint)
    }

    enum class ShiftDirection {
        UP,
        DOWN
    }

    companion object {

        private const val TAG = "PlayerDao"
    }
}
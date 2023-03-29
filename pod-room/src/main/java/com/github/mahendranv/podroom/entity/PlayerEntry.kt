package com.github.mahendranv.podroom.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.github.mahendranv.podroom.dao.PlayerDao

@Entity(
    tableName = "player_entries",
    foreignKeys = [
        ForeignKey(
            entity = Episode::class,
            parentColumns = ["id"],
            childColumns = ["id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["id"], unique = false)
    ]
)
data class PlayerEntry(

    @PrimaryKey
    @ColumnInfo(name = "id")
    var id: Long,

    @ColumnInfo(name = "position")
    var position: Int,

    @ColumnInfo(name = "complete")
    var complete: Boolean
) {

    fun shift(direction: PlayerDao.ShiftDirection): PlayerEntry =
        if (direction == PlayerDao.ShiftDirection.UP) {
            copy(position = position - 1)
        } else {
            copy(position = position + 1)
        }
}

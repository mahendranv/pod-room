package com.github.mahendranv.podroom.dao

import androidx.room.Dao
import androidx.room.Query
import com.github.mahendranv.podroom.entity.Channel
import kotlinx.coroutines.flow.Flow

@Dao
interface ChannelDao {

    @Query("select * from channels")
    fun fetchAllChannels(): Flow<List<Channel>>
}
package com.github.mahendranv.podroom

import android.content.Context
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.github.mahendranv.podroom.db.PodcastDatabaseInternal
import com.github.mahendranv.podroom.di.PodDIContainer
import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
abstract class BaseTest {

    protected lateinit var appContext: Context

    internal lateinit var diContainer: PodDIContainer

    protected lateinit var podRoom: PodRoom

    @Before
    open fun setup() {
        appContext = InstrumentationRegistry.getInstrumentation().context
        diContainer = PodDIContainer.getInstance()
        diContainer.db =
            Room.inMemoryDatabaseBuilder(appContext, PodcastDatabaseInternal::class.java)
                .allowMainThreadQueries()
                .build()
        diContainer.isInitialized = true

        podRoom = PodRoom.getInstance(context = appContext)
        println("setup " + diContainer.db.openHelper.writableDatabase.path)
    }

    @After
    open fun tearDown() {
        diContainer.releaseResources()
        PodRoom.clearInstance()
    }
}
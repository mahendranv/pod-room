package com.github.mahendranv.podroom

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Before
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
abstract class BaseTest {

    protected lateinit var appContext: Context

    protected lateinit var podRoom: PodRoom

    @Before
    open fun setup() {
        appContext = InstrumentationRegistry.getInstrumentation().context
        podRoom = PodRoom.getInstance(context = appContext)
    }
}
package com.starwars.kamino

import androidx.test.core.app.ApplicationProvider
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(application = TestApplication::class)
abstract class RobolectricTestBase {
    protected val context by lazy { ApplicationProvider.getApplicationContext<TestApplication>() }
}
package com.starwars.kamino.ui.planet

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.starwars.kamino.R
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class PlanetFragmentTest {

    private lateinit var fragmentScenario: FragmentScenario<PlanetFragment>

    @Before
    fun onSetup() {
        fragmentScenario = launchFragmentInContainer<PlanetFragment>()
        fragmentScenario.moveToState(Lifecycle.State.RESUMED)
    }

    @Test
    fun `should visible planet name on screen`() {
        onView(withId(R.id.textPlanetName)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
    }

    @Test
    fun `should gone progress bar on screen`() {
        onView(withId(R.id.progressBar)).check(matches(withEffectiveVisibility(Visibility.GONE)))
    }

}

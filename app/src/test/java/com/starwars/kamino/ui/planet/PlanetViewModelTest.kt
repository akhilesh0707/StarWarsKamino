package com.starwars.kamino.ui.planet

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.starwars.kamino.RxImmediateSchedulerRule
import com.starwars.kamino.ui.planet.model.LikeModel
import com.starwars.kamino.ui.planet.model.PlanetModel
import com.starwars.kamino.ui.planet.repository.PlanetRepository
import io.reactivex.Single
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.ClassRule
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.*

class PlanetViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    companion object {
        @ClassRule
        @JvmField
        val schedulers = RxImmediateSchedulerRule()
    }

    private lateinit var viewModel: PlanetViewModel

    private lateinit var repository: PlanetRepository

    @Before
    fun onSetup() {
        repository = mock(PlanetRepository::class.java)
        viewModel = PlanetViewModel(repository)
    }

    @Test
    fun `should not null view model`() {
        assertNotNull(viewModel)
    }

    @Test
    fun `call get planet api and should return error`() {
        // Given
        val error = Exception("Planet error response")
        `when`(repository.getPlanet(anyInt())).thenReturn(Single.error(error))

        // When
        viewModel.getPlanet()

        //Then
        val resultError = viewModel.planetUIModel.value as PlanetUIModel.Error
        assertEquals(resultError.error, error.message)
    }

    @Test
    fun `call get planet api and should return success response`() {
        // Given
        val planetName = "Kamino"
        val planetResponse = mock(PlanetModel::class.java)
        `when`(planetResponse.name).thenReturn(planetName)
        `when`(repository.getPlanet(anyInt())).thenReturn(Single.just(planetResponse))

        // When
        viewModel.getPlanet()

        //Then
        val success = viewModel.planetUIModel.value as PlanetUIModel.Success
        assertEquals(success.planetModel, planetResponse)
        assertEquals(success.planetModel.name, planetName)
    }

    @Test
    fun `call like planet api and should return error`() {
        // Given
        val error = Exception("Like error response")
        `when`(repository.likePlanet(anyInt())).thenReturn(Single.error(error))

        // When
        viewModel.likePlanet()

        //Then
        val resultError = viewModel.planetUIModel.value as PlanetUIModel.Error
        assertEquals(resultError.error, error.message)
    }

    @Test
    fun `call like planet api and should return success response`() {
        // Given
        val likeCount = 10
        val planetId = 7
        val likeResponse = mock(LikeModel::class.java)
        `when`(likeResponse.planetId).thenReturn(planetId)
        `when`(likeResponse.likes).thenReturn(likeCount)
        `when`(repository.likePlanet(anyInt())).thenReturn(Single.just(likeResponse))

        // When
        viewModel.likePlanet()

        //Then
        val success = viewModel.planetUIModel.value as PlanetUIModel.SuccessLike
        assertEquals(success.likeModel, likeResponse)
        assertEquals(success.likeModel.planetId, planetId)
        assertEquals(success.likeModel.likes, likeCount)
    }

}
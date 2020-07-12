package com.starwars.kamino.ui.residents

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.starwars.kamino.RxImmediateSchedulerRule
import com.starwars.kamino.ui.planet.model.PlanetModel
import com.starwars.kamino.ui.residents.model.ResidentModel
import com.starwars.kamino.ui.residents.repository.ResidentRepository
import io.reactivex.Observable
import org.junit.*
import org.junit.Assert.assertNotNull
import org.mockito.Mockito.*

class ResidentsViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    companion object {
        @ClassRule
        @JvmField
        val schedulers = RxImmediateSchedulerRule()
    }

    private lateinit var viewModel: ResidentsViewModel

    private lateinit var repository: ResidentRepository

    @Before
    fun onSetup() {
        repository = mock(ResidentRepository::class.java)
        viewModel = ResidentsViewModel(repository)
    }

    @Test
    fun `should not null view model`() {
        assertNotNull(viewModel)
    }

    @Test
    fun `call get resident api and should return error response`() {
        // Given
        val mockPlanet = mock(PlanetModel::class.java)
        val residentList = listOf("http://private-84e428-starwars2.apiary-mock.com/residents/73")
        `when`(mockPlanet.residents).thenReturn(residentList)
        viewModel.planet = mockPlanet
        val errorResponse = Exception("Resident error response")
        `when`(repository.getResident(anyInt())).thenReturn(Observable.error(errorResponse))

        // When
        viewModel.getResident()

        // Then
        val resultError = viewModel.residentUIModel.value as ResidentUIModel.Error
        Assert.assertEquals(resultError.error, errorResponse.message)
    }

    @Test
    fun `call get resident api and should return success response`() {
        // Given
        val mockPlanet = mock(PlanetModel::class.java)
        val residentList = listOf("http://private-84e428-starwars2.apiary-mock.com/residents/73")
        `when`(mockPlanet.residents).thenReturn(residentList)
        viewModel.planet = mockPlanet
        val residentResponse = mock(ResidentModel::class.java)
        val residentName = "Luma Su"
        `when`(residentResponse.name).thenReturn(residentName)
        `when`(repository.getResident(anyInt())).thenReturn(Observable.just(residentResponse))

        // When
        viewModel.getResident()

        // Then
        val success = viewModel.residentUIModel.value as ResidentUIModel.Success
        Assert.assertEquals(success.residentModel, residentResponse)
        Assert.assertEquals(success.residentModel.name, residentName)
    }
}
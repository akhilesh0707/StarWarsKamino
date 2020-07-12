package com.starwars.kamino.ui.residents

import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.starwars.kamino.RobolectricTestBase
import com.starwars.kamino.ui.residents.model.ResidentModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

class ResidentsAdapterTest : RobolectricTestBase() {

    private lateinit var adapter: ResidentsAdapter
    private val requestManager = mock(RequestManager::class.java)
    private val listener = mock(ResidentsAdapter.OnResidentClickListener::class.java)

    @Before
    fun onSetup() {
        val listOf = arrayListOf<ResidentModel>(mock(ResidentModel::class.java))
        adapter = ResidentsAdapter(listOf, requestManager, listener)
    }

    @Test
    fun `should not null adapter`() {
        assertNotNull(adapter)
    }

    @Test
    fun `adapter onCreateViewHolder should return the view type ResidentViewHolder`() {
        val parentView = getParentView()
        val viewHolder = adapter.onCreateViewHolder(parentView, 0)
        // Then
        assertNotNull(viewHolder)
    }

    @Test
    fun `should return adapter item count one`() {
        val itemCount = adapter.itemCount
        // Then
        assertEquals(1, itemCount)
    }

    @Test
    fun `should return adapter view and call bind with success`() {
        val parentView = getParentView()
        val viewHolder = adapter.onCreateViewHolder(parentView, 0)
        val resident = mock(ResidentModel::class.java)
        val residentName = "Luma Su"
        `when`(resident.name).thenReturn(residentName)
        adapter.onBindViewHolder(viewHolder, 0)
        // Then
        assertNotNull(viewHolder)
    }

    private fun getParentView(): ViewGroup {
        val recyclerView = RecyclerView(context)
        recyclerView.layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        return recyclerView
    }
}
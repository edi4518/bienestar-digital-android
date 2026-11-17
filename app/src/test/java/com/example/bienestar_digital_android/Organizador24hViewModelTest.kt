package com.example.bienestar_digital_android

import com.example.bienestar_digital_android.data.repository.DailyUsageSummary
import com.example.bienestar_digital_android.feature.organizador24h.Organizador24hViewModel.Organizador24hViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class Organizador24hViewModelTest {

    private lateinit var viewModel: Organizador24hViewModel

    @Before
    fun setUp() {
        viewModel = Organizador24hViewModel()
    }

    @Test
    fun `estado inicial del Organizador24h es valido y suma 24 horas`() {
        val uiState = viewModel.uiState.value
        assertEquals(24, uiState.totalAsignadas)
        assertTrue(uiState.esSumaValida)
        assertFalse(uiState.mostrarDialogoPermiso)
    }

    @Test
    fun `al cambiar horas de suenio se actualiza el total de asignadas`() {
        viewModel.cambiarHoras("suenio", -1) // 8 -> 7
        val uiState = viewModel.uiState.value

        assertEquals(7, uiState.horasSuenio)
        assertEquals(23, uiState.totalAsignadas)
        assertFalse(uiState.esSumaValida)
    }

    @Test
    fun `DailyUsageSummary calcula correctamente el total en horas`() {
        val summary = DailyUsageSummary(
            totalScreenTimeMinutes = 150, // 2.5 horas
            socialMediaTimeMinutes = 90,   // 1.5 horas
            topApps = emptyList()
        )

        assertEquals(2.5f, summary.totalScreenTimeHours, 0.01f)
        assertEquals(1.5f, summary.socialMediaTimeHours, 0.01f)
    }
}

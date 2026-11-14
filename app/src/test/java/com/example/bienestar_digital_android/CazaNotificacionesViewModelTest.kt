package com.example.bienestar_digital_android

import com.example.bienestar_digital_android.feature.cazanotificaciones.CazaNotificacionesItem
import com.example.bienestar_digital_android.feature.cazanotificaciones.CazaNotificacionesViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class CazaNotificacionesViewModelTest {

    private lateinit var viewModel: CazaNotificacionesViewModel

    @Before
    fun setUp() {
        viewModel = CazaNotificacionesViewModel()
    }

    @Test
    fun `estado inicial de vidas es 3 corazones`() {
        val state = viewModel.uiState.value
        assertEquals(3, state.lives)
        assertEquals(0, state.score)
    }

    @Test
    fun `click en notificacion de trampa resta 1 vida`() {
        val trampaItem = CazaNotificacionesItem(
            id = "1",
            remitente = "Red Social",
            mensaje = "Promoción de juego",
            tipo = "TRAMPA",
            puntos = -50
        )

        viewModel.onNotificationClicked(trampaItem)
        val state = viewModel.uiState.value

        assertEquals(2, state.lives)
    }

    @Test
    fun `perder las 3 vidas termina el juego`() {
        val trampaItem = CazaNotificacionesItem(
            id = "1",
            remitente = "Red Social",
            mensaje = "Promoción de juego",
            tipo = "TRAMPA",
            puntos = -50
        )

        viewModel.onNotificationClicked(trampaItem) // 3 -> 2
        viewModel.onNotificationClicked(trampaItem) // 2 -> 1
        viewModel.onNotificationClicked(trampaItem) // 1 -> 0

        val state = viewModel.uiState.value
        assertEquals(0, state.lives)
        assertTrue(state.isGameOver)
    }
}

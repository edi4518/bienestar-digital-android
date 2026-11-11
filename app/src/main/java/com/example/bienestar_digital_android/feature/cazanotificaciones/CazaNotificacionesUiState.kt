package com.example.bienestar_digital_android.feature.cazanotificaciones

data class CazaNotificacionesUiState(
    val score: Int = 0,
    val timeLeft: Int = 60, // Segundos de duración de la ronda
    val activeNotifications: List<CazaNotificacionesItem> = emptyList(),
    val isGameOver: Boolean = false,
    val isGameStarted: Boolean = false
)
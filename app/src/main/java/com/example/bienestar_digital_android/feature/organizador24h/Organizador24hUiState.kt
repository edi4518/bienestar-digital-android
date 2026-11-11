package com.example.bienestar_digital_android.feature.organizador24h

data class Organizador24hUiState (
    // Datos de Usuario de manera predeterminada según spec
    val horasSuenio: Int = 8,
    val horasEscuela: Int = 6,
    val horasPantalla: Int = 4,
    val horasDeporte: Int = 3,
    val horasComidas: Int = 3,
    val horasEstudio: Int = 0,
    val horasSocial: Int = 3,

    // Total de las horas del día
    val totalAsignadas: Int = 24,
    val esSumaValida: Boolean = true,

    // Pronóstico dinámico de batería, foco y cansancio
    val bateriaDiurna: Int = 85,
    val focoCognitivo: String = "Óptimo",
    val cansancioEstado: String = "Bajo",

    // Resultado de la simulación
    val estadoEquilibrioTexto: String = "Equilibrado",
    val alertaActiva: String? = null,
    val feedbackReflexivo: String? = null,
)
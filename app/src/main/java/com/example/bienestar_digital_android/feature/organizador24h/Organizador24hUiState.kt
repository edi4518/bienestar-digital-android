package com.example.bienestar_digital_android.feature.organizador24h

data class Organizador24hUiState (
    // Datos de Usuario de manera predeterminada
    val horasSuenio: Int = 8,
    val horasEscuela: Int = 5,
    val horasEstudio: Int = 3,
    val horasPantalla: Int =3,
    val horasSocial: Int = 3,
    val horasDeporte: Int = 2,

    // Total de las horas del dia
    val totalAsignadas: Int = 24,
    val esSumaValida: Boolean = true,

    // Pronostico dinamico de bateria, foco y animo
    val bateriaDiurna: Int = 85,
    val focoCognitivo: String = "Optimo",
    val cansancioEstado: String = "Bajo",

    // Resultado de la simulacion
    val estadoEquilibrioTexto: String = "Equilibrado",
    val alertaActiva: String? = null,
    val feedbackReflexivo: String ? = null,
)
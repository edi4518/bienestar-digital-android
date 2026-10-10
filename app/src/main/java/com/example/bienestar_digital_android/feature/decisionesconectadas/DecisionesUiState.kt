package com.example.bienestar_digital_android.feature.decisionesconectadas

import com.example.bienestar_digital_android.domain.model.ArquetipoFinal
import com.example.bienestar_digital_android.domain.model.Capitulo
import com.example.bienestar_digital_android.domain.model.FranjaDilema


data class DecisionesUiState(
    // Indica si se está cargando el capítulo
    val estaCargado: Boolean = true,

    // Estadísticas de salud digital del jugador (0 a 100)
    val energia: Int = 100,
    val animo: Int = 100,
    val foco: Int = 100,

    // Avance del juego
    val indiceCapituloActual: Int = 0,
    val capituloActual: Capitulo? = null,
    val indiceFranjaActual: Int = 0, // Va de 0 a 4 (5 franjas horarias por dia)

    // Modal formativo / reflexivo emergente
    val mostrarModalReflexivo: Boolean = false,
    val feedbackReflexivo: String = "",

    // Cierre de la jornada y arquetipo final desbloqueado
    val juegoTerminado: Boolean = false,
    val arquetipoFinal: ArquetipoFinal? = null
) {
    // Franja horaria que corresponde mostrar en pantalla ahora mismo
    val franjaActual: FranjaDilema?
        get() = capituloActual?.franjas?.getOrNull(indiceFranjaActual)
}
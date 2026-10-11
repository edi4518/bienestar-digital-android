package com.example.bienestar_digital_android.feature.organizador24h.Organizador24hViewModel

import androidx.lifecycle.ViewModel
import com.example.bienestar_digital_android.feature.organizador24h.Organizador24hUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class Organizador24hViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(Organizador24hUiState())
    val uiState: StateFlow<Organizador24hUiState> = _uiState.asStateFlow()

    // Actualizar el estado del ViewModel
    fun cambiarHoras(actividad: String, delta: Int){
        _uiState.update { currentState ->
            var suenio = currentState.horasSuenio
            var estudio = currentState.horasEstudio
            var pantalla = currentState.horasPantalla
            var social = currentState.horasSocial
            var deporte = currentState.horasDeporte
            var escuela = currentState.horasEscuela

            when (actividad) {
                "suenio" -> suenio = (suenio + delta).coerceIn(0, 24)
                "estudio" -> estudio = (estudio + delta).coerceIn(0, 24)
                "pantalla" -> pantalla = (pantalla + delta).coerceIn(0, 24)
                "social" -> social = (social + delta).coerceIn(0, 24)
                "deporte" -> deporte = (deporte + delta).coerceIn(0, 24)
                "escuela" -> escuela = (escuela + delta).coerceIn(0, 24)
            }

            val total = suenio + escuela + estudio + pantalla + social + deporte

            // Calculo dinamico de bateria, foco y animo
            val bateria = calcularBateria(suenio, pantalla)
            val foco = if (estudio >= 3 && suenio >= 7) "Optimo" else "En riesgo"
            val cansancio = if (suenio < 7 || pantalla >= 5) "Alto" else "Bajo"

            // Actualizar el estado del ViewModel
            currentState.copy(
                horasSuenio = suenio,
                horasEstudio = estudio,
                horasPantalla = pantalla,
                horasSocial = social,
                horasDeporte = deporte,
                totalAsignadas = total,
                esSumaValida = total == 24,
                bateriaDiurna = bateria,
                focoCognitivo = foco,
                cansancioEstado = cansancio
            )
        }
    }
    // Funcion para calcular la bateria
    private fun calcularBateria(suenio: Int, pantalla: Int): Int {
        var bateriaDiurna = suenio * 10
        if (pantalla > 3) bateriaDiurna -= (pantalla * 5)
        return bateriaDiurna.coerceIn(10, 100)
    }

    // Funcion para simular la jornada
    fun simularJornada() {
        _uiState.update { state ->
            val alerta: String?
            val reflex: String?

            when {
                state.horasPantalla >= 6 && state.horasSuenio < 6 -> {
                    alerta = "ALERTA: VAMPING CRÍTICO ACTIVADO"
                    reflex = "Fuente SAP/UNICEF: Quitar horas de sueño para navegar a oscuras fragmenta el descanso profundo y afecta tu memoria."
                }
                state.horasPantalla >= 4 && state.horasEstudio == 0 -> {
                    alerta = "ALERTA: PROCRASTINACIÓN COMPULSIVA"
                    reflex = "Fuente CHT: Postergar responsabilidades escolares traslada la ansiedad hacia la noche."
                }
                state.horasPantalla <= 2 && state.horasSuenio >= 8 -> {
                    alerta = "DÍA EQUILIBRADO: BIENESTAR LOGRADO"
                    reflex = "Fuente SAP: Acotar el ocio digital a menos de 2 horas protege la salud mental y previene la fatiga diurna."
                }
                else -> {
                    alerta = "ESTADO MODERADO"
                    reflex = "Buen trabajo, aunque puedes optimizar tus pausas digitales para recargar mejor energía."
                }
            }
            state.copy(
                alertaActiva = alerta,
                feedbackReflexivo = reflex,
                estadoEquilibrioTexto = if (state.horasPantalla <= 2) "Equilibrado" else "Desbalanceado"
            )
        }
    }
}
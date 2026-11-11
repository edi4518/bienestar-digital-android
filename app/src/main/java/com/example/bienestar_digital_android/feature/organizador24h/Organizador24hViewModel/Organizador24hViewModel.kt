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
    fun cambiarHoras(actividad: String, delta: Int) {
        _uiState.update { currentState ->
            var suenio = currentState.horasSuenio
            var escuela = currentState.horasEscuela
            var pantalla = currentState.horasPantalla
            var deporte = currentState.horasDeporte
            var comidas = currentState.horasComidas

            when (actividad) {
                "suenio", "sleep" -> suenio = (suenio + delta).coerceIn(0, 24)
                "escuela", "school" -> escuela = (escuela + delta).coerceIn(0, 24)
                "pantalla", "screens" -> pantalla = (pantalla + delta).coerceIn(0, 24)
                "deporte", "social", "sport" -> deporte = (deporte + delta).coerceIn(0, 24)
                "comidas", "leisure", "estudio" -> comidas = (comidas + delta).coerceIn(0, 24)
            }

            val total = suenio + escuela + pantalla + deporte + comidas

            // Calculo dinamico de bateria, foco y cansancio
            val bateria = when {
                suenio >= 8 && pantalla <= 3 -> 95
                suenio >= 7 && pantalla <= 5 -> 80
                suenio < 7 -> 45
                else -> 60
            }
            val foco = when {
                suenio >= 8 && pantalla <= 3 -> "Máximo"
                suenio >= 7 && pantalla <= 5 -> "Óptimo"
                suenio < 7 -> "Brumoso"
                else -> "Regular"
            }
            val cansancio = when {
                suenio >= 8 && pantalla <= 3 -> "Nulo"
                suenio >= 7 && pantalla <= 5 -> "Bajo"
                suenio < 7 -> "Alto"
                else -> "Medio"
            }

            currentState.copy(
                horasSuenio = suenio,
                horasEscuela = escuela,
                horasPantalla = pantalla,
                horasDeporte = deporte,
                horasComidas = comidas,
                horasSocial = deporte,
                totalAsignadas = total,
                esSumaValida = total == 24,
                bateriaDiurna = bateria,
                focoCognitivo = foco,
                cansancioEstado = cansancio
            )
        }
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
                state.horasPantalla >= 4 || state.horasSuenio < 8 -> {
                    alerta = "¡Ojo! Las pantallas están canibalizando tu tiempo de descanso profundo."
                    reflex = "Fuente SAP: Superar las 2 horas de pantallas recreativas diarias canibaliza horas de descanso REM y reduce la consolidación de la memoria escolar."
                }
                state.horasPantalla <= 2 -> {
                    alerta = "¡DÍA EQUILIBRADO: BIENESTAR LOGRADO!"
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
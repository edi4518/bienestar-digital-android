package com.example.bienestar_digital_android.data.repository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


object UserProgressRepository {

    data class EstadoBienestarGlobal(
        val energia: Int = 100,
        val foco: Int = 100,
        val animo: Int = 100,
        val narrativaCompletada: Boolean = false,
        val ultimoArquetipoNombre: String? = null,

        // Datos para reanudar la partida
        val numeroCapituloGuardado: Int = 1,
        val indiceFranjaGuardado: Int = 0,
        val partidaEnCurso: Boolean = false // false si termino o colapsó

    )

    private val _estadoGlobal = MutableStateFlow(EstadoBienestarGlobal())
    val estadoGlobal: StateFlow<EstadoBienestarGlobal> = _estadoGlobal.asStateFlow()

    fun actualizarDesdeNarrativa(
        energiaFinal: Int,
        focoFinal: Int,
        animoFinal: Int,
        arquetipoNombre: String?
    ) {
        _estadoGlobal.update {
            it.copy(
                energia = energiaFinal,
                foco = focoFinal,
                animo = animoFinal,
                narrativaCompletada = true,
                ultimoArquetipoNombre = arquetipoNombre
            )
        }
    }

    fun resetearProgresoInicial() {
        _estadoGlobal.update {
            EstadoBienestarGlobal(
                energia = 100,
                foco = 100,
                animo = 100,
                numeroCapituloGuardado = 1,
                indiceFranjaGuardado = 0,
                partidaEnCurso = true
            )
        }
    }

    fun guardarProgresoPartida(
        energiaActual: Int,
        focoActual: Int,
        animoActual: Int,
        numeroCapitulo: Int,
        indiceFranja: Int
    ) {
        _estadoGlobal.update {
            it.copy(
                energia = energiaActual,
                foco = focoActual,
                animo = animoActual,
                numeroCapituloGuardado = numeroCapitulo,
                indiceFranjaGuardado = indiceFranja,
                partidaEnCurso = true
            )
        }
    }

    fun finalizarPartidaNarrativa(
        energiaFinal: Int,
        focoFinal: Int,
        animoFinal: Int,
        arquetipoNombre: String?
    ) {
        _estadoGlobal.update {
            it.copy(
                energia = energiaFinal,
                foco = focoFinal,
                animo = animoFinal,
                narrativaCompletada = true,
                ultimoArquetipoNombre = arquetipoNombre,
                numeroCapituloGuardado = 1,
                indiceFranjaGuardado = 0,
                partidaEnCurso = false // Para que la próxima inicie una nueva partida nueva
            )

        }
    }


}



package com.example.bienestar_digital_android.data.repository

import com.example.bienestar_digital_android.data.local.DecisionesLocalDataSource
import com.example.bienestar_digital_android.domain.model.ArquetipoFinal
import com.example.bienestar_digital_android.domain.model.Capitulo
import com.example.bienestar_digital_android.domain.model.JuegoDecisionesData
import com.example.bienestar_digital_android.domain.model.ModoJornada
import kotlin.collections.find


class DecisionesRepository(
    private val localDataSource: DecisionesLocalDataSource
) {
    // Variable en memoria (Cache) para no leer el archivo en cada momento
    private var datosEnMemoria: JuegoDecisionesData? = null

    // Obtiene los datos cargados, si es la primera vez, lee el archivo
    private fun obtenerDatos(): JuegoDecisionesData {
        if (datosEnMemoria == null) {
            datosEnMemoria = localDataSource.cargarDatosJuego()
        }
        return datosEnMemoria!!
    }

    // Devuelve un capítulo especifico por su numero (del 1 al 7)

    fun obtenerCapitulo(numero: Int): Capitulo? {
        return obtenerDatos().capitulos.find { it.numero == numero }
    }

    // Devuelve la lista de modos de juego (Estándar, Exámenes, Batería al 10%)

    @Suppress("UNUSED") //
    fun obtenerModosJornada(): List<ModoJornada> {
        return obtenerDatos().modosJornada
    }

    // Devuelve todos los arquetipos para evaluar el final de la partida

    fun obtenerArquetipos(): List<ArquetipoFinal> {
        return obtenerDatos().arquetiposFinales
    }

}
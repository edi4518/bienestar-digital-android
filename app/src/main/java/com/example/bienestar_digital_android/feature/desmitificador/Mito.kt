package com.example.bienestar_digital_android.feature.desmitificador

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

data class Mito(
    val id: Int = 0,
    val categoria: String = "MITO POPULAR",
    val afirmacion: String = "",
    val esVerdad: Boolean = false,
    val evidenciaEmpirica: String = "",
    val xp: Int = 50
)

object MitoLoader {
    private val mitosFallback = listOf(
        Mito(
            id = 1,
            categoria = "MITO POPULAR #01",
            afirmacion = "Usar el celular justo antes de dormir no afecta en nada a la calidad del descanso nocturno.",
            esVerdad = false,
            evidenciaEmpirica = "Según estudios neurobiológicos, la luz azul de las pantallas inhibe hasta un 50% la segregación de melatonina, alterando el ritmo circadiano y retrasando la fase de sueño profundo.",
            xp = 50
        ),
        Mito(
            id = 2,
            categoria = "MITO POPULAR #02",
            afirmacion = "La multitarea digital mejora la agilidad mental y la velocidad para resolver problemas complejos.",
            esVerdad = false,
            evidenciaEmpirica = "Investigaciones de la Universidad de Stanford demuestran que la multitarea continua aumenta los errores hasta un 40% y sobrecarga la memoria de trabajo.",
            xp = 50
        ),
        Mito(
            id = 3,
            categoria = "MITO POPULAR #03",
            afirmacion = "El uso continuo del celular es un ocio inofensivo y los adolescentes podemos autorregularnos sin problema cuando queremos.",
            esVerdad = false,
            evidenciaEmpirica = "Según informes de UNICEF Argentina y estudios neurocognitivos, 1 de cada 2 adolescentes reporta uso problemático y el 24% intentó sin éxito reducir su tiempo de conexión debido al diseño persuasivo e infinito de los algoritmos.",
            xp = 50
        ),
        Mito(
            id = 4,
            categoria = "CIENCIA DIGITAL #04",
            afirmacion = "Activar el modo escala de grises en la pantalla disminuye significativamente el tiempo de uso involuntario del teléfono.",
            esVerdad = true,
            evidenciaEmpirica = "Estudios comportamentales confirman que al eliminar la estimulación de los colores neón, el cerebro reduce las descargas automáticas de dopamina.",
            xp = 50
        ),
        Mito(
            id = 5,
            categoria = "MITO POPULAR #05",
            afirmacion = "Tener el celular boca abajo en la mesa silenciado elimina por completo la distracción cognitiva.",
            esVerdad = false,
            evidenciaEmpirica = "El fenómeno conocido como 'Brain Drain' demuestra que la sola presencia física del teléfono inteligente cercano consume recursos de la memoria de trabajo.",
            xp = 50
        )
    )

    fun cargarMitosDesdeAssets(context: Context): List<Mito> {
        return try {
            val jsonString = context.assets.open("mitos.json").use { inputStream ->
                inputStream.bufferedReader().use { it.readText() }
            }
            if (jsonString.isBlank()) {
                mitosFallback
            } else {
                val listType = object : TypeToken<List<Mito>>() {}.type
                val parsedList: List<Mito>? = Gson().fromJson(jsonString, listType)
                if (parsedList.isNullOrEmpty()) mitosFallback else parsedList
            }
        } catch (e: Throwable) {
            e.printStackTrace()
            mitosFallback
        }
    }
}

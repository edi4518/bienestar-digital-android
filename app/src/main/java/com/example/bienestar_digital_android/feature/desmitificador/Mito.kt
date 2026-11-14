package com.example.bienestar_digital_android.feature.desmitificador

import android.content.Context
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken

data class Mito(
    val id: Int = 0,
    val categoria: String = "CARD-01",
    val afirmacion: String = "",
    val esVerdad: Boolean = false,
    @SerializedName("explicacion", alternate = ["evidenciaEmpirica"])
    val evidenciaEmpirica: String = "",
    val fuente: String = "CHT",
    val xp: Int = 100
)

object MitoLoader {
    private val mitosFallback = listOf(
        Mito(
            id = 1,
            categoria = "CARD-01 | CHT",
            afirmacion = "Responder mensajes de WhatsApp mientras resuelves la tarea de matemáticas no afecta tu rendimiento escolar.",
            esVerdad = false,
            evidenciaEmpirica = "Fuente CHT: El cerebro no procesa dos tareas complejas a la vez; realiza 'switch tasking', una alternancia rápida que aumenta hasta un 40% el tiempo necesario para terminar la tarea y genera más errores.",
            fuente = "CHT",
            xp = 100
        ),
        Mito(
            id = 2,
            categoria = "CARD-02 | CHT",
            afirmacion = "Tener el celular boca abajo sobre el escritorio mientras estudias consume parte de tu capacidad de atención.",
            esVerdad = true,
            evidenciaEmpirica = "Fuente CHT (Brain Drain): El cerebro gasta energía de forma inconsciente en inhibir la tentación de mirar la pantalla simplemente por saber que el dispositivo está al alcance visual.",
            fuente = "CHT",
            xp = 100
        ),
        Mito(
            id = 3,
            categoria = "CARD-03 | SAP",
            afirmacion = "Si pones la pantalla en modo noche o filtro cálido, puedes usar redes en la cama sin afectar tu calidad de sueño.",
            esVerdad = false,
            evidenciaEmpirica = "Fuente SAP: Aunque la luz cálida reduce el impacto, el estímulo cognitivo y la interacción con contenidos interactivos frenan la segregación natural de melatonina y mantienen el cerebro en alerta.",
            fuente = "SAP",
            xp = 100
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

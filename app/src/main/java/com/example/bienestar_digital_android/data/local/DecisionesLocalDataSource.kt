package com.example.bienestar_digital_android.data.local

import android.content.Context
import com.example.bienestar_digital_android.domain.model.JuegoDecisionesData
import com.google.gson.Gson

class DecisionesLocalDataSource (private val context: Context) {

    private  val gson = Gson()

    // Abre el JSON de assets y lo transforma automáticamente en nuestros moldes

    fun cargarDatosJuego(): JuegoDecisionesData {
        val jsonString = context.assets.open("decisiones_conectadas_data.json")
            .bufferedReader()
            .use { it.readText()
            }

        return gson.fromJson(jsonString, JuegoDecisionesData::class.java)
    }
}
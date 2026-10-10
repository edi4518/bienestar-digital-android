package com.example.bienestar_digital_android.domain.model

import com.google.gson.annotations.SerializedName

// Este es el molde principal que envuelve todo lo que tiene el JSON

data class JuegoDecisionesData(
    @SerializedName("modosJornada")
    val modosJornada: List<ModoJornada>,

    @SerializedName("eventosAleatorios")
    val eventosAleatorios: List<EventoAleatorio>,

    @SerializedName("capitulos")
    val capitulos: List<Capitulo>,

    @SerializedName("arquetiposFinales")
    val arquetiposFinales: List<ArquetipoFinal>
)

// Molde para los modos (Semana Estándar, Exámenes, Batería al 10%)

data class ModoJornada(
    @SerializedName("id")
    val id: String,

    @SerializedName("nombre")
    val nombre: String,

    @SerializedName("energiaInicial")
    val energiaInicial: Int,

    @SerializedName("animoInicial")
    val animoInicial: Int,

    @SerializedName("focoInicial")
    val focoInicial: Int,

    @SerializedName("descripcion")
    val descripcion: String
)

// Molde para los eventos sorpresa (como "El grupo Arde")

data class EventoAleatorio(
    @SerializedName("id")
    val id: String,

    @SerializedName("titulo")
    val titulo: String,

    @SerializedName("contextoNarrativo")
    val contextoNarrativo: String,

    @SerializedName("opcionA")
    val opcionA: OpcionDecision,

    @SerializedName("opcionB")
    val opcionB: OpcionDecision
)

// Molde para cada capítulo del juego

data class Capitulo(
    @SerializedName("numero")
    val numero: Int,

    @SerializedName("dia")
    val dia: String,

    @SerializedName("titulo")
    val titulo: String,

    @SerializedName("temaCentral")
    val temaCentral: String,

    @SerializedName("franjas")
    val franjas: List<FranjaDilema>
)

// Molde para cada momento del día (07:00 AM, 11:30 AM, etc.)

data class FranjaDilema(
    @SerializedName("franjaId")
    val franjaId: String,

    @SerializedName("horaLabel")
    val horaLabel: String,

    @SerializedName("etapaLabel")
    val etapaLabel: String,

    @SerializedName("estadoFomo")
    val estadoFomo: String = "FOMO ALTO",

    @SerializedName("appOrigen")
    val appOrigen: String = "DISCORD",

    @SerializedName("remitenteNotificacion")
    val remitenteNotificacion: String = "",

    @SerializedName("mensajeNotificacion")
    val mensajeNotificacion: String = "",

    @SerializedName("tiempoRelativo")
    val tiempoRelativo: String = "ahora",

    @SerializedName("contextoNarrativo")
    val contextoNarrativo: String,

    @SerializedName("opcionA")
    val opcionA: OpcionDecision,

    @SerializedName("opcionB")
    val opcionB: OpcionDecision

)


// Molde para cada una de las dos elecciones posibles

data class OpcionDecision(
    @SerializedName("texto")
    val texto: String,

    @SerializedName("deltaEnergia")
    val deltaEnergia: Int,

    @SerializedName("deltaAnimo")
    val deltaAnimo: Int,

    @SerializedName("deltaFoco")
    val deltaFoco: Int,

    @SerializedName("tagEfectoEco")
    val tagEfectoEco: String? = null,

    @SerializedName("feedbackReflexivo")
    val feedbackReflexivo: String

)


// Molde para la pantalla final que resume como te fue

data class ArquetipoFinal(
    @SerializedName("id")
    val id: String,

    @SerializedName("nombre")
    val nombre: String,

    @SerializedName("prioridad")
    val prioridad: Int,

    @SerializedName("condicion")
    val condicion: String,

    @SerializedName("descripcionNarrativa")
    val descripcionNarrativa: String,

    @SerializedName("tacticaNombre")
    val tacticaNombre: String,

    @SerializedName("tacticaDescripcion")
    val tacticaDescripcion: String
)

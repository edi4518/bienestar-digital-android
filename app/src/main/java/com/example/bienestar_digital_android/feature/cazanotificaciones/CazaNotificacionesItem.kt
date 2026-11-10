package com.example.bienestar_digital_android.feature.cazanotificaciones

import java.util.UUID

data class CazaNotificacionesItem(
    val id: String,
    val remitente: String,
    val mensaje: String,
    val tipo: String, // "PRIORITARIA" o "TRAMPA"
    val puntos: Int,
    var xPos: Float = 0f,
    var yPos: Float = 0f,
    val instanceId: String = UUID.randomUUID().toString() // Identificador único para controlar su tiempo de vida
)

data class CazaNotificacionesGameResponse(
    val notificaciones: List<CazaNotificacionesItem>
)


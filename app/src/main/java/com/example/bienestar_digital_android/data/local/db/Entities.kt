package com.example.bienestar_digital_android.data.local.db

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entidad que almacena el historial diario de auditorías de uso de pantalla y redes sociales.
 */
@Entity(tableName = "auditorias_diarias")
data class AuditoriaDiariaEntity(
    @PrimaryKey
    val fecha: String, // Formato YYYY-MM-DD
    val totalPantallaMinutos: Long,
    val redesSocialesMinutos: Long,
    val desconexionMinutos: Long,
    val metaPantallaHoras: Int,
    val metaCumplida: Boolean,
    val bateriaDiurna: Int,
    val timestamp: Long = System.currentTimeMillis()
)

/**
 * Entidad que almacena el perfil persistente del usuario.
 */
@Entity(tableName = "perfil_usuario")
data class UserProfileEntity(
    @PrimaryKey
    val id: Int = 1,
    val gamertag: String,
    val age: Int,
    val gender: String,
    val avatarResId: Int,
    val customAvatarUri: String? = null,
    val energia: Int = 100,
    val foco: Int = 100,
    val animo: Int = 100,
    val levelTitle: String = "Nivel 1 - Recluta Digital"
)

/**
 * Entidad que almacena récords y puntuaciones máximas en minijuegos.
 */
@Entity(tableName = "records_minijuegos")
data class MinijuegoRecordEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val juegoNombre: String, // "CazaNotificaciones" o "Desmitificador"
    val puntajeMaximo: Int,
    val aciertos: Int,
    val fechaIso: String,
    val timestamp: Long = System.currentTimeMillis()
)

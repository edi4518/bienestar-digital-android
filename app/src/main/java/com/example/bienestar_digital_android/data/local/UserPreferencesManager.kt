package com.example.bienestar_digital_android.data.local

import android.content.Context
import android.content.SharedPreferences
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class UserPreferencesManager(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("bienestar_user_prefs", Context.MODE_PRIVATE)

    /**
     * Indica si el usuario ya ha creado y guardado un perfil en la aplicación.
     */
    var haCompletadoPerfil: Boolean
        get() = prefs.getBoolean("ha_completado_perfil", false)
        set(value) = prefs.edit().putBoolean("ha_completado_perfil", value).apply()

    /**
     * Guarda el Gamertag/Nombre del usuario habitual.
     */
    var gamertagGuardado: String
        get() = prefs.getString("gamertag_guardado", "") ?: ""
        set(value) = prefs.edit().putString("gamertag_guardado", value).apply()

    /**
     * ID de avatar preset seleccionado.
     */
    var avatarResId: Int
        get() = prefs.getInt("avatar_res_id", 1)
        set(value) = prefs.edit().putInt("avatar_res_id", value).apply()

    /**
     * URI de la foto seleccionada desde la galería del teléfono.
     */
    var customAvatarUri: String?
        get() = prefs.getString("custom_avatar_uri", null)
        set(value) = prefs.edit().putString("custom_avatar_uri", value).apply()

    /**
     * Racha de días consecutivos que el usuario se conecta a la app.
     */
    var rachaDias: Int
        get() = prefs.getInt("racha_dias", 1)
        set(value) = prefs.edit().putInt("racha_dias", value).apply()

    /**
     * Última fecha en que el usuario abrió la aplicación (YYYY-MM-DD).
     */
    var ultimaFechaConexion: String
        get() = prefs.getString("ultima_fecha_conexion", "") ?: ""
        set(value) = prefs.edit().putString("ultima_fecha_conexion", value).apply()

    /**
     * Verifica y actualiza la racha de días consecutivos.
     */
    fun actualizarRachaConsecutiva(): Int {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val hoyStr = sdf.format(Date())
        val fechaUltima = ultimaFechaConexion

        if (fechaUltima.isBlank()) {
            rachaDias = 1
            ultimaFechaConexion = hoyStr
            return 1
        }

        if (fechaUltima == hoyStr) {
            return rachaDias
        }

        try {
            val dateHoy = sdf.parse(hoyStr)
            val dateUltima = sdf.parse(fechaUltima)
            if (dateHoy != null && dateUltima != null) {
                val diffInMillis = dateHoy.time - dateUltima.time
                val diffInDays = diffInMillis / (1000 * 60 * 60 * 24)

                if (diffInDays == 1L) {
                    val nuevaRacha = rachaDias + 1
                    rachaDias = nuevaRacha
                    ultimaFechaConexion = hoyStr
                    return nuevaRacha
                } else if (diffInDays > 1L) {
                    rachaDias = 1
                    ultimaFechaConexion = hoyStr
                    return 1
                }
            }
        } catch (_: Exception) {
            rachaDias = 1
            ultimaFechaConexion = hoyStr
        }
        return rachaDias
    }

    /**
     * Guarda la información del perfil del usuario de forma segura.
     */
    fun guardarPerfilUsuario(gamertag: String, avatarResId: Int = 1, customAvatarUri: String? = null) {
        val finalTag = gamertag.ifBlank { "Recluta" }
        prefs.edit()
            .putBoolean("ha_completado_perfil", true)
            .putString("gamertag_guardado", finalTag)
            .putInt("avatar_res_id", avatarResId)
            .putString("custom_avatar_uri", customAvatarUri)
            .apply()
    }

    /**
     * Elimina las preferencias para permitir crear otro perfil.
     */
    fun resetearPerfil() {
        prefs.edit()
            .remove("ha_completado_perfil")
            .remove("gamertag_guardado")
            .remove("avatar_res_id")
            .remove("custom_avatar_uri")
            .remove("racha_dias")
            .remove("ultima_fecha_conexion")
            .apply()
    }
}

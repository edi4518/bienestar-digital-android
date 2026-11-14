package com.example.bienestar_digital_android.data.local

import android.content.Context
import android.content.SharedPreferences

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
     * Guarda la información del perfil del usuario.
     */
    fun guardarPerfilUsuario(gamertag: String) {
        val finalTag = gamertag.ifBlank { "Recluta" }
        prefs.edit()
            .putBoolean("ha_completado_perfil", true)
            .putString("gamertag_guardado", finalTag)
            .apply()
    }

    /**
     * Elimina las preferencias para permitir crear otro perfil.
     */
    fun resetearPerfil() {
        prefs.edit()
            .remove("ha_completado_perfil")
            .remove("gamertag_guardado")
            .apply()
    }
}

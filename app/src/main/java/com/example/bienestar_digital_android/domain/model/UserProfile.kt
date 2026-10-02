package com.example.bienestar_digital_android.domain.model

/**
 * Opciones de género disponibles en el onboarding
 */
enum class GenderOption {
    FEMALE, MASCULINE, PREFER_NOT_TO_SAY
}

/**
 * Modelo de dominio que representa los datos persistidos del usuario
 */
data class UserProfile(
    val gamertag: String = "Alex_24",
    val age: Int = 16,
    val gender: GenderOption = GenderOption.MASCULINE,
    val avatarResId: Int = 1,
    val levelTitle: String = "Nivel 1 - Recluta Digital"
)
package com.example.bienestar_digital_android.feature.onboarding

import com.example.bienestar_digital_android.domain.model.GenderOption

/**
 * Estado inmutable que consume la pantalla (UI)
 */
data class OnboardingUiState(
    val gamertag: String = "Alex_24",
    val age: Int = 16,
    val gender: GenderOption = GenderOption.MASCULINE,
    val avatarResId: Int = 1,
    val levelTitle: String = "Nivel 1 - Recluta Digital",
    val isTargetAge: Boolean = true, // Valida si la edad está en el rango objetivo (13 a 16 años)
    val isFormValid: Boolean = true
)

/**
 * Eventos que la interfaz (UI) le dispara al ViewModel pasando sus parámetros
 */
sealed interface OnboardingEvent {
    data class OnGamertagChanged(val gamertag: String) : OnboardingEvent
    data class OnAgeChanged(val age: Int) : OnboardingEvent // Parámetro numérico de edad
    data class OnGenderSelected(val gender: GenderOption) : OnboardingEvent
    data class OnAvatarChanged(val avatarResId: Int) : OnboardingEvent
    object OnSubmitProfile : OnboardingEvent
}
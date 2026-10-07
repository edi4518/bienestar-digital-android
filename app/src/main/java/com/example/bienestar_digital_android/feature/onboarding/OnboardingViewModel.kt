package com.example.bienestar_digital_android.feature.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bienestar_digital_android.domain.model.UserProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class OnboardingViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(OnboardingUiState())
    val uiState: StateFlow<OnboardingUiState> = _uiState.asStateFlow()

    /**
     * Procesador central de eventos de la UI
     */
    fun onEvent(event: OnboardingEvent) {
        when (event) {
            is OnboardingEvent.OnGamertagChanged -> {
                val newGamertag = event.gamertag
                _uiState.update { currentState ->
                    currentState.copy(
                        gamertag = newGamertag,
                        isFormValid = newGamertag.isNotBlank()
                    )
                }
            }

            is OnboardingEvent.OnAgeChanged -> {
                // Recibe el parámetro de edad desde la pantalla
                val newAge = event.age.coerceIn(10, 99)
                _uiState.update { currentState ->
                    currentState.copy(
                        age = newAge,
                        isTargetAge = newAge in 13..16 // Rango objetivo de 13 a 16 años
                    )
                }
            }

            is OnboardingEvent.OnGenderSelected -> {
                _uiState.update { currentState ->
                    currentState.copy(gender = event.gender)
                }
            }

            is OnboardingEvent.OnAvatarChanged -> {
                _uiState.update { currentState ->
                    currentState.copy(avatarResId = event.avatarResId)
                }
            }

            is OnboardingEvent.OnSubmitProfile -> {
                if (_uiState.value.isFormValid) {
                    saveUserProfile()
                }
            }
        }
    }

    /**
     * Persistencia de datos del perfil
     */
    private fun saveUserProfile() {
        viewModelScope.launch {
            val userProfile = UserProfile(
                gamertag = _uiState.value.gamertag,
                age = _uiState.value.age,
                gender = _uiState.value.gender,
                avatarResId = _uiState.value.avatarResId,
                levelTitle = _uiState.value.levelTitle
            )
            // Guardar en DataStore / Repositorio antes de navegar al Hub
        }
    }
}
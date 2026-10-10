package com.example.bienestar_digital_android.feature.hub


sealed class HubUiEvent {

    // Clic en el avatar o nombre de usuario
    data object OnPerfilClick: HubUiEvent()

    // Clic en el botón "Continuar" de la tarjeta Narrativa
    data object OnContinuarNarrativaClick: HubUiEvent()

    // Clic en alguna de las mini misiones (recibe el Id para saber si fue Caza de Notis o Mito/Verdad)
    data class OnMiniMisionClick (val misionId: String) : HubUiEvent()

    // Clic en la tarjeta del Organizador de las 24 Horas
    data object OnOrganizadorClick : HubUiEvent()

    // Clic en la tarjeta de Data real / Tip información
    data object OnTipClick : HubUiEvent()


}
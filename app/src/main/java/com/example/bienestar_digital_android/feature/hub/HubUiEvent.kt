package com.example.bienestar_digital_android.feature.hub


sealed class HubUiEvent {

    // Click en el avatar o nombre de usuario
    data object OnPerfilClick: HubUiEvent()

    // Click en el boton "Contiinuar" de la tarjeta Narrativa
    data object OnContinuarNarrativaClick: HubUiEvent()

    // Click en alguna de las mini misiones (recibe el id para saber si fue Caza de Notis o Mito/Verdad)
    data class OnMiniMisionClick (val misionId: String) : HubUiEvent()

    // Click en la tarjeta del Orgenizador de las 24 Horas
    data object OnOrganizadorClick : HubUiEvent()

    // Click en la tarjeta de Data real / Tip informacion
    data object OnTipClick : HubUiEvent()


}
package com.example.bienestar_digital_android.core.common

sealed class Screen (val route: String) {
    data object Hub: Screen ("hub_screen")
    data object DecisionesConectadas: Screen("decisiones_conectadas_screen")
    data object CazaNotificaciones: Screen("caza_notificaciones_screen")
    data object Desmitificador: Screen("desmitificador_screen")
    data object Organizador24h: Screen("organizador_24h_screen")

}


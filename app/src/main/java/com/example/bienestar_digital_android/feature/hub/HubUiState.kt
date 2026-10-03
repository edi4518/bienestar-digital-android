package com.example.bienestar_digital_android.feature.hub

// Estado principal que consumira la pantalla

data class HubUiState(
    val isLoading: Boolean =false,
    val userName: String ="",
    val userRacha: Int = 0,
    val nivel: Int = 1,
    val stats: RadarStats = RadarStats(),
    val misionNarrativa: MissionNarrativaState  = MissionNarrativaState(),
    val miniMisiones: List<MiniMisionState> = emptyList(),
    val balance: Balance24HorasState = Balance24HorasState(),
    val tipDelDia: TipDataState = TipDataState()
)

// Métricas de energia, foco y ánimo (valores de 0 a 100)

data class RadarStats(
    val energia: Int = 0,
    val foco: Int = 0,
    val animo: Int = 0
)

// Tarjeta grande izquierda

data class MissionNarrativaState(
    val id: String = "",
    val titulo: String = "",
    val descripcion: String = "",
    val capituloActual: Int = 1,
    val totalCapitulos: Int = 1
)

data class MiniMisionState(
    val id: String = "",
    val categoriaTag: String = "",
    val titulo: String = "",
    val descripcion: String = "",
    val xpRecompensa: Int = 0
)

// Organizador 24 Horas

data class Balance24HorasState (
    val horasSuenio: Int = 0,
    val horasEstudio: Int = 0,
    val horasPantalla: Int = 0,
    val horasLibre: Int = 0,
    val esEquilibrado: Boolean = true
)

//Tarjeta inferior de Data Real

data class TipDataState (
    val fuente: String = "",
    val descripcion: String = ""
)
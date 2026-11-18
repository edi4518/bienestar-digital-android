package com.example.bienestar_digital_android.feature.organizador24h

import com.example.bienestar_digital_android.data.repository.AppUsageInfo

data class Organizador24hUiState (
    // Día seleccionado (0=L, 1=M, 2=MI, 3=J, 4=V, 5=S, 6=D)
    val diaSeleccionado: Int = 0,

    // Datos de Usuario de manera predeterminada según spec (para el día seleccionado)
    val horasSuenio: Int = 8,
    val horasEscuela: Int = 6,
    val horasPantalla: Int = 4,
    val horasDeporte: Int = 3,
    val horasComidas: Int = 3,
    val horasEstudio: Int = 0,
    val horasSocial: Int = 3,

    // Automatización con uso real
    val esPantallaAutomatica: Boolean = false,

    // Total de las horas del día
    val totalAsignadas: Int = 24,
    val esSumaValida: Boolean = true,

    // Pronóstico dinámico de batería, foco y cansancio
    val bateriaDiurna: Int = 85,
    val focoCognitivo: String = "Óptimo",
    val cansancioEstado: String = "Bajo",

    // Resultado de la simulación
    val estadoEquilibrioTexto: String = "Equilibrado",
    val alertaActiva: String? = null,
    val feedbackReflexivo: String? = null,

    // Datos del uso real del dispositivo
    val tienePermisoUso: Boolean = false,
    val usoRealPantallaMinutos: Long? = null,
    val usoRealRedesSocialesMinutos: Long? = null,
    val comparativaFeedback: String? = null,
    val mostrarDialogoPermiso: Boolean = false,

    // Modal de Auditoría Digital (24H)
    val mostrarModalAuditoria: Boolean = false,
    val redesPlaneadoMinutos: Long = 45L,
    val redesRealMinutos: Long = 0L,
    val ocioPlaneadoMinutos: Long = 60L,
    val ocioRealMinutos: Long = 0L,
    val estudioPlaneadoMinutos: Long = 180L,
    val estudioRealMinutos: Long = 0L,
    val desconexionPlaneadoMinutos: Long = 360L,
    val desconexionRealMinutos: Long = 0L,
    val feedbackSistemaModal: String = "",
    val redesDetalleApps: List<AppUsageInfo> = emptyList(),
    val estaRedesExpandido: Boolean = false
)

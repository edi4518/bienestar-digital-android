package com.example.bienestar_digital_android.feature.organizador24h.Organizador24hViewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bienestar_digital_android.data.repository.UsageStatsRepository
import com.example.bienestar_digital_android.feature.organizador24h.Organizador24hUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.example.bienestar_digital_android.data.repository.UserProgressRepository
import java.util.Calendar

class Organizador24hViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(Organizador24hUiState())
    val uiState: StateFlow<Organizador24hUiState> = _uiState.asStateFlow()

    // Actualizar el estado del ViewModel
    fun initData(context: Context) {
        val calendar = Calendar.getInstance()
        val hoyIndex = (calendar.get(Calendar.DAY_OF_WEEK) + 5) % 7
        seleccionarDia(context, hoyIndex)
    }

    fun seleccionarDia(context: Context, diaIndex: Int) {
        val prefs = context.getSharedPreferences("organizador24h_prefs", Context.MODE_PRIVATE)
        val suenio = prefs.getInt("suenio_$diaIndex", 8)
        val escuela = prefs.getInt("escuela_$diaIndex", 6)
        var pantalla = prefs.getInt("pantalla_$diaIndex", 4)
        val deporte = prefs.getInt("deporte_$diaIndex", 3)
        val comidas = prefs.getInt("comidas_$diaIndex", 3)

        var esPantallaAutomatica = false
        val calendar = Calendar.getInstance()
        val hoyIndex = (calendar.get(Calendar.DAY_OF_WEEK) + 5) % 7

        if (diaIndex == hoyIndex) {
            val repository = UsageStatsRepository(context)
            if (repository.hasUsageStatsPermission()) {
                val summary = repository.getTodayUsageSummary()
                val horasReales = (summary.totalScreenTimeMinutes / 60L).toInt()
                pantalla = horasReales.coerceIn(0, 24)
                esPantallaAutomatica = true
                prefs.edit().putInt("pantalla_$diaIndex", pantalla).apply()
            }
        }

        val total = suenio + escuela + pantalla + deporte + comidas
        
        _uiState.update { currentState ->
            val bateria = calcularBateria(suenio, pantalla)
            val foco = calcularFoco(suenio, pantalla)
            val cansancio = calcularCansancio(suenio, pantalla)

            currentState.copy(
                diaSeleccionado = diaIndex,
                esPantallaAutomatica = esPantallaAutomatica,
                horasSuenio = suenio,
                horasEscuela = escuela,
                horasPantalla = pantalla,
                horasDeporte = deporte,
                horasComidas = comidas,
                horasSocial = deporte,
                totalAsignadas = total,
                esSumaValida = total == 24,
                bateriaDiurna = bateria,
                focoCognitivo = foco,
                cansancioEstado = cansancio,
                alertaActiva = null,
                feedbackReflexivo = null
            )
        }
    }

    fun cambiarHoras(actividad: String, delta: Int) {
        cambiarHoras(null, actividad, delta)
    }

    fun cambiarHoras(context: Context?, actividad: String, delta: Int) {
        _uiState.update { currentState ->
            val totalActual = currentState.totalAsignadas
            if (delta > 0 && totalActual >= 24) return@update currentState

            var suenio = currentState.horasSuenio
            var escuela = currentState.horasEscuela
            var pantalla = currentState.horasPantalla
            var deporte = currentState.horasDeporte
            var comidas = currentState.horasComidas

            when (actividad) {
                "suenio", "sleep" -> suenio = (suenio + delta).coerceIn(0, 24)
                "escuela", "school" -> escuela = (escuela + delta).coerceIn(0, 24)
                "pantalla", "screens" -> if (!currentState.esPantallaAutomatica) pantalla = (pantalla + delta).coerceIn(0, 24)
                "deporte", "social", "sport" -> deporte = (deporte + delta).coerceIn(0, 24)
                "comidas", "leisure", "estudio" -> comidas = (comidas + delta).coerceIn(0, 24)
            }

            val total = suenio + escuela + pantalla + deporte + comidas

            // Guardar preferencias del día si el contexto no es nulo
            context?.getSharedPreferences("organizador24h_prefs", Context.MODE_PRIVATE)?.edit()?.apply {
                putInt("suenio_${currentState.diaSeleccionado}", suenio)
                putInt("escuela_${currentState.diaSeleccionado}", escuela)
                putInt("pantalla_${currentState.diaSeleccionado}", pantalla)
                putInt("deporte_${currentState.diaSeleccionado}", deporte)
                putInt("comidas_${currentState.diaSeleccionado}", comidas)
                apply()
            }

            // Calculo dinamico de bateria, foco y cansancio
            val bateria = calcularBateria(suenio, pantalla)
            val foco = calcularFoco(suenio, pantalla)
            val cansancio = calcularCansancio(suenio, pantalla)

            currentState.copy(
                horasSuenio = suenio,
                horasEscuela = escuela,
                horasPantalla = pantalla,
                horasDeporte = deporte,
                horasComidas = comidas,
                horasSocial = deporte,
                totalAsignadas = total,
                esSumaValida = total == 24,
                bateriaDiurna = bateria,
                focoCognitivo = foco,
                cansancioEstado = cansancio
            )
        }
    }

    private fun calcularBateria(suenio: Int, pantalla: Int): Int {
        return when {
            suenio >= 8 && pantalla <= 3 -> 95
            suenio >= 7 && pantalla <= 5 -> 80
            suenio < 7 -> 45
            else -> 60
        }
    }

    private fun calcularFoco(suenio: Int, pantalla: Int): String {
        return when {
            suenio >= 8 && pantalla <= 3 -> "Máximo"
            suenio >= 7 && pantalla <= 5 -> "Óptimo"
            suenio < 7 -> "Brumoso"
            else -> "Regular"
        }
    }

    private fun calcularCansancio(suenio: Int, pantalla: Int): String {
        return when {
            suenio >= 8 && pantalla <= 3 -> "Nulo"
            suenio >= 7 && pantalla <= 5 -> "Bajo"
            suenio < 7 -> "Alto"
            else -> "Medio"
        }
    }

    // Funcion para simular la jornada
    fun simularJornada() {
        _uiState.update { state ->
            val alerta: String?
            val reflex: String?

            when {
                state.horasPantalla >= 6 && state.horasSuenio < 6 -> {
                    alerta = "ALERTA: VAMPING CRÍTICO ACTIVADO"
                    reflex = "Fuente SAP/UNICEF: Quitar horas de sueño para navegar a oscuras fragmenta el descanso profundo y afecta tu memoria."
                }
                state.horasPantalla >= 4 || state.horasSuenio < 8 -> {
                    alerta = "¡Ojo! Las pantallas están canibalizando tu tiempo de descanso profundo."
                    reflex = "Fuente SAP: Superar las 2 horas de pantallas recreativas diarias canibaliza horas de descanso REM y reduce la consolidación de la memoria escolar."
                }
                state.horasPantalla <= 2 -> {
                    alerta = "¡DÍA EQUILIBRADO: BIENESTAR LOGRADO!"
                    reflex = "Fuente SAP: Acotar el ocio digital a menos de 2 horas protege la salud mental y previene la fatiga diurna."
                }
                else -> {
                    alerta = "ESTADO MODERADO"
                    reflex = "Buen trabajo, aunque puedes optimizar tus pausas digitales para recargar mejor energía."
                }
            }
            state.copy(
                alertaActiva = alerta,
                feedbackReflexivo = reflex,
                estadoEquilibrioTexto = if (state.horasPantalla <= 2) "Equilibrado" else "Desbalanceado"
            )
        }
    }

    /**
     * Verifica el uso real en el dispositivo y realiza la comparativa con el plan de 24h.
     */
    fun verificarYAnalizarUsoReal(context: Context) {
        val repository = UsageStatsRepository(context)

        if (!repository.hasUsageStatsPermission()) {
            _uiState.update {
                it.copy(
                    tienePermisoUso = false,
                    mostrarDialogoPermiso = true
                )
            }
            return
        }

        val summary = repository.getTodayUsageSummary()

        _uiState.update { state ->
            val realPantallaMin = summary.totalScreenTimeMinutes
            val planeadoPantallaMin = state.horasPantalla * 60L
            val planeadoRedesMin = (state.horasPantalla * 60L * 0.45f).toLong().coerceAtLeast(45L)
            val planeadoOcioMin = (state.horasPantalla * 60L * 0.35f).toLong().coerceAtLeast(45L)
            val planeadoEstudioMin = ((state.horasEscuela + state.horasEstudio) * 60L).coerceAtLeast(180L)
            val planeadoDesconexionMin = ((state.horasSuenio + state.horasDeporte + state.horasComidas) * 60L).coerceAtLeast(360L)

            val realRedesMin = summary.socialMediaTimeMinutes
            val realOcioMin = summary.streamingTimeMinutes
            val realEstudioMin = summary.productivityTimeMinutes
            val realDesconexionMin = summary.disconnectionTimeMinutes

            val difPantalla = realPantallaMin - planeadoPantallaMin
            val difRedes = realRedesMin - planeadoRedesMin

            val feedbackModal = when {
                difPantalla > 0 && difRedes > 0 -> {
                    val hP = difPantalla / 60
                    val mP = difPantalla % 60
                    val tP = if (hP > 0) "${hP}h ${mP}m" else "${mP}m"
                    val hR = difRedes / 60
                    val mR = difRedes % 60
                    val tR = if (hR > 0) "${hR}h ${mR}m" else "${mR}m"
                    "Detectamos ${tP} más de tiempo total en pantalla y ${tR} más de redes que lo planeado en tu organizador hoy. ⚠️"
                }
                difPantalla > 0 -> {
                    val hP = difPantalla / 60
                    val mP = difPantalla % 60
                    val tP = if (hP > 0) "${hP}h ${mP}m" else "${mP}m"
                    "Exceso detectado: Usaste la pantalla ${tP} más de la meta fijada en tu organizador hoy. ⚠️"
                }
                difRedes > 0 -> {
                    val hR = difRedes / 60
                    val mR = difRedes % 60
                    val tR = if (hR > 0) "${hR}h ${mR}m" else "${mR}m"
                    "Detectamos ${tR} más de redes sociales que lo planeado en tu organizador hoy. ⚠️"
                }
                else -> {
                    "¡Excelente! Mantuviste tu uso de pantalla y redes sociales dentro de lo planeado en tu organizador hoy. ✅"
                }
            }

            state.copy(
                tienePermisoUso = true,
                mostrarDialogoPermiso = false,
                mostrarModalAuditoria = true,
                usoRealPantallaMinutos = summary.totalScreenTimeMinutes,
                usoRealRedesSocialesMinutos = summary.socialMediaTimeMinutes,
                redesPlaneadoMinutos = planeadoRedesMin,
                redesRealMinutos = realRedesMin,
                ocioPlaneadoMinutos = planeadoOcioMin,
                ocioRealMinutos = realOcioMin,
                estudioPlaneadoMinutos = planeadoEstudioMin,
                estudioRealMinutos = realEstudioMin,
                desconexionPlaneadoMinutos = planeadoDesconexionMin,
                desconexionRealMinutos = realDesconexionMin,
                feedbackSistemaModal = feedbackModal,
                redesDetalleApps = summary.topApps.filter { it.isSocialMedia || it.usageTimeMinutes > 0 }
            )
        }
    }

    fun toggleExpandirRedes() {
        _uiState.update { it.copy(estaRedesExpandido = !it.estaRedesExpandido) }
    }

    fun solicitarPermisoUso(context: Context) {
        val repository = UsageStatsRepository(context)
        repository.openUsageAccessSettings()
        _uiState.update { it.copy(mostrarDialogoPermiso = false) }
    }

    fun ocultarDialogoPermiso() {
        _uiState.update { it.copy(mostrarDialogoPermiso = false) }
    }

    fun cerrarModalAuditoria() {
        _uiState.update { it.copy(mostrarModalAuditoria = false) }
    }

    /**
     * Sincroniza nuevamente el consumo real del dispositivo y recalcula los indicadores
     * globales de bienestar (Batería, Foco y Cansancio), impactando en el juego.
     */
    fun sincronizarYRecalcularHabitos(context: Context) {
        val repository = UsageStatsRepository(context)
        if (repository.hasUsageStatsPermission()) {
            val summary = repository.getTodayUsageSummary()
            val usoRealPantallaHs = summary.totalScreenTimeHours

            _uiState.update { state ->
                val metaPantalla = state.horasPantalla.toFloat()
                val exceso = usoRealPantallaHs - metaPantalla

                val nuevaBateria = when {
                    exceso <= 0 -> 95
                    exceso <= 1.5f -> 75
                    else -> 40
                }
                val nuevoFoco = when {
                    exceso <= 0 -> "Máximo"
                    exceso <= 1.5f -> "Regular"
                    else -> "Brumoso"
                }
                val nuevoCansancio = when {
                    exceso <= 0 -> "Bajo"
                    exceso <= 1.5f -> "Medio"
                    else -> "Alto"
                }

                state.copy(
                    bateriaDiurna = nuevaBateria,
                    focoCognitivo = nuevoFoco,
                    cansancioEstado = nuevoCansancio,
                    usoRealPantallaMinutos = summary.totalScreenTimeMinutes,
                    usoRealRedesSocialesMinutos = summary.socialMediaTimeMinutes,
                    mostrarModalAuditoria = false
                )
            }

            val current = _uiState.value
            UserProgressRepository.guardarProgresoPartida(
                energiaActual = current.bateriaDiurna,
                focoActual = if (current.focoCognitivo == "Máximo") 100 else 50,
                animoActual = if (current.cansancioEstado == "Bajo") 90 else 40,
                numeroCapitulo = 1,
                indiceFranja = 0
            )

            // Persistencia permanente en SQLite con Room (Ejecutada en hilo de E/S Dispatchers.IO para evitar cierres)
            viewModelScope.launch(kotlinx.coroutines.Dispatchers.IO) {
                try {
                    val db = com.example.bienestar_digital_android.data.local.db.BienestarDatabase.getDatabase(context)
                    val hoyFecha = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault()).format(java.util.Date())
                    val auditoria = com.example.bienestar_digital_android.data.local.db.AuditoriaDiariaEntity(
                        fecha = hoyFecha,
                        totalPantallaMinutos = summary.totalScreenTimeMinutes,
                        redesSocialesMinutos = summary.socialMediaTimeMinutes,
                        desconexionMinutos = summary.disconnectionTimeMinutes,
                        metaPantallaHoras = current.horasPantalla,
                        metaCumplida = (summary.totalScreenTimeHours <= current.horasPantalla),
                        bateriaDiurna = current.bateriaDiurna
                    )
                    db.auditoriaDao().guardarAuditoria(auditoria)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        } else {
            cerrarModalAuditoria()
        }
    }
}
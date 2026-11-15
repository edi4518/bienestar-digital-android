package com.example.bienestar_digital_android.feature.hub

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bienestar_digital_android.data.local.UserPreferencesManager
import com.example.bienestar_digital_android.data.repository.RankingRepository
import com.example.bienestar_digital_android.data.repository.UserProgressRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HubViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(HubUiState())
    val uiState: StateFlow<HubUiState> = _uiState.asStateFlow()

    private var rankingJob: Job? = null

    init {
        cargarDatasHub()
        observarProgresoGlobal()
        cargarRankingFirebase("Global")
    }

    private fun cargarDatasHub() {
        viewModelScope.launch {
            _uiState.update {
                HubUiState(
                    isLoading = false,
                    userName = "Alex",
                    userRacha = 3,
                    nivel = 2,
                    stats = RadarStats(
                        energia = UserProgressRepository.estadoGlobal.value.energia,
                        foco = UserProgressRepository.estadoGlobal.value.foco,
                        animo = UserProgressRepository.estadoGlobal.value.animo
                    ),
                    misionNarrativa = MissionNarrativaState(
                        id = "mision_narrativa_1",
                        titulo = "Decisiones Conectadas",
                        descripcion = "Cada elección nocturna cuenta.",
                        capituloActual = UserProgressRepository.estadoGlobal.value.numeroCapituloGuardado,
                        totalCapitulos = 7
                    ),
                    miniMisiones = listOf(
                        MiniMisionState(
                            id = "caza_notis",
                            categoriaTag = "REFLEJOS",
                            titulo = "Caza de Notis",
                            descripcion = "Filtrá las trampas del algoritmo.",
                            xpRecompensa = 30
                        ),
                        MiniMisionState(
                            id = "mito_verdad",
                            categoriaTag = "SWIPE & TRUTH",
                            titulo = "Mito o Verdad",
                            descripcion = "Desarmá fake trends rápido.",
                            xpRecompensa = 40
                        )
                    ),
                    balance = Balance24HorasState(
                        horasSuenio = 8,
                        horasEstudio = 6,
                        horasPantalla = 3,
                        horasLibre = 7,
                        esEquilibrado = true
                    ),
                    tipDelDia = TipDataState(
                        fuente = "DATA REAL  •  UNICEF 2024",
                        descripcion = "1 de cada 2 adolescentes reporta uso compulsivo de pantallas. Entrenar tus pausas recupera tu foco."
                    )
                )
            }
        }
    }

    private fun observarProgresoGlobal() {
        viewModelScope.launch {
            UserProgressRepository.estadoGlobal.collect { estado ->
                _uiState.update { actual ->
                    actual.copy(
                        stats = actual.stats.copy(
                            energia = estado.energia,
                            foco = estado.foco,
                            animo = estado.animo
                        ),
                        misionNarrativa = actual.misionNarrativa.copy(
                            capituloActual = estado.numeroCapituloGuardado
                        )
                    )
                }
            }
        }
    }

    fun setUserName(name: String) {
        if (name.isNotBlank()) {
            _uiState.update { it.copy(userName = name) }
        }
    }

    fun inicializarPreferenciasRacha(context: Context) {
        val prefs = UserPreferencesManager(context)
        val rachaCalculada = prefs.actualizarRachaConsecutiva()
        _uiState.update {
            it.copy(
                userRacha = rachaCalculada,
                userName = if (prefs.gamertagGuardado.isNotBlank()) prefs.gamertagGuardado else it.userName,
                avatarResId = prefs.avatarResId,
                customAvatarUri = prefs.customAvatarUri
            )
        }
    }

    fun abrirRankingDialog() {
        _uiState.update { it.copy(mostrarRankingDialog = true) }
        cargarRankingFirebase(_uiState.value.juegoFiltroRanking)
    }

    fun cerrarRankingDialog() {
        _uiState.update { it.copy(mostrarRankingDialog = false) }
    }

    fun cambiarFiltroRanking(filtro: String) {
        _uiState.update { it.copy(juegoFiltroRanking = filtro) }
        cargarRankingFirebase(filtro)
    }

    fun abrirEditarPerfilDialog() {
        _uiState.update { it.copy(mostrarEditarPerfilDialog = true) }
    }

    fun cerrarEditarPerfilDialog() {
        _uiState.update { it.copy(mostrarEditarPerfilDialog = false) }
    }

    fun actualizarPerfil(context: Context, nuevoNombre: String, avatarResId: Int, customAvatarUri: String?) {
        val finalNombre = nuevoNombre.ifBlank { "Recluta" }
        val prefs = UserPreferencesManager(context)
        prefs.guardarPerfilUsuario(finalNombre, avatarResId, customAvatarUri)

        _uiState.update {
            it.copy(
                userName = finalNombre,
                avatarResId = avatarResId,
                customAvatarUri = customAvatarUri,
                mostrarEditarPerfilDialog = false
            )
        }

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val db = com.example.bienestar_digital_android.data.local.db.BienestarDatabase.getDatabase(context)
                db.userProfileDao().guardarPerfil(
                    com.example.bienestar_digital_android.data.local.db.UserProfileEntity(
                        gamertag = finalNombre,
                        age = 20,
                        gender = "Masculino",
                        avatarResId = avatarResId,
                        customAvatarUri = customAvatarUri,
                        energia = _uiState.value.stats.energia,
                        foco = _uiState.value.stats.foco,
                        animo = _uiState.value.stats.animo
                    )
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun cargarRankingFirebase(filtro: String) {
        rankingJob?.cancel()
        rankingJob = viewModelScope.launch {
            RankingRepository.obtenerTop10Ranking(filtro).collect { items ->
                _uiState.update { it.copy(rankingList = items) }
            }
        }
    }

    fun onEvent(event: HubUiEvent) {
        when (event) {
            is HubUiEvent.OnContinuarNarrativaClick -> {}
            is HubUiEvent.OnMiniMisionClick -> {}
            is HubUiEvent.OnOrganizadorClick -> {}
            is HubUiEvent.OnPerfilClick -> {}
            is HubUiEvent.OnTipClick -> {}
        }
    }
}

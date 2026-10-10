package com.example.bienestar_digital_android.feature.hub

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HubViewModel: ViewModel() {

    // 1. Estado mutable privado (solo el Vier Model puede modificarlo)
    private val _uiState = MutableStateFlow(HubUiState(isLoading = true))

    // 2. Estado publico inmutable (la vista solo lo lee de forma reactiva)
    val uiState: StateFlow<HubUiState> = _uiState.asStateFlow()

    init {
        cargarDatasHub()
    }

    private fun cargarDatasHub() {
        viewModelScope.launch {
            // Simulamos la carga inicial con los datos del diseño
            _uiState.update {
                HubUiState (
                    isLoading = false,
                    userName = "Alex",
                    userRacha = 3,
                    nivel = 2,
                    stats = RadarStats(
                        energia = 75,
                        foco = 60,
                        animo = 85
                    ),
                    misionNarrativa = MissionNarrativaState(
                        id = "mision_narrativa_1",
                        titulo = "Decisiones Conectadas",
                        descripcion = "Cada elección nocturna cuenta.",
                        capituloActual = 2,
                        totalCapitulos = 4
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

    // Funcion para llamar el nombre puesto en la creacion de perfil
    fun setUserName(name: String) {
        if(name.isNotBlank()) {
            _uiState.update { it.copy(userName = name) }
        }
    }

    // 3. Receptor de eventos del usuario
    fun onEvent (event: HubUiEvent) {
        when (event) {
            is HubUiEvent.OnContinuarNarrativaClick -> {
                // Aqui mas adelante dispararemos la navegacion al capitulo 2
            }
            is HubUiEvent.OnMiniMisionClick -> {
                // event.misionId nos dice si tocó "caza_notis" o "mito_verdad"
            }
            is HubUiEvent.OnOrganizadorClick -> {
                // Navegacion a la pantalla del organizador 24h
            }
            is HubUiEvent.OnPerfilClick -> {
                // Navegacion al perfil
            }
            is HubUiEvent.OnTipClick -> {
                // Accion opcional para abrir ling/nota
            }
        }
    }

}
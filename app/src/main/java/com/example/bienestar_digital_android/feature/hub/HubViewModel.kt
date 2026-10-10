package com.example.bienestar_digital_android.feature.hub

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bienestar_digital_android.data.repository.UserProgressRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HubViewModel : ViewModel() {

    // 1. Estado mutable privado (solo el ViewModel puede modificarlo)
    private val _uiState = MutableStateFlow(HubUiState())

    // 2. Estado público inmutable (la vista solo lo lee de forma reactiva)
    val uiState: StateFlow<HubUiState> = _uiState.asStateFlow()

    // 1. Invocamos las funciones en el arranque
    init {
        cargarDatasHub()
        observarProgresoGlobal()

    }

    private fun cargarDatasHub() {
        viewModelScope.launch {
            // Simulamos la carga inicial con los datos del diseño
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

    /* OBSERVA EL ESTADO GLOBAL COMPARTIDO:
         Cuando el usuario juega "Decisiones Conectadas", termina su partida y pulsa
         "Volver al menú principal", esta corrutina recibe la emisión de UserProgressRepository
         y actualiza de inmediato el objeto `stats = RadarStats(...)` de la pantalla principal.
     */
    private fun observarProgresoGlobal() {
        // Escuchamos los cambios del repositorio global en tiempo real
        viewModelScope.launch {
            UserProgressRepository.estadoGlobal.collect { estado ->
                _uiState.update { actual ->
                    actual.copy(
                        stats = actual.stats.copy(
                            energia = estado.energia,
                            foco = estado.foco,
                            animo = estado.animo
                        )
                    )
                }
            }
        }
    }

    // Función para llamar el nombre puesto en la creación de perfil
    fun setUserName(name: String) {
        if(name.isNotBlank()) {
            _uiState.update { it.copy(userName = name) }
        }
    }

    // 3. Receptor de eventos del usuario
    fun onEvent (event: HubUiEvent) {
        when (event) {
            is HubUiEvent.OnContinuarNarrativaClick -> {
                // Aquí más adelante dispararemos la navegación al capítulo
            }

            is HubUiEvent.OnMiniMisionClick -> {
                // event.misionId nos dice si tocó "caza_notis" o "mito_verdad"
            }

            is HubUiEvent.OnOrganizadorClick -> {
                // Navegación a la pantalla del organizador 24 hs
            }

            is HubUiEvent.OnPerfilClick -> {
                // Navegación al perfil
            }

            is HubUiEvent.OnTipClick -> {
                // Acción opcional para abrir link/nota
            }
        }
    }
}
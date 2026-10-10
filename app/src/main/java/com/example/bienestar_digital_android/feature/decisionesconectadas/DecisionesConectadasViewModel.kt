package com.example.bienestar_digital_android.feature.decisionesconectadas

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bienestar_digital_android.data.local.DecisionesLocalDataSource
import com.example.bienestar_digital_android.data.repository.DecisionesRepository
import com.example.bienestar_digital_android.data.repository.UserProgressRepository
import com.example.bienestar_digital_android.domain.model.ArquetipoFinal
import com.example.bienestar_digital_android.domain.model.OpcionDecision
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


class DecisionesConectadasViewModel(
    private val repository: DecisionesRepository
) : ViewModel() {

    // Estado interno mutable (solo el ViewModel puede modificarlo)
    private val _uiState = MutableStateFlow(DecisionesUiState())

    // Estado público de solo lectura (la View de Compose se suscribe a este)
    val uiState: StateFlow<DecisionesUiState> = _uiState.asStateFlow()

    init {
        // Apenas nace el ViewModel, cargamos el primer día (Capítulo 1: Lunes)
        cargarProgresoOIniciar()
    }

    // 1. Cargar el capítulo y dejar los stats listos
    private fun cargarProgresoOIniciar() {
        val progreso = UserProgressRepository.estadoGlobal.value

        if (progreso.partidaEnCurso) {
            // Reanudamos la partida en el capítulo y franja donde la dejó, con sus estados
            val capituloGuardado = repository.obtenerCapitulo(progreso.numeroCapituloGuardado)
            _uiState.update { estadoPrevio ->
                estadoPrevio.copy(
                    estaCargado = true,
                    capituloActual = capituloGuardado,
                    indiceFranjaActual = progreso.indiceFranjaGuardado,
                    energia = progreso.energia,
                    animo = progreso.animo,
                    foco = progreso.foco,
                    mostrarModalReflexivo = false,
                    juegoTerminado = false,
                    arquetipoFinal = null
                )
            }
        } else {
            // Arranca de cero (Capitulo 1, franja 0 y todo al 100)
            iniciarCapituloNuevo(1)

        }

    }

    // Inicia un capítulo limpio desde 0
    fun iniciarCapituloNuevo(nuevoCapitulo: Int) {
        val capitulo = repository.obtenerCapitulo(nuevoCapitulo)
        _uiState.update { estadoPrevio ->
            estadoPrevio.copy(
                estaCargado = true,
                capituloActual = capitulo,
                indiceFranjaActual = 0,
                energia = 100,
                animo = 100,
                foco = 100,
                mostrarModalReflexivo = false,
                juegoTerminado = false,
                arquetipoFinal = null
            )
        }
    }


    // 2. Acción: El jugador tocó una opcion (A o B)
    fun seleccionarOpcion(opcion: OpcionDecision) {
        val estadoActual = _uiState.value

        // Calculamos los nuevos valores silenciosamente (restringidos entre 0 y 100)
        val nuevaEnergia = (estadoActual.energia + opcion.deltaEnergia).coerceIn(0, 100)
        val nuevoAnimo = (estadoActual.animo + opcion.deltaAnimo).coerceIn(0, 100)
        val nuevoFoco = (estadoActual.foco + opcion.deltaFoco).coerceIn(0, 100)

        // Verificamos si alguna estadística colapsó a 0
        if (nuevaEnergia <= 0 || nuevoAnimo <= 0 || nuevoFoco <= 0) {
            val arquetipoColapso = repository.obtenerArquetipos().find { it.id == "ARQ_COLAPSO" }

            _uiState.update {
                it.copy(
                    energia = nuevaEnergia,
                    animo = nuevoAnimo,
                    foco = nuevoFoco,
                    juegoTerminado = true,
                    arquetipoFinal = arquetipoColapso,
                    mostrarModalReflexivo = false
                )
            }

            // Cerramos la partida en el repositorio global para que no reanude acá
            UserProgressRepository.finalizarPartidaNarrativa(
                energiaFinal = nuevaEnergia,
                focoFinal = nuevoFoco,
                animoFinal = nuevoAnimo,
                arquetipoNombre = arquetipoColapso?.nombre
            )
            return
        }

        // Si no colapsó, abrimos la ventana reflexiva con el feedback formativo
        _uiState.update {
            it.copy(
                energia = nuevaEnergia,
                animo = nuevoAnimo,
                foco = nuevoFoco,
                mostrarModalReflexivo = true,
                feedbackReflexivo = opcion.feedbackReflexivo
            )
        }

        // Guardamos las estadísticas intermedias en el repositorio global
        UserProgressRepository.guardarProgresoPartida(
            energiaActual = nuevaEnergia,
            focoActual = nuevoFoco,
            animoActual = nuevoAnimo,
            numeroCapitulo = estadoActual.capituloActual?.numero ?: 1,
            indiceFranja = estadoActual.indiceFranjaActual
        )
    }

    // 3. Acción: El jugador tocó "Continuar jornada" en el modal
    fun avanzarSiguienteFranja() {
        val estadoActual = _uiState.value
        val totalFranjas = estadoActual.capituloActual?.franjas?.size ?: 0
        val siguienteIndiceFranja = estadoActual.indiceFranjaActual + 1

        if (siguienteIndiceFranja < totalFranjas) {
            // 1. Todavía quedan franjas en el día (ej.: pasar de Mañana a Tarde escolar)
            _uiState.update {
                it.copy(
                    indiceFranjaActual = siguienteIndiceFranja,
                    mostrarModalReflexivo = false
                )
            }
            // Guardamos el avance dentro del mismo día
            UserProgressRepository.guardarProgresoPartida(
                energiaActual = estadoActual.energia,
                focoActual = estadoActual.foco,
                animoActual = estadoActual.animo,
                numeroCapitulo = estadoActual.capituloActual?.numero ?: 1,
                indiceFranja = siguienteIndiceFranja
            )
        } else {
            // 2. Terminaron las franjas de este día. Consultamos si hay un siguiente capítulo.
            val numeroCapituloActual = estadoActual.capituloActual?.numero ?: 1
            val siguienteNumeroCapitulo = numeroCapituloActual + 1

            val proximoCapitulo = repository.obtenerCapitulo(siguienteNumeroCapitulo)

            if (proximoCapitulo != null) {
                // Avanzamos al siguiente día, reseteando el índice de franjas
                _uiState.update {
                    it.copy(
                        capituloActual = proximoCapitulo,
                        indiceFranjaActual = 0,
                        mostrarModalReflexivo = false
                    )
                }
                // Guardamos el nuevo día alcanzado en el repositorio global
                UserProgressRepository.guardarProgresoPartida(
                    energiaActual = estadoActual.energia,
                    focoActual = estadoActual.foco,
                    animoActual = estadoActual.animo,
                    numeroCapitulo = siguienteNumeroCapitulo,
                    indiceFranja = 0
                )
            } else {
                // 3. Se completaron las 5 franjas de los 7 capítulos
                val arquetipoObtenido = calcularArquetipoFinal(
                    energia = estadoActual.energia,
                    animo = estadoActual.animo,
                    foco = estadoActual.foco
                )
                _uiState.update {
                    it.copy(
                        mostrarModalReflexivo = false,
                        juegoTerminado = true,
                        arquetipoFinal = arquetipoObtenido
                    )
                }

                // Marcamos el final de la partida para resetear el ciclo y actualizar el Hub
                UserProgressRepository.finalizarPartidaNarrativa(
                    energiaFinal = estadoActual.energia,
                    focoFinal = estadoActual.foco,
                    animoFinal = estadoActual.animo,
                    arquetipoNombre = arquetipoObtenido?.nombre
                )
            }
        }
    }

    // 4. Lógica de prioridad para determinar el arquetipo alcanzado
    private fun calcularArquetipoFinal(energia: Int, animo: Int, foco: Int): ArquetipoFinal? {
        val arquetipos = repository.obtenerArquetipos()

        return when {
            // Prioridad 1: Colapso
            energia <= 0 || animo <= 0 || foco <= 0 -> {
                arquetipos.firstOrNull { it.id == "ARQ_COLAPSO" }
            }
            // Prioridad 2: Estratega Equilibrado
            energia >= 60 && animo >= 60 && foco >= 60 -> {
                arquetipos.firstOrNull { it.id == "ARQ_ESTRATEGA" }
            }
            // Prioridad 3: Atleta del Enfoque
            energia >= 50 && foco <= 75 -> {
                arquetipos.firstOrNull { it.id == "ARQ_ATLETA_FOCO" }
            }
            // Prioridad 4: Búho Nocturno Agotado
            energia <= 30 && foco <= 35 -> {
                arquetipos.firstOrNull { it.id == "ARQ_BUHO_NOCTURNO" }
            }
            // Prioridad 5: Navegante Ansioso
            animo <= 35 && foco <= 40 -> {
                arquetipos.firstOrNull { it.id == "ARQ_NAVEGANTE_ANSIOSO" }
            }
            //Por Descarte
            else -> {
                arquetipos.firstOrNull { it.id == "ARQ_ESTRATEGA" } ?: arquetipos.firstOrNull()
            }

        }

    }

    fun finalizarYGuardarProgreso() {
        val actual = _uiState.value
        UserProgressRepository.actualizarDesdeNarrativa(
            energiaFinal = actual.energia,
            focoFinal = actual.foco,
            animoFinal = actual.animo,
            arquetipoNombre = actual.arquetipoFinal?.nombre
        )
    }
}


class DecisionesViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DecisionesConectadasViewModel::class.java)) {
            val dataSource = DecisionesLocalDataSource(context.applicationContext)
            val repository = DecisionesRepository(dataSource)
            return DecisionesConectadasViewModel(repository) as T
        }
        throw IllegalArgumentException("Clase ViewModel desconocida")
    }
}
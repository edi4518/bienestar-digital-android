package com.example.bienestar_digital_android.feature.cazanotificaciones

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.io.InputStreamReader
import java.util.UUID
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

class CazaNotificacionesViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(CazaNotificacionesUiState())
    val uiState: StateFlow<CazaNotificacionesUiState> = _uiState.asStateFlow()

    private var fullNotificationPool: List<CazaNotificacionesItem> = emptyList()

    private var gameTimerJob: Job? = null
    private var spawnerJob: Job? = null

    fun loadGameAssets(context: Context) {
        try {
            val inputStream = context.assets.open("notificaciones_juego.json")
            val reader = InputStreamReader(inputStream)
            val gameResponse = Gson().fromJson(reader, CazaNotificacionesGameResponse::class.java)
            reader.close()

            if (gameResponse.notificaciones.isEmpty()) return
            fullNotificationPool = gameResponse.notificaciones
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun startGame() {
        if (fullNotificationPool.isEmpty()) return

        _uiState.value = CazaNotificacionesUiState(
            score = 0,
            timeLeft = 60, // Partida de 60 segundos
            activeNotifications = emptyList(),
            isGameOver = false,
            isGameStarted = true
        )

        startTimer()
        startSpawner()
    }

    private fun startTimer() {
        gameTimerJob = viewModelScope.launch {
            while (isActive) {
                delay(1.seconds)
                val currentTime = _uiState.value.timeLeft
                if (currentTime > 0) {
                    val nextTime = currentTime - 1
                    _uiState.update { it.copy(timeLeft = nextTime) }
                    if (nextTime == 0) {
                        endGame()
                        break
                    }
                }
            }
        }
    }

    private fun startSpawner() {
        spawnerJob?.cancel()
        spawnerJob = viewModelScope.launch {
            while (isActive) {
                spawnRandomNotification()

                //  LÓGICA DINÁMICA DE APARICIÓN:
                // Calculamos cuánto tiempo ha transcurrido (30 - timeLeft).
                // A menor tiempo restante, menor será el delay entre burbujas (aparecen más rápido).
                val timeLeft = _uiState.value.timeLeft
                val elapsedSeconds = (60 - timeLeft).coerceIn(0, 60)

                // Empieza spawneando cada 2.0s y se acelera progresivamente hasta cada 0.8s al final
                val dynamicDelay = (900L - (elapsedSeconds * 10L)).coerceAtLeast(250L)

                delay(dynamicDelay.milliseconds)
            }
        }
    }

    private fun spawnRandomNotification() {
        if (fullNotificationPool.isEmpty()) return
        val template = fullNotificationPool.random()
        val newItem = template.copy(
            xPos = (10..180).random().toFloat(),
            yPos = (20..220).random().toFloat(),
            instanceId = UUID.randomUUID().toString()
        )

        _uiState.update { state ->
            val updatedList = if (state.activeNotifications.size >= 5) {
                state.activeNotifications.drop(1) + newItem
            } else {
                state.activeNotifications + newItem
            }
            state.copy(activeNotifications = updatedList)
        }

        // ⚡ LÓGICA DINÁMICA DE EXPIRACIÓN:
        // A medida que el tiempo se agota, las burbujas desaparecen más rápido de la pantalla.
        val timeLeft = _uiState.value.timeLeft
        val elapsedSeconds = (60 - timeLeft).coerceIn(0, 60)

        // Empiezan durando 3.5 segundos y se reducen progresivamente hasta 1.5 segundos al final
        val dynamicExpiration = (3500L - (elapsedSeconds * 33L)).coerceAtLeast(1500L)

        viewModelScope.launch {
            delay(dynamicExpiration.milliseconds)
            handleNotificationExpiration(newItem.instanceId)
        }
    }

    private fun handleNotificationExpiration(instanceId: String) {
        _uiState.update { state ->
            val target = state.activeNotifications.find { it.instanceId == instanceId }
            // Si era una trampa y expira sin ser tocada, suma +50 puntos correctamente
            val reward = if (target?.tipo == "TRAMPA") 50 else 0
            val filteredList = state.activeNotifications.filter { it.instanceId != instanceId }

            state.copy(
                score = (state.score + reward).coerceAtLeast(0),
                activeNotifications = filteredList
            )
        }
    }

    fun onNotificationClicked(item: CazaNotificacionesItem) {
        _uiState.update { state ->
            val pointsToAdd = if (item.tipo == "PRIORITARIA") 100 else -50
            val newScore = state.score + pointsToAdd
            val filteredList = state.activeNotifications.filter { it.instanceId != item.instanceId }

            state.copy(
                score = newScore.coerceAtLeast(0),
                activeNotifications = filteredList
            )
        }
    }

    private fun endGame() {
        gameTimerJob?.cancel()
        spawnerJob?.cancel()
        _uiState.update { it.copy(isGameOver = true, activeNotifications = emptyList()) }
    }

    override fun onCleared() {
        gameTimerJob?.cancel()
        spawnerJob?.cancel()
    }
}
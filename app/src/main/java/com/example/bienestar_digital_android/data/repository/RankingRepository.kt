package com.example.bienestar_digital_android.data.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class RankingItem(
    val id: String = "",
    val gamertag: String = "Recluta",
    val puntaje: Int = 0,
    val juegoNombre: String = "Global",
    val fechaIso: String = "",
    val timestamp: Long = System.currentTimeMillis()
)

object RankingRepository {

    private const val TAG = "RankingRepository"
    private const val COLLECTION_RANKING = "ranking"

    /**
     * Guarda la puntuación del jugador en Cloud Firestore de Firebase.
     */
    fun guardarPuntajeFirebase(gamertag: String, juegoNombre: String, puntaje: Int) {
        if (puntaje <= 0) return

        val firestore = try {
            FirebaseFirestore.getInstance()
        } catch (e: Exception) {
            Log.e(TAG, "Error al inicializar Firestore: ${e.message}")
            null
        } ?: return

        val fechaActual = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val finalTag = gamertag.ifBlank { "Recluta" }

        val registro = hashMapOf(
            "gamertag" to finalTag,
            "juegoNombre" to juegoNombre,
            "puntaje" to puntaje,
            "fechaIso" to fechaActual,
            "timestamp" to System.currentTimeMillis()
        )

        firestore.collection(COLLECTION_RANKING)
            .add(registro)
            .addOnSuccessListener { docRef ->
                Log.d(TAG, "¡Puntuación guardada con éxito en Firebase! ID: ${docRef.id}")
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Error al guardar en Firebase: ${e.message}")
            }
    }

    /**
     * Consulta las puntuaciones más altas del ranking en tiempo real desde Firebase Firestore.
     * En el filtro GLOBAL agrupa por usuario y SUMA sus puntos totales acumulados.
     */
    fun obtenerTop10Ranking(juegoFiltro: String = "Global"): Flow<List<RankingItem>> = callbackFlow {
        val firestore = try {
            FirebaseFirestore.getInstance()
        } catch (e: Exception) {
            Log.e(TAG, "Error Firestore listener: ${e.message}")
            null
        }

        if (firestore == null) {
            trySend(emptyList())
            close()
            return@callbackFlow
        }

        val listenerRegistration = firestore.collection(COLLECTION_RANKING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e(TAG, "Error al escuchar Firestore ranking: ${error.message}")
                    trySend(emptyList())
                    return@addSnapshotListener
                }

                if (snapshot != null && !snapshot.isEmpty) {
                    val allItems = snapshot.documents.map { doc ->
                        RankingItem(
                            id = doc.id,
                            gamertag = doc.getString("gamertag") ?: "Recluta",
                            puntaje = doc.getLong("puntaje")?.toInt() ?: 0,
                            juegoNombre = doc.getString("juegoNombre") ?: "Global",
                            fechaIso = doc.getString("fechaIso") ?: "",
                            timestamp = doc.getLong("timestamp") ?: 0L
                        )
                    }

                    val sortedTop10 = if (juegoFiltro == "Global" || juegoFiltro.isBlank()) {
                        // En GLOBAL: Agrupa por usuario y SUMA todos los puntos acumulados de ambos minijuegos
                        allItems.groupBy { it.gamertag.uppercase(Locale.getDefault()) }
                            .map { (_, entries) ->
                                RankingItem(
                                    id = entries.first().id,
                                    gamertag = entries.first().gamertag,
                                    puntaje = entries.sumOf { it.puntaje },
                                    juegoNombre = "Global",
                                    fechaIso = entries.maxByOrNull { it.timestamp }?.fechaIso ?: "",
                                    timestamp = entries.maxOf { it.timestamp }
                                )
                            }
                            .sortedByDescending { it.puntaje }
                            .take(10)
                    } else {
                        // En Pestañas de Minijuegos específicos: Agrupa por usuario y toma su MEJOR marca en ese juego
                        allItems.filter { it.juegoNombre.equals(juegoFiltro, ignoreCase = true) }
                            .groupBy { it.gamertag.uppercase(Locale.getDefault()) }
                            .map { (_, entries) ->
                                RankingItem(
                                    id = entries.first().id,
                                    gamertag = entries.first().gamertag,
                                    puntaje = entries.maxOf { it.puntaje },
                                    juegoNombre = juegoFiltro,
                                    fechaIso = entries.maxByOrNull { it.puntaje }?.fechaIso ?: "",
                                    timestamp = entries.maxOf { it.timestamp }
                                )
                            }
                            .sortedByDescending { it.puntaje }
                            .take(10)
                    }

                    trySend(sortedTop10)
                } else {
                    trySend(emptyList())
                }
            }

        awaitClose { listenerRegistration.remove() }
    }
}

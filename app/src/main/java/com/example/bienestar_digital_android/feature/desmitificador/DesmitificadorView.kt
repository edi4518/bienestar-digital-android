package com.example.bienestar_digital_android.feature.desmitificador

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.PhonelinkLock
import androidx.compose.material.icons.filled.PriorityHigh
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.delay
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.roundToInt

// Paleta de Colores UI/UX Neón Modo Oscuro
private val BgCanvas = Color(0xFF0F0F14)
private val BgDarkCard = Color(0xFF1B1B26)
private val BorderDark = Color(0xFF2E2E3E)
private val AccentLime = Color(0xFFCCFF00) // Verde Neón
private val AccentPink = Color(0xFFFF3366) // Rosa Neón (Mito/Falso)
private val AccentCyan = Color(0xFF00F5D4) // Cyan (Verdad/Verdadero)
private val AccentPurple = Color(0xFFB8B0FF)

@Composable
fun DesmitificadorView(
    onNavigateBack: () -> Unit = {}
) {
    DesmitificadorScreen(onNavigateBack = onNavigateBack)
}

@Composable
fun DesmitificadorScreen(
    onNavigateBack: () -> Unit = {}
) {
    val context = LocalContext.current
    var mitos by remember(context) { mutableStateOf(MitoLoader.cargarMitosDesdeAssets(context).shuffled()) }

    var currentIndex by remember { mutableIntStateOf(0) }
    var aciertos by remember { mutableIntStateOf(0) }
    var totalXp by remember { mutableIntStateOf(0) }
    var tiempoRestante by remember { mutableIntStateOf(60) }
    var respuestaSeleccionada by remember { mutableStateOf<Boolean?>(null) }
    var juegoFinalizado by remember { mutableStateOf(false) }
    val historialResultados = remember { androidx.compose.runtime.mutableStateListOf<Boolean>() }

    // Temporizador de 60 segundos por sesión según especificación
    LaunchedEffect(juegoFinalizado) {
        if (!juegoFinalizado) {
            while (tiempoRestante > 0 && !juegoFinalizado) {
                delay(1000)
                tiempoRestante--
            }
            if (tiempoRestante <= 0 && !juegoFinalizado) {
                juegoFinalizado = true
            }
        } else if (totalXp > 0) {
            val prefs = com.example.bienestar_digital_android.data.local.UserPreferencesManager(context)
            val gamertag = prefs.gamertagGuardado.ifBlank { "Recluta" }
            com.example.bienestar_digital_android.data.repository.RankingRepository.guardarPuntajeFirebase(
                gamertag = gamertag,
                juegoNombre = "Desmitificador",
                puntaje = totalXp
            )
        }
    }

    val scrollState = rememberScrollState()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = BgCanvas
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 18.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            if (juegoFinalizado) {
                PantallaResultadosFinales(
                    aciertos = aciertos,
                    total = mitos.size,
                    totalXp = totalXp,
                    tiempoRestante = tiempoRestante,
                    onReiniciar = {
                        mitos = MitoLoader.cargarMitosDesdeAssets(context).shuffled()
                        currentIndex = 0
                        aciertos = 0
                        totalXp = 0
                        tiempoRestante = 60
                        respuestaSeleccionada = null
                        historialResultados.clear()
                        juegoFinalizado = false
                    },
                    onVolver = onNavigateBack
                )
            } else if (mitos.isNotEmpty() && currentIndex < mitos.size) {
                val mitoActual = mitos[currentIndex]

                Column {
                    // 1. Header (Atrás, Ronda, Tiempo Restante, XP y Barra Segmentada)
                    HeaderDesmitificador(
                        rondaActual = currentIndex + 1,
                        totalRondas = mitos.size,
                        tiempoRestante = tiempoRestante,
                        xp = totalXp,
                        historialResultados = historialResultados,
                        onNavigateBack = onNavigateBack
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // 2. Guía de Gestos
                    GuiaDeGestosHeader()

                    Spacer(modifier = Modifier.height(14.dp))

                    // 3. Tarjeta Central Interactiva
                    TarjetaMainMito(
                        mito = mitoActual,
                        respuestaSeleccionada = respuestaSeleccionada,
                        onRespuesta = { esVerdad ->
                            if (respuestaSeleccionada == null) {
                                respuestaSeleccionada = esVerdad
                                val esAcierto = (esVerdad == mitoActual.esVerdad)
                                historialResultados.add(esAcierto)
                                if (esAcierto) {
                                    aciertos++
                                    totalXp += mitoActual.xp
                                }
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // 4. Botones de Respuesta (MITO FALSO / VERDADERO)
                    BotonesDeRespuesta(
                        respuestaSeleccionada = respuestaSeleccionada,
                        onSeleccionarMito = {
                            if (respuestaSeleccionada == null) {
                                respuestaSeleccionada = false
                                val esAcierto = (!mitoActual.esVerdad)
                                historialResultados.add(esAcierto)
                                if (esAcierto) {
                                    aciertos++
                                    totalXp += mitoActual.xp
                                }
                            }
                        },
                        onSeleccionarVerdad = {
                            if (respuestaSeleccionada == null) {
                                respuestaSeleccionada = true
                                val esAcierto = (mitoActual.esVerdad)
                                historialResultados.add(esAcierto)
                                if (esAcierto) {
                                    aciertos++
                                    totalXp += mitoActual.xp
                                }
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // 5. Feedback Card (Evidencia Empírica y Siguiente Tarjeta)
                    AnimatedVisibility(
                        visible = respuestaSeleccionada != null,
                        enter = fadeIn() + slideInVertically { it / 2 },
                        exit = fadeOut()
                    ) {
                        respuestaSeleccionada?.let { seleccion ->
                            FeedbackCardEvidencia(
                                mito = mitoActual,
                                respuestaUsuario = seleccion,
                                onSiguienteTarjeta = {
                                    respuestaSeleccionada = null
                                    if (currentIndex + 1 < mitos.size) {
                                        currentIndex++
                                    } else {
                                        // Bonus por tiempo restante (+10 XP por segundo)
                                        val bonusTiempo = tiempoRestante * 10
                                        totalXp += bonusTiempo
                                        juegoFinalizado = true
                                    }
                                }
                            )
                        }
                    }
                }
            } else {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "Cargando minijuego...", color = Color.White)
                }
            }
        }
    }
}

@Composable
fun HeaderDesmitificador(
    rondaActual: Int,
    totalRondas: Int,
    tiempoRestante: Int,
    xp: Int,
    historialResultados: List<Boolean> = emptyList(),
    onNavigateBack: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Botón Volver Atrás
            IconButton(
                onClick = onNavigateBack,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF1E1E28))
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Volver",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }

            // Badge Ronda "TARJETA 03/15"
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(Color(0xFF1B1B26))
                    .border(1.dp, BorderDark, RoundedCornerShape(50))
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "TARJETA ",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF8E8E9F)
                    )
                    Text(
                        text = "${if (rondaActual < 10) "0$rondaActual" else rondaActual}/$totalRondas",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = AccentLime
                    )
                }
            }

            // Temporizador "60s"
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(Color(0xFF1B1B26))
                    .border(1.dp, BorderDark, RoundedCornerShape(50))
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Schedule,
                        contentDescription = "Tiempo",
                        tint = AccentCyan,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${tiempoRestante}s",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = AccentCyan
                    )
                }
            }

            // XP
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(Color(0xFF1B1B26))
                    .border(1.dp, BorderDark, RoundedCornerShape(50))
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = "XP",
                        tint = AccentLime,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "$xp XP",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = AccentLime
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Barra de Progreso Segmentada (Verde Acierto, Rojo Error o Gris Pendiente)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            for (i in 1..totalRondas) {
                val indexSegmento = i - 1
                val esAciertoPrevio = if (indexSegmento < historialResultados.size) historialResultados[indexSegmento] else null
                val esActual = i == rondaActual

                val colorSegmento = when {
                    esAciertoPrevio == true -> AccentLime // Verde Acierto
                    esAciertoPrevio == false -> AccentPink // Rojo/Rosa Error
                    esActual -> AccentLime.copy(alpha = 0.9f)
                    else -> Color(0xFF2A2A38) // Gris pendiente
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(colorSegmento)
                )
            }
        }
    }
}

@Composable
fun GuiaDeGestosHeader() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "← DESLIZA: MITO",
            fontSize = 11.sp,
            fontWeight = FontWeight.ExtraBold,
            color = AccentPink,
            letterSpacing = 0.8.sp
        )

        Text(
            text = "VERDAD: DESLIZA →",
            fontSize = 11.sp,
            fontWeight = FontWeight.ExtraBold,
            color = AccentCyan,
            letterSpacing = 0.8.sp
        )
    }
}

@Composable
fun TarjetaMainMito(
    mito: Mito,
    respuestaSeleccionada: Boolean?,
    onRespuesta: (Boolean) -> Unit
) {
    var offsetX by remember { mutableFloatStateOf(0f) }
    val swipeThreshold = 160f

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .offset { IntOffset(offsetX.roundToInt(), 0) }
            .graphicsLayer {
                rotationZ = offsetX * 0.03f
            }
            .pointerInput(mito.id) {
                if (respuestaSeleccionada == null) {
                    detectHorizontalDragGestures(
                        onDragEnd = {
                            if (offsetX > swipeThreshold) {
                                onRespuesta(true) // Deslizó a la derecha = VERDADERO
                            } else if (offsetX < -swipeThreshold) {
                                onRespuesta(false) // Deslizó a la izquierda = MITO
                            }
                            offsetX = 0f
                        },
                        onDragCancel = { offsetX = 0f },
                        onHorizontalDrag = { _, dragAmount ->
                            offsetX += dragAmount
                        }
                    )
                }
            },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = BgDarkCard),
        border = BorderStroke(
            width = 1.5.dp,
            brush = Brush.verticalGradient(
                colors = listOf(
                    BorderDark,
                    when {
                        offsetX > 40f -> AccentCyan.copy(alpha = 0.8f)
                        offsetX < -40f -> AccentPink.copy(alpha = 0.8f)
                        else -> BorderDark
                    }
                )
            )
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Fila Superior: Categoria Pill & Shield Icon
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(Color(0xFF2D2452))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(AccentPurple)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = mito.categoria.uppercase(),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = AccentPurple,
                            letterSpacing = 0.8.sp
                        )
                    }
                }

                Icon(
                    imageVector = Icons.Default.Shield,
                    contentDescription = null,
                    tint = Color(0xFF5E5E72),
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Ícono Gráfico Central (Celular Bloqueado con Alerta !)
            Box(
                modifier = Modifier.size(76.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .clip(RoundedCornerShape(18.dp))
                        .background(Color(0xFF252533)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.PhonelinkLock,
                        contentDescription = null,
                        tint = AccentLime,
                        modifier = Modifier.size(36.dp)
                    )
                }

                // Alerta ! en la esquina superior derecha
                Box(
                    modifier = Modifier
                        .size(22.dp)
                        .align(Alignment.TopEnd)
                        .clip(CircleShape)
                        .background(AccentPink),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.PriorityHigh,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(13.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Afirmación / Pregunta Principal
            Text(
                text = "“${mito.afirmacion}”",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center,
                lineHeight = 26.sp,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Subtítulo "¿MITO O REALIDAD CIENTÍFICA?"
            Text(
                text = "¿MITO O REALIDAD CIENTÍFICA?",
                fontSize = 11.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF8E8E9F),
                letterSpacing = 1.2.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Footer de la tarjeta
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "👆 Elegí una opción o deslizá",
                    fontSize = 11.sp,
                    color = Color(0xFF7E7E92)
                )

                Text(
                    text = "Ciencia Digital",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = AccentLime
                )
            }
        }
    }
}

@Composable
fun BotonesDeRespuesta(
    respuestaSeleccionada: Boolean?,
    onSeleccionarMito: () -> Unit,
    onSeleccionarVerdad: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Botón MITO (FALSO)
        Box(
            modifier = Modifier
                .weight(1f)
                .height(56.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(
                    if (respuestaSeleccionada == false) AccentPink.copy(alpha = 0.25f)
                    else Color(0xFF252532)
                )
                .border(
                    1.5.dp,
                    if (respuestaSeleccionada == false) AccentPink else BorderDark,
                    RoundedCornerShape(20.dp)
                )
                .clickable(enabled = respuestaSeleccionada == null) { onSeleccionarMito() },
            contentAlignment = Alignment.Center
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(26.dp)
                        .clip(CircleShape)
                        .background(AccentPink.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Mito",
                        tint = AccentPink,
                        modifier = Modifier.size(16.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "MITO (FALSO)",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
            }
        }

        // Botón VERDADERO
        Box(
            modifier = Modifier
                .weight(1f)
                .height(56.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(
                    if (respuestaSeleccionada == true) AccentCyan.copy(alpha = 0.25f)
                    else Color(0xFF252532)
                )
                .border(
                    1.5.dp,
                    if (respuestaSeleccionada == true) AccentCyan else BorderDark,
                    RoundedCornerShape(20.dp)
                )
                .clickable(enabled = respuestaSeleccionada == null) { onSeleccionarVerdad() },
            contentAlignment = Alignment.Center
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(26.dp)
                        .clip(CircleShape)
                        .background(AccentCyan.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Verdadero",
                        tint = AccentCyan,
                        modifier = Modifier.size(16.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "VERDADERO",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = AccentCyan
                )
            }
        }
    }
}

@Composable
fun FeedbackCardEvidencia(
    mito: Mito,
    respuestaUsuario: Boolean,
    onSiguienteTarjeta: () -> Unit
) {
    val esCorrecto = respuestaUsuario == mito.esVerdad

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = BgDarkCard),
        border = BorderStroke(
            width = 1.5.dp,
            color = if (esCorrecto) AccentLime else AccentPink
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            // Header del Feedback (Icono, Título y Badge +100 XP)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(if (!mito.esVerdad) AccentPink.copy(alpha = 0.2f) else AccentCyan.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (!mito.esVerdad) Icons.Default.Close else Icons.Default.Check,
                            contentDescription = null,
                            tint = if (!mito.esVerdad) AccentPink else AccentCyan,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Column {
                        Text(
                            text = if (!mito.esVerdad) "¡ES UN MITO!" else "¡ES VERDADERO!",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                        Text(
                            text = if (esCorrecto) "Respuesta correcta confirmada" else "¡Respuesta incorrecta!",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (esCorrecto) Color(0xFF8E8E9F) else AccentPink
                        )
                    }
                }

                // Badge +100 XP (Verde si acertó, Rojo/Rosa si erró)
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(if (esCorrecto) AccentLime else AccentPink.copy(alpha = 0.85f))
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = if (esCorrecto) "+${mito.xp} XP" else "+0 XP",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = if (esCorrecto) Color(0xFF0F0F14) else Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Subtítulo EVIDENCIA EMPÍRICA
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.MenuBook,
                    contentDescription = null,
                    tint = AccentLime,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "EVIDENCIA EMPÍRICA",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = AccentLime,
                    letterSpacing = 0.8.sp
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Texto de Evidencia Empírica recabada de las fuentes
            val evidenciaTexto = mito.evidenciaEmpirica.ifBlank {
                "Fuente SAP / OMS: Según investigaciones neurocognitivas sobre hábitos digitales, la estimulación continua de pantallas e interacción constante altera los patrones de atención y el descanso profundo."
            }

            Text(
                text = evidenciaTexto,
                fontSize = 12.sp,
                color = Color.White.copy(alpha = 0.95f),
                lineHeight = 16.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Botón Verde Neón Compacto "SIGUIENTE TARJETA →"
            Button(
                onClick = onSiguienteTarjeta,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(46.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AccentLime),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "SIGUIENTE TARJETA →",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF0F0F14),
                        letterSpacing = 0.5.sp
                    )
                }
            }
        }
    }
}

@Composable
fun PantallaResultadosFinales(
    aciertos: Int,
    total: Int,
    totalXp: Int,
    tiempoRestante: Int,
    onReiniciar: () -> Unit,
    onVolver: () -> Unit
) {
    val (arquetipoTitulo, diagnostico, tactica) = when (aciertos) {
        in 13..15 -> Triple(
            "ARQUETIPO: DESARMADOR ALGORÍTMICO",
            "Excelente nivel de discernimiento sobre las trampas de diseño persuasivo y el impacto cognitivo de los dispositivos.",
            "TÁCTICA DE AUTODEFENSA DIGITAL: ACTIVACIÓN DE LA ESCALA DE GRISES\n\nVe a Ajustes ➔ Accesibilidad ➔ Visión ➔ Corrección de color y activa la escala de grises. Esto neutraliza las recompensas visuales diseñadas para llamar tu atención y reduce la fricción de uso."
        )
        in 8..12 -> Triple(
            "ARQUETIPO: NAVEGANTE EN OBSERVACIÓN",
            "Reconoces los mitos más evidentes, pero aún mantienes algunas creencias erróneas sobre la multitarea digital y los efectos de las pantallas en el descanso.",
            "TÁCTICA DE AUTODEFENSA DIGITAL: CREACIÓN DE ZONAS DE FRICCIÓN\n\nEstablece el hábito de poner a cargar el teléfono celular fuera del dormitorio al menos 45 minutos antes de acostarte. Utiliza un reloj despertador analógico para evitar la tentación del uso nocturno (vamping)."
        )
        else -> Triple(
            "ARQUETIPO: PRESA DE LA HIPERCONEXIÓN",
            "Presentas una alta vulnerabilidad a las falacias populares de la tecnología, sobreestimando la capacidad de atención del cerebro y minimizando los efectos de la sobreestimulación.",
            "TÁCTICA DE AUTODEFENSA DIGITAL: DESACTIVACIÓN DE DISPARADORES VISUALES\n\nIngresa a Ajustes ➔ Notificaciones y deshabilita los globos de notificación o badges numéricos rojos. De esta forma, serás tú quien decida cuándo revisar las aplicaciones sin responder a impulsos visuales automáticos."
        )
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(96.dp)
                .clip(CircleShape)
                .background(AccentLime.copy(alpha = 0.15f))
                .border(2.dp, AccentLime, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.EmojiEvents,
                contentDescription = "Trofeo",
                tint = AccentLime,
                modifier = Modifier.size(52.dp)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "¡Desmitificación Completada!",
            fontSize = 22.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color.White,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Lograste $aciertos de $total respuestas correctas",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = AccentCyan
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Puntaje total: +$totalXp XP",
            fontSize = 14.sp,
            fontWeight = FontWeight.ExtraBold,
            color = AccentLime
        )

        Spacer(modifier = Modifier.height(18.dp))

        // Tarjeta de Arquetipo Resultante
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .background(Color(0xFF1B1B26))
                .border(1.5.dp, AccentLime, RoundedCornerShape(18.dp))
                .padding(16.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = arquetipoTitulo,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Black,
                    color = AccentLime
                )
                Text(
                    text = diagnostico,
                    fontSize = 12.sp,
                    color = Color.White,
                    lineHeight = 17.sp
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF12121A))
                        .border(1.dp, BorderDark, RoundedCornerShape(12.dp))
                        .padding(12.dp)
                ) {
                    Text(
                        text = tactica,
                        fontSize = 12.sp,
                        color = AccentCyan,
                        lineHeight = 17.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onReiniciar,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = AccentLime),
            shape = RoundedCornerShape(14.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = null,
                tint = Color(0xFF0F0F14)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "VOLVER A JUGAR",
                color = Color(0xFF0F0F14),
                fontWeight = FontWeight.ExtraBold,
                fontSize = 14.sp
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
            onClick = onVolver,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            border = BorderStroke(1.5.dp, BorderDark),
            shape = RoundedCornerShape(14.dp)
        ) {
            Text(
                text = "VOLVER AL HUB",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        }
    }
}

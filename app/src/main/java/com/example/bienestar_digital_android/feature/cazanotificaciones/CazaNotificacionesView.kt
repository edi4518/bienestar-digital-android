package com.example.bienestar_digital_android.feature.cazanotificaciones

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bienestar_digital_android.core.designsystem.*
import androidx.compose.ui.platform.LocalLocale
import com.example.bienestar_digital_android.R

@Composable
fun CazaNotificacionesTopBar(
    onBackClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onBackClick,
            modifier = Modifier
                .size(42.dp)
                .background(BgDarkCard, CircleShape)
                .border(1.dp, BorderDark, CircleShape)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Volver",
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .background(AccentCyan, CircleShape)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "MINIJUEGO DE ATENCIÓN",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = AccentCyan,
                    letterSpacing = 1.sp
                )
            }
            Text(
                text = "Caza de Notificaciones",
                fontSize = 17.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Box(
            modifier = Modifier
                .background(AccentLime, RoundedCornerShape(12.dp))
                .padding(horizontal = 10.dp, vertical = 5.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "⚡ RETO",
                fontSize = 12.sp,
                fontWeight = FontWeight.Black,
                color = Color.Black
            )
        }
    }
}

@Composable
fun CazaNotificacionesStatsBar(
    score: Int,
    timeLeft: Int,
    modifier: Modifier = Modifier
) {
    val minutes = timeLeft / 60
    val seconds = timeLeft % 60
    val timeFormatted = String.format(LocalLocale.current.platformLocale,"%02d:%02d", minutes, seconds)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .background(BgDarkCard, RoundedCornerShape(16.dp))
            .border(1.dp, BorderDark, RoundedCornerShape(16.dp))
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(AccentLime.copy(alpha = 0.2f), RoundedCornerShape(10.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = AccentLime,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(text = "PUNTAJE", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF94A3B8))
                    Text(text = "$score pts", fontSize = 16.sp, fontWeight = FontWeight.Black, color = AccentLime)
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "TIEMPO RESTANTE",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF94A3B8)
                    )
                    Text(
                        text = timeFormatted,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Black,
                        color = AccentCyan
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(AccentCyan.copy(alpha = 0.2f), RoundedCornerShape(10.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Schedule,
                        contentDescription = null,
                        tint = AccentCyan,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun CazaNotificacionesScreen(
    viewModel: CazaNotificacionesViewModel,
    onBackToMenu: () -> Unit
) {
    val context = LocalContext.current
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadGameAssets(context)
    }

    CazaNotificacionesContent(
        state = state,
        onStartClick = { viewModel.startGame() },
        onNotificationClick = { item -> viewModel.onNotificationClicked(item) },
        onBackClick = onBackToMenu
    )
}

@Composable
fun CazaNotificacionesContent(
    state: CazaNotificacionesUiState,
    onStartClick: () -> Unit,
    onNotificationClick: (CazaNotificacionesItem) -> Unit,
    onBackClick: () -> Unit
) {

    val context = LocalContext.current

    LaunchedEffect(state.isGameOver) {
        if (state.isGameOver) {
            SoundPlayer.playSound(context, R.raw.juego_terminado)
        }
    }

    val backgroundColor = Color(0xFF0E0E12)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 16.dp)
        ) {
            CazaNotificacionesTopBar(onBackClick = onBackClick)

            Spacer(modifier = Modifier.height(4.dp))

            CazaNotificacionesStatsBar(
                score = state.score,
                timeLeft = state.timeLeft
            )

            Spacer(modifier = Modifier.height(12.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 16.dp)
                    .background(BgDarkCard, RoundedCornerShape(22.dp))
                    .border(1.5.dp, BorderDark, RoundedCornerShape(22.dp))
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                if (!state.isGameStarted) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth().padding(16.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .background(AccentCyan.copy(alpha = 0.15f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.NotificationsActive,
                                contentDescription = null,
                                tint = AccentCyan,
                                modifier = Modifier.size(32.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Entrenamiento de Enfoque",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Atrapá las notificaciones prioritarias de familia y estudio (+100 pts), pero esquivá los cebos algorítmicos y redes sociales (-50 pts).",
                            fontSize = 13.sp,
                            color = Color.LightGray,
                            textAlign = TextAlign.Center,
                            lineHeight = 18.sp
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(AccentLime, RoundedCornerShape(14.dp))
                                .clickable { onStartClick() }
                                .padding(vertical = 14.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Comenzar reto ➔",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.Black
                            )
                        }
                    }
                } else if (state.isGameOver) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth().padding(16.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .background(AccentPink.copy(alpha = 0.15f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Warning,
                                contentDescription = null,
                                tint = AccentPink,
                                modifier = Modifier.size(32.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "¡Tiempo Finalizado!",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Puntaje Final: ${state.score} puntos",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = AccentLime
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = if (state.score >= 200) "¡Excelente resistencia al algoritmo! Mantuviste el foco intacto." else "Cuidado: el contenido basura fragmentó tu atención. ¡Volvé a intentarlo!",
                            fontSize = 13.sp,
                            color = Color.LightGray,
                            textAlign = TextAlign.Center,
                            lineHeight = 18.sp
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(AccentLime, RoundedCornerShape(14.dp))
                                .clickable { onStartClick() }
                                .padding(vertical = 12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Jugar de nuevo",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.Black
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, BorderDark, RoundedCornerShape(14.dp))
                                .clickable { onBackClick() }
                                .padding(vertical = 12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Volver al menú principal",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                } else {
                    // Lista vertical estilo bandeja de notificaciones o chat (sin superposición)
                    androidx.compose.foundation.lazy.LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        userScrollEnabled = false
                    ) {
                        items(
                            items = state.activeNotifications,
                            key = { it.instanceId }
                        ) { notif ->
                            val isPrioritaria = notif.tipo == "PRIORITARIA"
                            val cardBg = if (isPrioritaria) Color(0xFF1A2E26) else Color(0xFF2E1A1A)
                            val borderColor = if (isPrioritaria) AccentLime else AccentPink
                            val badgeColor = if (isPrioritaria) AccentLime else AccentPink

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(cardBg)
                                    .border(
                                        1.5.dp,
                                        borderColor.copy(alpha = 0.8f),
                                        RoundedCornerShape(16.dp)
                                    )
                                    .clickable {
                                        SoundPlayer.playSound(
                                            context,
                                            if (isPrioritaria) R.raw.click else R.raw.clickfallo
                                        )
                                        onNotificationClickedSafe(
                                            onNotificationClick,
                                            notif
                                        )
                                    }
                                    .padding(12.dp)
                            ) {
                                Column(modifier = Modifier.fillMaxWidth()) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = notif.remitente,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis,
                                            modifier = Modifier.weight(1f)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Box(
                                            modifier = Modifier
                                                .background(
                                                    badgeColor.copy(alpha = 0.2f),
                                                    RoundedCornerShape(6.dp)
                                                )
                                                .padding(horizontal = 6.dp, vertical = 2.dp)
                                        ) {
                                            Text(
                                                text = if (isPrioritaria) "+100 pts" else "-50 pts",
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Black,
                                                color = badgeColor
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = notif.mensaje,
                                        fontSize = 12.sp,
                                        color = Color(0xFFCBD5E1),
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis,
                                        lineHeight = 16.sp,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // 💡 CAJA DEL TIP
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .background(BgDarkCard, RoundedCornerShape(14.dp))
                    .border(1.dp, BorderDark, RoundedCornerShape(14.dp))
                    .padding(horizontal = 14.dp, vertical = 10.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "💡", fontSize = 12.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "TIP: Desactivar notificaciones no esenciales reduce la multitarea y el estrés.",
                        fontSize = 10.sp,
                        color = Color(0xFF64748B),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}



// Función auxiliar para mantener la signatura limpia del clic
private fun onNotificationClickedSafe(
    onClick: (CazaNotificacionesItem) -> Unit,
    item: CazaNotificacionesItem
) {
    onClick(item)
}

@Preview(showBackground = true, name = "Pantalla de Inicio")
@Composable
fun CazaNotificacionesStartPreview() {
    BienestardigitalandroidTheme {
        val dummyState = CazaNotificacionesUiState(isGameStarted = false)
        CazaNotificacionesContent(
            state = dummyState,
            onStartClick = {},
            onNotificationClick = {},
            onBackClick = {}
        )
    }
}

@Preview(showBackground = true, name = "Juego en Curso")
@Composable
fun CazaNotificacionesGamePreview() {
    BienestardigitalandroidTheme {
        val dummyState = CazaNotificacionesUiState(
            isGameStarted = true,
            score = 150,
            timeLeft = 22,
            activeNotifications = listOf(
                CazaNotificacionesItem("1", "Mamá", "Avisame cuando salgas del cole por favor.", "PRIORITARIA", 100, 10f, 20f),
                CazaNotificacionesItem("2", "Feed Algoritmo", "¡12 nuevos reels recomendados para vos!", "TRAMPA", -50, 20f, 150f)
            )
        )
        CazaNotificacionesContent(
            state = dummyState,
            onStartClick = {},
            onNotificationClick = {},
            onBackClick = {}
        )
    }
}
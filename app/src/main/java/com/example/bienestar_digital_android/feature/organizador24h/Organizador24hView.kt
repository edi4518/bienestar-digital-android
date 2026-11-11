package com.example.bienestar_digital_android.feature.organizador24h

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.DirectionsRun
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bienestar_digital_android.core.designsystem.BienestardigitalandroidTheme

private val BackgroundColor = Color(0xFF0E0E12)
private val SurfaceContainer = Color(0xFF1F1F23)
private val SurfaceLowest = Color(0xFF131317)
private val SurfaceContainerHighest = Color(0xFF353439)
private val PrimaryFixed = Color(0xFFC3F400)
private val OnPrimaryFixed = Color(0xFF161E00)
private val SecondaryFixed = Color(0xFF26FEDC)
private val OnSecondary = Color(0xFF00382F)
private val PurpleColor = Color(0xFF6A0BAA)
private val PurpleText = Color(0xFFE0B6FF)
private val RedError = Color(0xFFFF5964)
private val OnErrorText = Color(0xFF690005)
private val GrayText = Color(0xFF8E9379)
private val TextOnSurface = Color(0xFFE4E1E7)
private val SurfaceBright = Color(0xFF39393D)

@Composable
fun Organizador24hScreen(
    uiState: Organizador24hUiState,
    onCambiarHoras: (String, Int) -> Unit,
    onSimularClick: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundColor)
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. TOP HUD: Nivel & Presupuesto
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(SurfaceContainer, CircleShape)
                        .clickable { onBackClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Volver al menú",
                        tint = TextOnSurface,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Row(
                    modifier = Modifier
                        .background(SurfaceContainer, RoundedCornerShape(9999.dp))
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Tune,
                        contentDescription = null,
                        tint = PrimaryFixed,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = "NIVEL 1: DÍA DE SEMANA",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextOnSurface
                    )
                }
            }

            val isValida = uiState.esSumaValida
            val chipBg = if (isValida) PrimaryFixed else SurfaceBright
            val chipTextColor = if (isValida) OnPrimaryFixed else TextOnSurface

            Row(
                modifier = Modifier
                    .background(chipBg, RoundedCornerShape(9999.dp))
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Timelapse,
                    contentDescription = null,
                    tint = chipTextColor,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = "${uiState.totalAsignadas}H / 24H ASIGNADAS",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = chipTextColor
                )
            }
        }

        // 2. HERO: Barra de Tiempo de 24 Horas Segmentada
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(SurfaceContainer, RoundedCornerShape(16.dp))
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Schedule,
                        contentDescription = null,
                        tint = SecondaryFixed,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = "LÍNEA DE 24 HORAS",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextOnSurface
                    )
                }
                Text(
                    text = "00:00 - 23:59",
                    fontSize = 11.sp,
                    color = GrayText
                )
            }

            // Escudo de Sueño: Banner
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0x4D6A0BAA), RoundedCornerShape(12.dp))
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Shield,
                        contentDescription = null,
                        tint = PurpleText,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = buildAnnotatedString {
                            append("Escudo de Sueño: ")
                            withStyle(style = SpanStyle(color = PurpleText, fontWeight = FontWeight.Bold)) {
                                append("8h Recomendadas")
                            }
                            append(" (SAP)")
                        },
                        fontSize = 11.sp,
                        color = TextOnSurface
                    )
                }

                Box(
                    modifier = Modifier
                        .background(SurfaceLowest, RoundedCornerShape(9999.dp))
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "PROTEGIDO",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = PurpleText
                    )
                }
            }

            // Pista visual de 24 horas
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(SurfaceLowest, RoundedCornerShape(12.dp))
                    .padding(4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(28.dp)
                        .clip(RoundedCornerShape(8.dp))
                ) {
                    if (uiState.horasSuenio > 0) {
                        Box(
                            modifier = Modifier
                                .weight(uiState.horasSuenio.toFloat())
                                .fillMaxHeight()
                                .background(PurpleColor),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Bedtime,
                                contentDescription = null,
                                tint = TextOnSurface,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                    if (uiState.horasEscuela > 0) {
                        Box(
                            modifier = Modifier
                                .weight(uiState.horasEscuela.toFloat())
                                .fillMaxHeight()
                                .background(SecondaryFixed),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.School,
                                contentDescription = null,
                                tint = OnSecondary,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                    if (uiState.horasDeporte > 0) {
                        Box(
                            modifier = Modifier
                                .weight(uiState.horasDeporte.toFloat())
                                .fillMaxHeight()
                                .background(PrimaryFixed),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.DirectionsRun,
                                contentDescription = null,
                                tint = OnPrimaryFixed,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                    if (uiState.horasPantalla > 0) {
                        Box(
                            modifier = Modifier
                                .weight(uiState.horasPantalla.toFloat())
                                .fillMaxHeight()
                                .background(RedError),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Smartphone,
                                contentDescription = null,
                                tint = OnErrorText,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                    if (uiState.horasComidas > 0) {
                        Box(
                            modifier = Modifier
                                .weight(uiState.horasComidas.toFloat())
                                .fillMaxHeight()
                                .background(SurfaceBright),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Restaurant,
                                contentDescription = null,
                                tint = TextOnSurface,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                }

                // Marcas de tiempo de referencia
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    listOf("00h", "06h", "12h", "18h", "24h").forEach { label ->
                        Text(text = label, fontSize = 10.sp, color = GrayText)
                    }
                }
            }

            // Banner de Alerta Dinámica
            if (uiState.alertaActiva != null) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0x6693000A), RoundedCornerShape(12.dp))
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = null,
                        tint = RedError,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = uiState.alertaActiva,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextOnSurface
                    )
                }
            }
        }

        // 3. MICRO-DASHBOARD: Pronóstico de Consecuencias (3 Columnas)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(SurfaceContainer, RoundedCornerShape(16.dp))
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "PRONÓSTICO PARA MAÑANA",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = GrayText
                )
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(PrimaryFixed, CircleShape)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                PronosticoCardSpec(
                    titulo = "Batería Diurna",
                    valor = "${uiState.bateriaDiurna}%",
                    colorValor = PrimaryFixed,
                    icon = Icons.Default.Bolt,
                    modifier = Modifier.weight(1f)
                )
                PronosticoCardSpec(
                    titulo = "Foco Cognitivo",
                    valor = uiState.focoCognitivo,
                    colorValor = SecondaryFixed,
                    icon = Icons.Default.Psychology,
                    modifier = Modifier.weight(1f)
                )
                PronosticoCardSpec(
                    titulo = "Cansancio",
                    valor = uiState.cansancioEstado,
                    colorValor = PurpleText,
                    icon = Icons.Default.SentimentNeutral,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // 4. MAZO DE ASIGNACIÓN: Stepper Cards
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "AJUSTÁ TU DISTRIBUCIÓN",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = GrayText
                )
                Text(
                    text = "1 bloque = 1 hora",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryFixed
                )
            }

            // Tarjeta 1: Sueño
            TarjetaAsignacionItem(
                titulo = "Sueño y Descanso",
                subtitulo = "Recomendado SAP: 8h - 10h",
                subtituloColor = PurpleText,
                horas = uiState.horasSuenio,
                icon = Icons.Default.Bedtime,
                iconBg = Color(0x4D6A0BAA),
                iconColor = PurpleText,
                onMinus = { onCambiarHoras("suenio", -1) },
                onPlus = { onCambiarHoras("suenio", 1) }
            )

            // Tarjeta 2: Escuela (Fijo)
            TarjetaAsignacionItem(
                titulo = "Escuela y Tareas",
                subtitulo = "Horas fijas obligatorias",
                subtituloColor = GrayText,
                horas = uiState.horasEscuela,
                icon = Icons.Default.School,
                iconBg = Color(0x3300DFC1),
                iconColor = SecondaryFixed,
                esFijo = true,
                fijoLockIcon = true
            )

            // Tarjeta 3: Redes y Pantallas (Zona crítica)
            TarjetaAsignacionItem(
                titulo = "Redes y Pantallas",
                subtitulo = "Zona crítica de ocio",
                subtituloColor = RedError,
                horas = uiState.horasPantalla,
                horasColor = RedError,
                icon = Icons.Default.Smartphone,
                iconBg = Color(0x4DFF5964),
                iconColor = RedError,
                onMinus = { onCambiarHoras("pantalla", -1) },
                onPlus = { onCambiarHoras("pantalla", 1) }
            )

            // Tarjeta 4: Deporte y Amigos
            TarjetaAsignacionItem(
                titulo = "Deporte y Amigos",
                subtitulo = "Conexión real",
                subtituloColor = PrimaryFixed,
                horas = uiState.horasDeporte,
                icon = Icons.Default.SportsVolleyball,
                iconBg = Color(0x33C3F400),
                iconColor = PrimaryFixed,
                onMinus = { onCambiarHoras("deporte", -1) },
                onPlus = { onCambiarHoras("deporte", 1) }
            )

            // Tarjeta 5: Comidas y Desconexión
            TarjetaAsignacionItem(
                titulo = "Comidas y Desconexión",
                subtitulo = "Rutina familiar y comidas",
                subtituloColor = GrayText,
                horas = uiState.horasComidas,
                icon = Icons.Default.Restaurant,
                iconBg = SurfaceBright,
                iconColor = TextOnSurface,
                onMinus = { onCambiarHoras("comidas", -1) },
                onPlus = { onCambiarHoras("comidas", 1) }
            )
        }

        // 5. BOTÓN CTA & EVIDENCIA CIENTÍFICA
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .background(PrimaryFixed, RoundedCornerShape(9999.dp))
                    .clickable { onSimularClick() },
                contentAlignment = Alignment.Center
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "COMPROBAR EQUILIBRIO",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = OnPrimaryFixed
                    )
                    Icon(
                        imageVector = Icons.Default.Bolt,
                        contentDescription = null,
                        tint = OnPrimaryFixed,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }

            if (uiState.feedbackReflexivo != null) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(SurfaceContainer, RoundedCornerShape(12.dp))
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Verified,
                        contentDescription = null,
                        tint = PrimaryFixed,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = uiState.feedbackReflexivo,
                        fontSize = 12.sp,
                        color = TextOnSurface
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF1B1B1F), RoundedCornerShape(12.dp))
                    .padding(12.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.Top
            ) {
                Icon(
                    imageVector = Icons.Default.Lightbulb,
                    contentDescription = null,
                    tint = PrimaryFixed,
                    modifier = Modifier
                        .size(18.dp)
                        .padding(top = 2.dp)
                )
                Text(
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(color = TextOnSurface, fontWeight = FontWeight.Bold)) {
                            append("Sociedad Argentina de Pediatría: ")
                        }
                        append("Superar las 2 horas de pantallas recreativas diarias canibaliza horas de descanso REM y reduce la consolidación de la memoria escolar.")
                    },
                    fontSize = 12.sp,
                    color = GrayText
                )
            }
        }
    }
}

@Composable
fun PronosticoCardSpec(
    titulo: String,
    valor: String,
    colorValor: Color,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(SurfaceLowest, RoundedCornerShape(12.dp))
            .padding(vertical = 10.dp, horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = colorValor,
                modifier = Modifier.size(18.dp)
            )
            Text(
                text = valor,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = colorValor
            )
        }
        Text(
            text = titulo,
            fontSize = 10.sp,
            color = GrayText
        )
    }
}

@Composable
fun TarjetaAsignacionItem(
    titulo: String,
    subtitulo: String,
    subtituloColor: Color,
    horas: Int,
    icon: ImageVector,
    iconBg: Color,
    iconColor: Color,
    horasColor: Color = TextOnSurface,
    esFijo: Boolean = false,
    fijoLockIcon: Boolean = false,
    onMinus: () -> Unit = {},
    onPlus: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(SurfaceContainer, RoundedCornerShape(16.dp))
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(iconBg, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(22.dp)
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = titulo,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextOnSurface
                    )
                    if (fijoLockIcon) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "Fijo",
                            tint = GrayText,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
                Text(
                    text = subtitulo,
                    fontSize = 11.sp,
                    color = subtituloColor
                )
            }
        }

        if (esFijo) {
            Box(
                modifier = Modifier
                    .background(SurfaceLowest, RoundedCornerShape(9999.dp))
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "${horas}h",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = SecondaryFixed
                )
            }
        } else {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(SurfaceContainerHighest, CircleShape)
                        .clickable { onMinus() },
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "-", color = TextOnSurface, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }

                Text(
                    text = "${horas}h",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = horasColor,
                    modifier = Modifier.width(28.dp),
                    textAlign = TextAlign.Center
                )

                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(SurfaceContainerHighest, CircleShape)
                        .clickable { onPlus() },
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "+", color = TextOnSurface, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Organizador24hScreenPreview() {
    BienestardigitalandroidTheme {
        Organizador24hScreen(
            uiState = Organizador24hUiState(),
            onCambiarHoras = { _, _ -> },
            onSimularClick = {},
            onBackClick = {}
        )
    }
}
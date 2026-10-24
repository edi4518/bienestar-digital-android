package com.example.bienestar_digital_android.feature.decisionesconectadas

import androidx.annotation.DrawableRes
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.example.bienestar_digital_android.R
import com.example.bienestar_digital_android.core.components.DopamineProgressBar
import com.example.bienestar_digital_android.core.designsystem.*
import com.example.bienestar_digital_android.domain.model.ArquetipoFinal
import kotlinx.coroutines.delay
import java.util.Locale
import kotlin.math.absoluteValue
import kotlin.math.roundToInt
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

// Componente de barra superior que muestra el título del capítulo actual, el indicador de nivel y el botón de retroceso.

@Composable
fun DecisionesTopBar(
    capituloTitulo: String,
    nivel: Int = 4,
    onBackClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Botón circular para volver atrás
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

        // Bloque de texto con etiqueta temática y título del capítulo
        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .background(AccentCyan, CircleShape)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "NARRATIVA INTERACTIVA",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = AccentCyan,
                    letterSpacing = 1.sp
                )
            }
            Text(
                text = capituloTitulo,
                fontSize = 17.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        // Badge indicador del nivel actual
        Box(
            modifier = Modifier
                .background(AccentLime, RoundedCornerShape(12.dp))
                .padding(horizontal = 10.dp, vertical = 5.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "⚡ NIVEL $nivel",
                fontSize = 12.sp,
                fontWeight = FontWeight.Black,
                color = Color.Black
            )
        }
    }
}

// Contenedor general que agrupa las tres barras de recursos vitales del jugador.

@Composable
fun ResourceStatusBar(
    energia: Int,
    foco: Int,
    animo: Int,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .background(BgDarkCard, RoundedCornerShape(16.dp))
            .border(1.dp, BorderDark, RoundedCornerShape(16.dp))
            .padding(horizontal = 14.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            StatResourceItem(
                icono = R.drawable.energia,
                nombre = "Energía",
                valor = energia,
                colorBarra = AccentLime,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(12.dp))
            StatResourceItem(
                icono = R.drawable.foco,
                nombre = "Foco",
                valor = foco,
                colorBarra = AccentCyan,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(12.dp))
            StatResourceItem(
                icono = R.drawable.animo,
                nombre = "Ánimo",
                valor = animo,
                colorBarra = AccentPurple,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

// Subcomponente individual para cada barra de estadística con animación de destello ante cambios.

@Composable
private fun StatResourceItem(
    @DrawableRes icono: Int,
    nombre: String,
    valor: Int,
    colorBarra: Color,
    modifier: Modifier = Modifier
) {
    var valorAnterior by remember { mutableIntStateOf(valor) }
    var colorFlashActivo by remember { mutableStateOf<Color?>(null) }
    val colorSubida = AccentLime
    val colorBajada = AccentPink

    // Efecto secundario que detecta si el recurso subió o bajó para aplicar destello temporal
    LaunchedEffect(valor) {
        if (valor > valorAnterior) {
            colorFlashActivo = colorSubida
            delay(800.milliseconds)
            colorFlashActivo = null
        } else if (valor < valorAnterior) {
            colorFlashActivo = colorBajada
            delay(800.milliseconds)
            colorFlashActivo = null
        }
        valorAnterior = valor
    }

    val targetFraction = (valor / 100f).coerceIn(0f, 1f)
    val animatedProgress by animateFloatAsState(
        targetValue = targetFraction,
        animationSpec = tween(durationMillis = 950, easing = FastOutSlowInEasing),
        label = "ResourceProgressAnimation"
    )
    val animatedColor by animateColorAsState(
        targetValue = colorFlashActivo ?: colorBarra,
        animationSpec = tween(durationMillis = 400),
        label = "ResourceColorAnimation"
    )

    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = icono),
                contentDescription = nombre,
                tint = colorBarra,
                modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = nombre, fontSize = 11.sp, color = Color.White, fontWeight = FontWeight.SemiBold)
            Text(text = "$valor%", fontSize = 11.sp, color = colorFlashActivo ?: Color.LightGray, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(6.dp))
        DopamineProgressBar(progress = animatedProgress, color = animatedColor, height = 6.dp)
    }
}

// Componente encargado de cargar la imagen ilustrativa de la escena desde los assets, permitiendo expandirla o reducirla al hacer clic.

@Composable
fun SceneImageView(
    numeroCapitulo: Int,
    franjaIndex: Int,
    isExpanded: Boolean,
    onImageClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val rutaAsset = "file:///android_asset/img/capitulo${numeroCapitulo}_franja${franjaIndex + 1}.jpg"
    val alturaBox = if (isExpanded) 360.dp else 210.dp

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(alturaBox)
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(22.dp))
            .background(BgDarkCard)
            .border(1.5.dp, AccentCyan.copy(alpha = 0.7f), RoundedCornerShape(22.dp))
            .clickable { onImageClick() },
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            model = rutaAsset,
            contentDescription = "Ilustración de la escena actual",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Etiqueta indicadora de zoom
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(10.dp)
                .background(Color.Black.copy(alpha = 0.6f), RoundedCornerShape(8.dp))
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Text(
                text = if (isExpanded) "🔍 Tocar para reducir" else "🔍 Tocar para ampliar",
                fontSize = 10.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

// Muestra la información temporal y el estado de tensión psicológica (FOMO).
@Composable
fun StatusPill(
    horaLabel: String,
    etapaLabel: String,
    estadoFomo: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .background(Color(0xFF131826), RoundedCornerShape(16.dp))
            .border(1.dp, Color(0xFF1E293B), RoundedCornerShape(16.dp))
            .padding(horizontal = 14.dp, vertical = 6.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "🌙", fontSize = 12.sp)
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "$horaLabel • $etapaLabel",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF94A3B8),
                    letterSpacing = 0.5.sp
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(5.dp)
                        .background(AccentPink.copy(alpha = 0.8f), CircleShape)
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = estadoFomo,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = AccentPink.copy(alpha = 0.9f),
                    letterSpacing = 0.5.sp
                )
            }
        }
    }
}

// Tarjeta estilo Bento que simula una notificación push y el dilema narrativo interno.

@Composable
fun NarrativeBentoCard(
    contextoNarrativo: String,
    remitenteNombre: String,
    mensajeNotificacion: String,
    tiempoRelativo: String,
    appOrigen: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .background(Color(0xFF151B2B), RoundedCornerShape(20.dp))
            .border(1.dp, Color(0xFF22304A), RoundedCornerShape(20.dp))
            .padding(14.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF1A2234), RoundedCornerShape(14.dp))
                    .padding(12.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "💬", fontSize = 12.sp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "$appOrigen • NOTIFICACIÓN",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF94A3B8),
                                letterSpacing = 0.5.sp
                            )
                        }
                        Text(text = tiempoRelativo, fontSize = 10.sp, color = Color(0xFF94A3B8))
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(22.dp)
                                .background(AccentPurple, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "👾", fontSize = 11.sp)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = remitenteNombre, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "\"$mensajeNotificacion\"",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFFE2E8F0),
                        lineHeight = 19.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF101622), RoundedCornerShape(12.dp))
                    .padding(12.dp)
            ) {
                Row(verticalAlignment = Alignment.Top) {
                    Text(text = "👁️‍🗨️", fontSize = 13.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = contextoNarrativo,
                        fontSize = 13.sp,
                        color = Color(0xFFCBD5E1),
                        lineHeight = 18.sp,
                        fontWeight = FontWeight.Normal
                    )
                }
            }
        }
    }
}

// Sección interactiva de decisiones por deslizamiento (swipe), con temporizador reactivo y transiciones de pintado progresivo y difuminado de texto.

@Composable
fun ExactSwipeableDecisionSection(
    numeroCapitulo: Int,
    franjaIndex: Int,
    onSwipeLeft: () -> Unit,
    onSwipeRight: () -> Unit,
    onTimeOut: () -> Unit,
    modifier: Modifier = Modifier
) {
    var offsetX by remember { mutableFloatStateOf(0f) }
    var tiempoRestante by remember { mutableIntStateOf(20) }
    val swipeThreshold = 130f

    // Bucle del temporizador: se reinicia automáticamente al cambiar de capítulo o franja
    LaunchedEffect(numeroCapitulo, franjaIndex) {
        tiempoRestante = 20
        while (tiempoRestante > 0) {
            delay(1.seconds)
            tiempoRestante--
        }
        // Condicional: si el tiempo llega a 0 sin acción del usuario, se dispara el timeout
        if (tiempoRestante == 0) {
            onTimeOut()
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "⏳ DECIDÍ EN " + String.format(Locale.ROOT, "00:%02d", tiempoRestante),
                fontSize = 13.sp,
                fontWeight = FontWeight.Black,
                color = Color.White
            )
            // Iteración para renderizar los bloques indicadores de tiempo restante
            Row(horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                repeat(5) { index ->
                    Box(
                        modifier = Modifier
                            .width(22.dp)
                            .height(6.dp)
                            .background(
                                color = if (tiempoRestante > (index * 4)) Color(0xFFA3E635) else Color(0xFFF87171),
                                shape = RoundedCornerShape(2.dp)
                            )
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(105.dp)
                .pointerInput(Unit) {
                    detectHorizontalDragGestures(
                        onDragEnd = {
                            // Condicionales para validar si se superó el umbral al soltar el arrastre
                            if (offsetX > swipeThreshold) {
                                onSwipeRight()
                            } else if (offsetX < -swipeThreshold) {
                                onSwipeLeft()
                            }
                            offsetX = 0f
                        },
                        onHorizontalDrag = { _, dragAmount: Float ->
                            offsetX += dragAmount
                        }
                    )
                }
        ) {
            // Fondo estático dividido en dos mitades (Izquierda Roja, Derecha Verde)
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(16.dp)),
                horizontalArrangement = Arrangement.spacedBy(0.dp)
            ) {
                // Cálculo de opacidad para desvanecer suavemente los textos originales al deslizar
                val alphaLeft = (1f - (offsetX.absoluteValue / 80f)).coerceIn(0f, 1f)
                val alphaRight = (1f - (offsetX.absoluteValue / 80f)).coerceIn(0f, 1f)

                // Bloque Izquierdo (Rojo base)
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .background(Color(0xFFF87171))
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Column(
                        modifier = Modifier.alpha(alphaLeft),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(text = "◀ DESLIZÁ •", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(text = "IGNORAR Y", fontSize = 13.sp, fontWeight = FontWeight.Black, color = Color.Black)
                        Text(text = "DESCONECTAR", fontSize = 13.sp, fontWeight = FontWeight.Black, color = Color.Black)
                    }
                }

                // Bloque Derecho (Verde Lima base)
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .background(Color(0xFFA3E635))
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Column(
                        modifier = Modifier.alpha(alphaRight),
                        horizontalAlignment = Alignment.End,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(text = "💬 ABRIR", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(text = "Y ENTRAR ▶", fontSize = 13.sp, fontWeight = FontWeight.Black, color = Color.Black)
                        Text(text = "• DESLIZÁ", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                    }
                }
            }

            // Condicionales para el pintado progresivo y centrado del texto ampliado al arrastrar
            if (offsetX < 0f) {
                val progress = ((-offsetX) / 150f).coerceIn(0f, 1f)
                Row(modifier = Modifier.fillMaxSize()) {
                    Box(modifier = Modifier.weight(1f).fillMaxHeight())
                    Box(
                        modifier = Modifier
                            .weight(progress)
                            .fillMaxHeight()
                            .background(Color(0xFFF87171)),
                        contentAlignment = Alignment.Center
                    ) {
                        if (progress > 0.2f) {
                            Text(
                                text = "¡IGNORAR Y DESCONECTAR!",
                                fontSize = (12.sp.value + (progress * 4)).sp,
                                fontWeight = FontWeight.Black,
                                color = Color.Black,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(horizontal = 8.dp)
                            )
                        }
                    }
                }
            } else if (offsetX > 0f) {
                val progress = (offsetX / 150f).coerceIn(0f, 1f)
                Row(modifier = Modifier.fillMaxSize()) {
                    Box(
                        modifier = Modifier
                            .weight(progress)
                            .fillMaxHeight()
                            .background(Color(0xFFA3E635)),
                        contentAlignment = Alignment.Center
                    ) {
                        if (progress > 0.2f) {
                            Text(
                                text = "¡ABRIR Y ENTRAR!",
                                fontSize = (12.sp.value + (progress * 4)).sp,
                                fontWeight = FontWeight.Black,
                                color = Color.Black,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(horizontal = 8.dp)
                            )
                        }
                    }
                    Box(modifier = Modifier.weight(1f).fillMaxHeight())
                }
            }

            // Tarjeta central blanca deslizable
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .offset { IntOffset(offsetX.roundToInt(), 0) },
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .background(Color.White, RoundedCornerShape(10.dp))
                        .border(1.dp, Color.Black, RoundedCornerShape(10.dp))
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "DESLIZÁ\nPARA DECIDIR",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.Black,
                        lineHeight = 11.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

// Pie de página con recomendación basada en evidencia científica o hábitos digitales.

@Composable
fun ScientificTipFooter(
    modifier: Modifier = Modifier,
    textoTip: String = "TIP: Alejar el celular antes de dormir puede ayudarte a descansar mejor"
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .background(Color(0xFF131826), RoundedCornerShape(14.dp))
            .border(1.dp, Color(0xFF1E293B), RoundedCornerShape(14.dp))
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "💡", fontSize = 11.sp)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = textoTip,
                fontSize = 10.sp,
                color = Color(0xFF64748B),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

// Ventana modal emergente que muestra la pausa reflexiva y pedagógica tras cada decisión.
@Composable
fun ReflectiveFeedbackDialog(
    feedbackTexto: String,
    onContinuarClick: () -> Unit
) {
    Dialog(onDismissRequest = {}) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(BgDarkCard, RoundedCornerShape(20.dp))
                .border(1.5.dp, BorderDark, RoundedCornerShape(20.dp))
                .padding(20.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .background(Color(0xFF2B2B36), RoundedCornerShape(8.dp))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "🧠 PAUSA REFLEXIVA",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        color = AccentCyan,
                        letterSpacing = 1.sp
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = feedbackTexto,
                    fontSize = 14.sp,
                    color = Color.White,
                    lineHeight = 20.sp,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(20.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(AccentLime, RoundedCornerShape(12.dp))
                        .clickable { onContinuarClick() }
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Continuar jornada ➔",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.Black
                    )
                }
            }
        }
    }
}

// Tarjeta de balance y cierre de jornada, mostrando el arquetipo final desbloqueado.
@Composable
fun FinalOutcomeCard(
    arquetipo: ArquetipoFinal?,
    onReiniciarClick: () -> Unit,
    onVolverAlHubClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(BgDarkCard, RoundedCornerShape(24.dp))
            .border(1.5.dp, AccentLime, RoundedCornerShape(24.dp))
            .padding(20.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "🏁 JORNADA COMPLETADA",
                fontSize = 11.sp,
                fontWeight = FontWeight.Black,
                color = AccentLime,
                letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = arquetipo?.nombre ?: "Estratega Digital",
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = arquetipo?.descripcionNarrativa
                    ?: "Lograste equilibrar tus impulsos frente a las notificaciones y tu rendimiento diario.",
                fontSize = 13.sp,
                color = Color.LightGray,
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF232330), RoundedCornerShape(14.dp))
                    .padding(12.dp)
            ) {
                Column {
                    Text(
                        text = "TÁCTICA RECOMENDADA: ${arquetipo?.tacticaNombre ?: ""}",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = AccentCyan
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = arquetipo?.tacticaDescripcion
                            ?: "Activá el modo 'No Molestar' 45 minutos antes de dormir.",
                        fontSize = 12.sp,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(AccentLime, RoundedCornerShape(12.dp))
                    .clickable { onReiniciarClick() }
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

            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, BorderDark, RoundedCornerShape(12.dp))
                    .clickable { onVolverAlHubClick() }
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Volver al menú principal",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}

// Orquestador principal de la vista de decisiones interactivas. Conecta el ViewModel con la interfaz gráfica y administra la visualización condicional de pantallas.

@Composable
fun DecisionesConectadasView(
    viewModel: DecisionesConectadasViewModel,
    onNavigateBack: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()

    // Estado local para alternar el zoom de la imagen de escena
    var isImageExpanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BgCanvas)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(bottom = 16.dp)
        ) {
            DecisionesTopBar(
                capituloTitulo = state.capituloActual?.let { "Capítulo ${it.numero}: ${it.titulo}" }
                    ?: "Cargando...",
                nivel = 4,
                onBackClick = {
                    viewModel.finalizarYGuardarProgreso()
                    onNavigateBack()
                }
            )

            ResourceStatusBar(
                energia = state.energia,
                foco = state.foco,
                animo = state.animo
            )

            // Condicional principal: muestra la tarjeta de balance final si el juego terminó, o la franja activa en caso contrario
            if (state.juegoTerminado) {
                FinalOutcomeCard(
                    arquetipo = state.arquetipoFinal,
                    onReiniciarClick = { viewModel.iniciarCapituloNuevo(1) },
                    onVolverAlHubClick = {
                        viewModel.finalizarYGuardarProgreso()
                        onNavigateBack()
                    }
                )
            } else {
                state.franjaActual?.let { franja ->
                    Spacer(modifier = Modifier.height(4.dp))

                    val numeroCapituloActual = state.capituloActual?.numero ?: 1

                    SceneImageView(
                        numeroCapitulo = numeroCapituloActual,
                        franjaIndex = state.indiceFranjaActual,
                        isExpanded = isImageExpanded,
                        onImageClick = { isImageExpanded = !isImageExpanded }
                    )

                    // Condicional para ocultar o mostrar el texto narrativo según el estado de zoom de la imagen
                    if (!isImageExpanded) {
                        StatusPill(
                            horaLabel = franja.horaLabel,
                            etapaLabel = franja.etapaLabel,
                            estadoFomo = franja.estadoFomo
                        )

                        NarrativeBentoCard(
                            contextoNarrativo = franja.contextoNarrativo,
                            remitenteNombre = franja.remitenteNotificacion,
                            mensajeNotificacion = franja.mensajeNotificacion,
                            tiempoRelativo = franja.tiempoRelativo,
                            appOrigen = franja.appOrigen
                        )

                        Spacer(modifier = Modifier.height(8.dp))
                    } else {
                        Spacer(modifier = Modifier.height(12.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "💡 Toca la imagen nuevamente para ver la historia y decidir",
                                fontSize = 11.sp,
                                color = AccentCyan,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    ExactSwipeableDecisionSection(
                        numeroCapitulo = numeroCapituloActual,
                        franjaIndex = state.indiceFranjaActual,
                        onSwipeLeft = { viewModel.seleccionarOpcion(franja.opcionA) },
                        onSwipeRight = { viewModel.seleccionarOpcion(franja.opcionB) },
                        onTimeOut = { viewModel.seleccionarOpcion(franja.opcionA) }
                    )

                    ScientificTipFooter()
                }
            }
        }

        // Condicional para mostrar el diálogo modal reflexivo cuando el estado lo solicita
        if (state.mostrarModalReflexivo) {
            ReflectiveFeedbackDialog(
                feedbackTexto = state.feedbackReflexivo,
                onContinuarClick = { viewModel.avanzarSiguienteFranja() }
            )
        }
    }
}
package com.example.bienestar_digital_android.feature.decisionesconectadas

import androidx.annotation.DrawableRes
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.bienestar_digital_android.R
import com.example.bienestar_digital_android.core.components.DopamineProgressBar
import com.example.bienestar_digital_android.core.designsystem.*
import com.example.bienestar_digital_android.domain.model.ArquetipoFinal
import com.example.bienestar_digital_android.domain.model.OpcionDecision
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

/* 1. BARRA SUPERIOR DE NAVEGACIÓN
      Presenta el título del capítulo actual, la etiqueta temática y el botón de retroceso.
      Permite al usuario regresar a la pantalla anterior ejecutando la acción delegada en `onBackClick`.
 */
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
        // Botón circular para volver al Hub
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

        // Bloque de texto con la categoría y el título del capítulo actual
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

/* 2. CONTENEDOR GENERAL DE RECURSOS
      Agrupa y distribuye proporcionalmente en pantalla los tres indicadores vitales: Energía, Foco y Ánimo.
 */
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
            // Recurso Energía (Verde neón)
            StatResourceItem(
                icono = R.drawable.energia,
                nombre = "Energía",
                valor = energia,
                colorBarra = AccentLime,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(12.dp))

            // Recurso Foco (Cian neón)
            StatResourceItem(
                icono = R.drawable.foco,
                nombre = "Foco",
                valor = foco,
                colorBarra = AccentCyan,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(12.dp))

            // Recurso Ánimo (Púrpura neón)
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

/* 2.1 COMPONENTE INDIVIDUAL DE CADA ESTADÍSTICA
    Se encarga de:
         1. Dibujar el ícono vectorial XML y el porcentaje numérico actual.
         2. Detectar si el valor subió o bajó respecto al estado anterior.
         3. Aplicar un destello cromático temporal: verde si sube, rojo/rosa si baja.
         4. Interpolar suavemente el llenado/vaciado de la barra con `animateFloatAsState.
 */
@Composable
private fun StatResourceItem(
    @DrawableRes icono: Int,
    nombre: String,
    valor: Int,
    colorBarra: Color,
    modifier: Modifier = Modifier
) {
    // Guarda el valor anterior para poder calcular si el cambio fue positivo o negativo
    var valorAnterior by remember { mutableIntStateOf(valor) }
    // Almacena el color momentáneo de destello; si es null, se muestra el color base
    var colorFlashActivo by remember { mutableStateOf<Color?>(null) }

    val colorSubida = AccentLime // Verde neón al incrementar
    val colorBajada = AccentPink // Rojo / Fucsia al decrementar

    // Efecto secundario que se ejecuta cada vez que el valor numérico cambia
    LaunchedEffect(valor) {
        if (valor > valorAnterior) {
            colorFlashActivo = colorSubida
            delay(800.milliseconds) // Mantiene el destello durante 800 ms
            colorFlashActivo = null
        } else if (valor < valorAnterior) {
            colorFlashActivo = colorBajada
            delay(800.milliseconds)
            colorFlashActivo = null
        }
        // Registra el nuevo valor como referencia histórica para la próxima comparación
        valorAnterior = valor
    }

    // Normalización del valor a una escala entre 0.0f y 1.0f para la barra de progreso
    val targetFraction = (valor / 100f).coerceIn(0f, 1f)

    // Animación suave de la longitud de la barra
    val animatedProgress by animateFloatAsState(
        targetValue = targetFraction,
        animationSpec = tween(
            durationMillis = 950,
            easing = FastOutSlowInEasing
        ),
        label = "ResourceProgressAnimation"
    )

    // Animación de transición suave entre el color de destello y el color original
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
            // Icono vectorial y etiqueta textual
            Icon(
                painter = painterResource(id = icono),
                contentDescription = nombre,
                tint = colorBarra,
                modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = nombre,
                fontSize = 11.sp,
                color = Color.White,
                fontWeight = FontWeight.SemiBold
            )
            // Porcentaje numérico que también se tiñe si hay un cambio activo
            Text(
                text = "$valor%",
                fontSize = 11.sp,
                color = colorFlashActivo ?: Color.LightGray,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(6.dp))

        // Barra con relleno animado
        DopamineProgressBar(
            progress = animatedProgress,
            color = animatedColor,
            height = 6.dp
        )
    }
}

/* 3. CÁPSULA DE ESTADO HORARIO Y FOMO
 Muestra el momento del día actual (hora y etapa) junto a una alerta de tensión psicológica (ej. "FOMO ALTO").
 */
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
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .background(BgDarkCard, RoundedCornerShape(20.dp))
            .border(1.dp, BorderDark, RoundedCornerShape(20.dp))
            .padding(horizontal = 14.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "🌙", fontSize = 13.sp)
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "$horaLabel • $etapaLabel",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    letterSpacing = 0.5.sp
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .background(AccentPink, CircleShape)
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = estadoFomo,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Black,
                    color = AccentPink,
                    letterSpacing = 0.5.sp
                )
            }
        }
    }
}

/* 4. TARJETA BENTO NARRATIVA
    Simula una notificación push en tiempo real (de mensajería o redes sociales) acompañada por el relato de la situación a la que se enfrenta el usuario.
*/
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
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .background(BgDarkCard, RoundedCornerShape(24.dp))
            .border(1.dp, BorderDark, RoundedCornerShape(24.dp))
            .padding(14.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Contenedor que simula la notificación emergente del celular
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF232330), RoundedCornerShape(18.dp))
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
                                text = "$appOrigen • NOTIFICACIÓN PUSH",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Gray,
                                letterSpacing = 0.5.sp
                            )
                        }
                        Text(
                            text = tiempoRelativo,
                            fontSize = 10.sp,
                            color = Color.Gray
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Remitente del mensaje
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .background(AccentPurple, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "👾", fontSize = 11.sp)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = remitenteNombre,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Box(
                            modifier = Modifier
                                .size(5.dp)
                                .background(AccentLime, CircleShape)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "\"$mensajeNotificacion\"",
                        fontSize = 13.sp,
                        color = Color.LightGray,
                        lineHeight = 18.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Contexto narrativo del dilema interno
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF14141B), RoundedCornerShape(16.dp))
                    .padding(12.dp)
            ) {
                Row(verticalAlignment = Alignment.Top) {
                    Text(text = "👁️‍🗨️", fontSize = 14.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = contextoNarrativo,
                        fontSize = 13.sp,
                        color = Color(0xFFDDDDDD),
                        lineHeight = 18.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

/* 5. CHIP DE IMPACTO NUMÉRICO
     Renderiza de forma visual las ganancias o pérdidas que una opción provocará en los recursos del jugador.
 */
@Composable
fun ImpactChip(
    text: String,
    colorTexto: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(Color(0xFF251A22), RoundedCornerShape(8.dp))
            .border(0.5.dp, colorTexto.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = text,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = colorTexto
        )
    }
}

/* 6. TARJETA DE DECISIÓN CLICKABLE
        Representa una de las dos bifurcaciones (Opción A impulsiva u Opción B estratégica).
        Expone la descripción de la acción y los chips con el impacto en Energía, Foco y Ánimo.
 */
@Composable
fun DecisionOptionCard(
    modifier: Modifier = Modifier,
    tipoLetra: String,
    subtituloTipo: String,
    opcion: OpcionDecision,
    esEstrategica: Boolean = false,
    onClick: () -> Unit
) {
    val borderColor = if (esEstrategica) AccentLime.copy(alpha = 0.5f) else BorderDark

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .background(BgDarkCard, RoundedCornerShape(18.dp))
            .border(1.dp, borderColor, RoundedCornerShape(18.dp))
            .clickable { onClick() }
            .padding(14.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Etiqueta identificadora (ej.: OPCIÓN A • IMPULSIVA)
                Box(
                    modifier = Modifier
                        .background(
                            if (esEstrategica) AccentLime else Color(0xFF2B2B36),
                            RoundedCornerShape(6.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = "OPCIÓN $tipoLetra • $subtituloTipo",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Black,
                        color = if (esEstrategica) Color.Black else Color.LightGray,
                        letterSpacing = 0.5.sp
                    )
                }

                Text(
                    text = if (esEstrategica) "🛡️" else "👆",
                    fontSize = 13.sp
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Texto descriptivo de la acción a tomar
            Text(
                text = opcion.texto,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                lineHeight = 19.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Lista horizontal de impactos en las estadísticas
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (opcion.deltaEnergia != 0) {
                    val signo = if (opcion.deltaEnergia > 0) "+" else ""
                    ImpactChip(
                        text = "$signo${opcion.deltaEnergia}% Energía",
                        colorTexto = if (opcion.deltaEnergia > 0) AccentLime else AccentPink
                    )
                }

                if (opcion.deltaFoco != 0) {
                    val signo = if (opcion.deltaFoco > 0) "+" else ""
                    ImpactChip(
                        text = "$signo${opcion.deltaFoco}% Foco",
                        colorTexto = if (opcion.deltaFoco > 0) AccentCyan else AccentPink
                    )
                }

                if (opcion.deltaAnimo != 0) {
                    val signo = if (opcion.deltaAnimo > 0) "+" else ""
                    ImpactChip(
                        text = "$signo${opcion.deltaAnimo}% Ánimo",
                        colorTexto = if (opcion.deltaAnimo > 0) AccentPurple else AccentPink
                    )
                }
            }
        }
    }
}

/* 7. PIE DE PÁGINA CON TIP CIENTÍFICO
        Presenta una recomendación basada en evidencia sobre hábitos de sueño, pausas visuales o higiene digital.
 */
@Composable
fun ScientificTipFooter(
    modifier: Modifier = Modifier,
    textoTip: String = "Dato SAP: Retirar pantallas 30 min antes protege la fase REM."
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .background(BgDarkCard, RoundedCornerShape(14.dp))
            .border(1.dp, BorderDark, RoundedCornerShape(14.dp))
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "💡", fontSize = 12.sp)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = textoTip,
                fontSize = 11.sp,
                color = Color.LightGray,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

/* 8. DIÁLOGO REFLEXIVO FORMATIVO
        Ventana modal emergente que aparece inmediatamente después de elegir una opción.
        Explica la justificación pedagógica y psicológica del impacto antes de continuar la jornada.
 */
@Composable
fun ReflectiveFeedbackDialog(
    feedbackTexto: String,
    onContinuarClick: () -> Unit
) {
    Dialog(onDismissRequest = { /* Bloquea el cierre accidental al tocar fuera */ }) {
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

                // Explicación formativa del impacto
                Text(
                    text = feedbackTexto,
                    fontSize = 14.sp,
                    color = Color.White,
                    lineHeight = 20.sp,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Botón para avanzar a la siguiente franja horaria
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

/* 9. TARJETA DE CIERRE DE JORNADA (ARQUETIPO DESBLOQUEADO)
        Se renderiza cuando el día concluye (o cuando algún recurso cae a cero). Muestra el arquetipo final alcanzado, su táctica preventiva y ofrece dos caminos:
            1. Reiniciar la partida.
            2. Volver al menú principal guardando los resultados obtenidos en el repositorio global.
 */
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

            // Nombre del arquetipo desbloqueado
            Text(
                text = arquetipo?.nombre ?: "Estratega Digital",
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(10.dp))

            // 1. Resumen de conducta
            Text(
                text = arquetipo?.descripcionNarrativa
                    ?: "Lograste equilibrar tus impulsos frente a las notificaciones y tu rendimiento diario.",
                fontSize = 13.sp,
                color = Color.LightGray,
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 2. Tarjeta interna con la táctica recomendada
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

            // Botón: Volver a jugar desde la franja 1
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

            // Botón: Salir al menú principal guardando el progreso
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

/* 10. PANTALLA PRINCIPAL DEL JUEGO (ORQUESTADOR COMPOSE)
        Recoge el estado del ViewModel y decide dinámicamente si mostrar:
            - El transcurso de la jornada horaria con sus dilemas y opciones.
            - La tarjeta de desenlace final si el juego terminó.
            - La ventana modal reflexiva cuando se elige una opción.
 */
@Composable
fun DecisionesConectadasView(
    viewModel: DecisionesConectadasViewModel,
    onNavigateBack: () -> Unit
) {
    // Suscripción reactiva al StateFlow del ViewModel
    val state by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()

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
            // Barra superior fija con guardado si el usuario pulsa retroceder
            DecisionesTopBar(
                capituloTitulo = state.capituloActual?.let { "Capítulo ${it.numero}:${it.titulo}" }
                    ?: "Cargando...",
                nivel = 4,
                onBackClick = {
                    viewModel.finalizarYGuardarProgreso()
                    onNavigateBack()
                }
            )

            // Barra visual de los 3 recursos
            ResourceStatusBar(
                energia = state.energia,
                foco = state.foco,
                animo = state.animo
            )

            // BIFURCACIÓN DE PANTALLA:
            // Si el juego terminó, mostramos la tarjeta de balance; si no, mostramos la franja activa.
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
                    // Cápsula de horario y FOMO
                    StatusPill(
                        horaLabel = franja.horaLabel,
                        etapaLabel = franja.etapaLabel,
                        estadoFomo = franja.estadoFomo
                    )

                    // Notificación push y contexto narrativo
                    NarrativeBentoCard(
                        contextoNarrativo = franja.contextoNarrativo,
                        remitenteNombre = franja.remitenteNotificacion,
                        mensajeNotificacion = franja.mensajeNotificacion,
                        tiempoRelativo = franja.tiempoRelativo,
                        appOrigen = franja.appOrigen
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 18.dp, vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Tu Elección",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "IMPACTA TU JORNADA",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Gray,
                            letterSpacing = 0.5.sp
                        )
                    }

                    // Opción A (Impulsiva)
                    DecisionOptionCard(
                        tipoLetra = "A",
                        subtituloTipo = "IMPULSIVA",
                        opcion = franja.opcionA,
                        esEstrategica = false,
                        onClick = { viewModel.seleccionarOpcion(franja.opcionA) }
                    )

                    // Opción B (Estratégica)
                    DecisionOptionCard(
                        tipoLetra = "B",
                        subtituloTipo = "ESTRATÉGICA",
                        opcion = franja.opcionB,
                        esEstrategica = true,
                        onClick = { viewModel.seleccionarOpcion(franja.opcionB) }
                    )

                    // Tip informativo al final del scroll
                    ScientificTipFooter()
                }
            }
        }

        // Modal que se superpone cuando el estado lo solicita
        if (state.mostrarModalReflexivo) {
            ReflectiveFeedbackDialog(
                feedbackTexto = state.feedbackReflexivo,
                onContinuarClick = { viewModel.avanzarSiguienteFranja() }
            )
        }
    }
}
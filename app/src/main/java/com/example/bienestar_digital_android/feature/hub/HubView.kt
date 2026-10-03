package com.example.bienestar_digital_android.feature.hub

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bienestar_digital_android.R


@Composable
fun HubScreen(
    uiState: HubUiState,
    onEvent: (HubUiEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    // Contenedor principal
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Elementos del Hub

        // Elemento 1: Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Lado izquierdo del Header
            Column {
                Text(
                    text = "Hola ${uiState.userName.uppercase()}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "Tu radar digital",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
            // Lado derecho del Header = Racha
            Row(
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.fuego),
                    contentDescription = "Icono de fuego",
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = "${uiState.userRacha}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }

        // Barra de estado del jugador
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 1. Barra de Energía
            StatBarItem(
                icon = R.drawable.energia,
                value = uiState.stats.energia,
                barColor = Color(0xFFCCFF00)
            )

            // 2. Barra de Foco
            StatBarItem(
                icon = R.drawable.foco,
                value = uiState.stats.foco,
                barColor = Color(0xFF00F5D4)
            )

            // 3. Barra de Ánimo
            StatBarItem(
                icon = R.drawable.animo,
                value = uiState.stats.animo,
                barColor = Color(0xFF9D4EDD)
            )
        }


        // Minijuegos
        Text(
            text = "MISIONES ACTIVAS",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Gray,
            modifier = Modifier.padding(vertical = 12.dp)
        )

        // Fila para dividir el Bento grid en dos columnas
        Row(
            modifier = Modifier.fillMaxWidth()
                .height(IntrinsicSize.Max),
            horizontalArrangement = Arrangement.spacedBy(12.dp)

        ) {
            // Decisiones Conectadas
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
                    .background(
                        color = Color(0xFF1B1B24),
                        shape = RoundedCornerShape(20.dp)
                    )
                    .border(
                        width = 1.5.dp,
                        color = Color(0xFF2E2E3E),
                        shape = RoundedCornerShape(20.dp)
                    )
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "NARRATIVA",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF00F5D4)
                )
                Text(
                    text = uiState.misionNarrativa.titulo,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = uiState.misionNarrativa.descripcion,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.weight(1f))

                // Botón "Continuar"
                Row( modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Color(0xFF14141B),
                        shape = RoundedCornerShape(12.dp))
                        .clickable { onEvent(HubUiEvent.OnContinuarNarrativaClick)}
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                    ) {
                    Text(
                        text = "Continuar",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF00F5D4)
                    )
                    Text(
                        text = "▶",
                        fontSize = 12.sp,
                        color = Color(0xFF00F5D4)
                    )
                }
            }

            // Caza de notis y Mito y verdad
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Tarjeta 2: Caza de Notis
                        uiState.miniMisiones.forEach { mision ->
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color(0xFF1B1B24), RoundedCornerShape(20.dp))
                                    .border(1.5.dp, Color(0xFF2E2E3E), RoundedCornerShape(20.dp))
                                    .clickable { onEvent(HubUiEvent.OnMiniMisionClick(mision.id)) }
                                    .padding(14.dp),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = mision.categoriaTag,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (mision.categoriaTag == "REFLEJOS") Color(0xFF9D4EDD) else Color(0xFFCCFF00)
                                )
                                Text(
                                    text = mision.titulo,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Text(
                                    text = mision.descripcion,
                                    fontSize = 11.sp,
                                    color = Color.Gray
                                )
                            }
                        }
                    }
                }
        // EL ORGANIZADOR DE LAS 24 HORAS
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
                .background(
                    color = Color(0xFF1B1B24), // SurfaceElevated
                    shape = RoundedCornerShape(20.dp)
                )
                .border(
                    width = 1.5.dp,
                    color = Color(0xFF2E2E3E), // StrokeStructural
                    shape = RoundedCornerShape(20.dp)
                )
                .clickable { onEvent(HubUiEvent.OnOrganizadorClick)}
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "ESTRATEGIA & PUZZLE",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFCCFF00) // Electric Lime
                )

                // Badge de estado "Equilibrado"
                Row(
                    modifier = Modifier
                        .background(
                            color = Color(0xFF262634),
                            shape = RoundedCornerShape(9999.dp)
                        )
                        .padding(horizontal = 10.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "🛡️", fontSize = 10.sp)
                    Text(
                        text = if (uiState.balance.esEquilibrado) "Equilibrado" else "Desbalanceado",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFCCFF00)
                    )
                }
            }

            Text(
                text = "El Organizador de las 24 Horas",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = "Distribuí tu día sin fundirte la dopamina.",
                fontSize = 12.sp,
                color = Color.Gray
            )

            // Simulación de la barra horaria segmentada por coloreS
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .background(
                        color = Color(0xFF14141B),
                        shape = RoundedCornerShape(4.dp)
                    )
            ) {
                // Bloque Sueño (Violeta)
                Box(modifier = Modifier.weight(0.33f).fillMaxHeight().background(Color(0xFF9D4EDD)))
                // Bloque Estudio (Cian)
                Box(modifier = Modifier.weight(0.25f).fillMaxHeight().background(Color(0xFF00F5D4)))
                // Bloque Pantallas / Ocio (Lima)
                Box(modifier = Modifier.weight(0.25f).fillMaxHeight().background(Color(0xFFCCFF00)))
                // Bloque Libre (Gris)
                Box(modifier = Modifier.weight(0.17f).fillMaxHeight().background(Color(0xFF4A4A5A)))
            }

            // Leyenda inferior de la barra de tiempo
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "🟣 Sueño ${uiState.balance.horasSuenio}", fontSize = 10.sp, color = Color.Gray)
                Text(text = "🔵 Estudio ${uiState.balance.horasEstudio}", fontSize = 10.sp, color = Color.Gray)
                Text(text = "🟢 Pantalla ${uiState.balance.horasPantalla}", fontSize = 10.sp, color = Color.Gray)
                Text(text = "⚪ Libre ${uiState.balance.horasLibre}", fontSize = 10.sp, color = Color.Gray)
            }
        }

        // DATA REAL
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .background(
                    color = Color(0xFF1B1B24),
                    shape = RoundedCornerShape(20.dp)
                )
                .border(
                    width = 1.5.dp,
                    color = Color(0xFF2E2E3E),
                    shape = RoundedCornerShape(20.dp)
                )
                .clickable { onEvent(HubUiEvent.OnTipClick)}
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icono de bombilla o alerta científica
            Text(text = "💡", fontSize = 24.sp)

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = uiState.tipDelDia.fuente,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF00F5D4) // Neon Cyan
                )
                Text(
                    text = uiState.tipDelDia.descripcion,
                    fontSize = 12.sp,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun StatBarItem(
    icon: Int,
    value: Int,
    barColor: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.width(90.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Image(
                painter = painterResource(id = icon),
                contentDescription = null,
                modifier = Modifier.size(16.dp)
            )
            Text(
                text = "$value",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = barColor
            )
        }

        // Barra con fondo gris y relleno
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .background(Color(0xFF2E2E3E), RoundedCornerShape(3.dp))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(fraction = (value.coerceIn(0, 100) / 100f))
                    .fillMaxHeight()
                    .background(barColor, RoundedCornerShape(3.dp))
            )
        }
    }
}
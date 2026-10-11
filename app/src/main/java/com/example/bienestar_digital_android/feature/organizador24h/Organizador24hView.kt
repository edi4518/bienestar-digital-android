package com.example.bienestar_digital_android.feature.organizador24h

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import com.example.bienestar_digital_android.feature.organizador24h.Organizador24hUiState

@Composable
fun Organizador24hScreen(
    uiState: Organizador24hUiState,
    onCambiarHoras: (String, Int) -> Unit,
    onSimularClick: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
){
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF0F0F13))
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Boton de retroceso y badge de horas asignadas
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            Box(
                modifier = Modifier
                    .background(Color(0xFF1B1B24), RoundedCornerShape(12.dp))
                    .border(1.5.dp, Color(0xFF2E2E3E), RoundedCornerShape(12.dp))
                    .clickable { onBackClick() }
                    .padding(10.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.flechaizq),
                    contentDescription = "Flecha apuntando a la Izquierda",
                    modifier = Modifier.size(16.dp),
                    colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(Color.White)
                )
            }
            // Badge de horas asignadas
            Row(
                modifier = Modifier
                    .background(
                        color = if (uiState.esSumaValida) Color(0xFFCCFF00) else Color(0xFFFF5964),
                        shape = RoundedCornerShape(9999.dp)
                    )
                    .padding(horizontal = 14.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.reloj),
                    contentDescription = "Reloj",
                    modifier = Modifier.size(16.dp),
                )
                Text(
                    text = "${uiState.totalAsignadas}hs / 24hs ASIGNADAS",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }
        }

        // 2. Línea de 24 Horas (Barra Segmentada Visual)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF1B1B24), RoundedCornerShape(20.dp))
                    .border(1.5.dp, Color(0xFF2E2E3E), RoundedCornerShape(20.dp))
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(text = "LÍNEA DE 24 HORAS", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF00F5D4))

                // Barra Proporcional
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(10.dp)
                        .background(Color(0xFF14141B), RoundedCornerShape(5.dp))
                ) {
                    if (uiState.horasSuenio > 0) Box(modifier = Modifier.weight(uiState.horasSuenio.toFloat()).fillMaxHeight().background(Color(0xFF00F5D4)))
                    if (uiState.horasEscuela > 0) Box(modifier = Modifier.weight(uiState.horasEscuela.toFloat()).fillMaxHeight().background(Color(0xFFFFE600)))
                    if (uiState.horasEstudio > 0) Box(modifier = Modifier.weight(uiState.horasEstudio.toFloat()).fillMaxHeight().background(Color(0xFF9D4EDD)))
                    if (uiState.horasPantalla > 0) Box(modifier = Modifier.weight(uiState.horasPantalla.toFloat()).fillMaxHeight().background(Color(0xFFFF5964)))
                    if (uiState.horasSocial > 0) Box(modifier = Modifier.weight(uiState.horasSocial.toFloat()).fillMaxHeight().background(Color(0xFFCCFF00)))
                    if (uiState.horasDeporte > 0) Box(modifier = Modifier.weight(uiState.horasDeporte.toFloat()).fillMaxHeight().background(Color(0xFF3A86FF)))
                }
            }

            // 3. Pronóstico para Mañana
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                PronosticoCard("Batería Diurna", "${uiState.bateriaDiurna}%", Color(0xFFCCFF00), Modifier.weight(1f))
                PronosticoCard("Foco Cognitivo", uiState.focoCognitivo, Color(0xFF00F5D4), Modifier.weight(1f))
                PronosticoCard("Cansancio", uiState.cansancioEstado, Color(0xFFFF5964), Modifier.weight(1f))
            }

            // 4. Bloques Modulares Interactivos (+ / -)
            Text(text = "AJUSTÁ TU DISTRIBUCIÓN", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Gray)

            BloqueItem(titulo = "Sueño y Descanso", subtitulo = "Recomendado SAP: 8h - 10h", horas = uiState.horasSuenio, colorBadge = Color(0xFF00F5D4), onMinus = { onCambiarHoras("suenio", -1) }, onPlus = { onCambiarHoras("suenio", 1) })
            BloqueItem(titulo = "Escuela y Tareas", subtitulo = "Horas fijas obligatorias", horas = uiState.horasEscuela, colorBadge = Color(0xFFFFE600), esFijo = true)
            BloqueItem(titulo = "Redes y Pantallas", subtitulo = "Zona crítica de ocio", horas = uiState.horasPantalla, colorBadge = Color(0xFFFF5964), onMinus = { onCambiarHoras("pantalla", -1) }, onPlus = { onCambiarHoras("pantalla", 1) })
            BloqueItem(titulo = "Deporte y Amigos", subtitulo = "Conexión real", horas = uiState.horasSocial, colorBadge = Color(0xFFCCFF00), onMinus = { onCambiarHoras("social", -1) }, onPlus = { onCambiarHoras("social", 1) })
            BloqueItem(titulo = "Estudio en Casa", subtitulo = "Refuerzo académico", horas = uiState.horasEstudio, colorBadge = Color(0xFF9D4EDD), onMinus = { onCambiarHoras("estudio", -1) }, onPlus = { onCambiarHoras("estudio", 1) })

            // 5. Botón de Comprobar Equilibrio
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFCCFF00), RoundedCornerShape(16.dp))
                    .clickable { onSimularClick() }
                    .padding(18.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "COMPROBAR EQUILIBRIO ⚡",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }

            // 6. Feedback / Alerta Activa si se simuló
            if (uiState.alertaActiva != null) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF1B1B24), RoundedCornerShape(16.dp))
                        .border(1.5.dp, Color(0xFFFF5964), RoundedCornerShape(16.dp))
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(text = uiState.alertaActiva!!, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFF5964))
                    Text(text = uiState.feedbackReflexivo!!, fontSize = 12.sp, color = Color.Gray)
                }
            }
    }
}

@Composable
fun PronosticoCard(titulo: String, valor: String, colorValor: Color, modifier: Modifier = Modifier) {
        Column(
            modifier = modifier
                .background(Color(0xFF1B1B24), RoundedCornerShape(16.dp))
                .border(1.5.dp, Color(0xFF2E2E3E), RoundedCornerShape(16.dp))
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(text = titulo, fontSize = 10.sp, color = Color.Gray)
            Text(text = valor, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = colorValor)
        }
    }

    @Composable
    fun BloqueItem(
        titulo: String,
        subtitulo: String,
        horas: Int,
        colorBadge: Color,
        esFijo: Boolean = false,
        onMinus: () -> Unit = {},
        onPlus: () -> Unit = {}
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF1B1B24), RoundedCornerShape(20.dp))
                .border(1.5.dp, Color(0xFF2E2E3E), RoundedCornerShape(20.dp))
                .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(text = titulo, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                Text(text = subtitulo, fontSize = 11.sp, color = Color.Gray)
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (!esFijo) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .background(Color(0xFF2E2E3E), RoundedCornerShape(8.dp))
                            .clickable { onMinus() },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "-", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }

                Text(
                    text = "${horas}h",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorBadge
                )

                if (!esFijo) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .background(Color(0xFF2E2E3E), RoundedCornerShape(8.dp))
                            .clickable { onPlus() },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "+", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
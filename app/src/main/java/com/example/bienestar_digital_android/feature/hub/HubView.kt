package com.example.bienestar_digital_android.feature.hub

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
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
    userName: String,
    userRacha: Int,
    modifier: Modifier = Modifier
) {
    // Contenedor principal
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
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
                    text = "Hola ${userName.uppercase()}",
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
                    text = "$userRacha",
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
                .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Indicador 1: Energía
            StatBarItem(
                iconRes = R.drawable.energia,
                percentage = "75%",
                barColor = Color(0xFFCCFF00)
            )

            // Indicador 2: Foco
            StatBarItem(
                iconRes = R.drawable.foco,
                percentage = "60%",
                barColor = Color(0xFF00F5D4)
            )

            // Indicador 3: Ánimo
            StatBarItem(
                iconRes = R.drawable.animo,
                percentage = "85%",
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
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Decisiones Conectadas
            Column(
                modifier = Modifier
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
                    text = "Decisiones Conectadas",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "Cada elección nocturna cuenta.",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }

            // Caza de notis y Mito y verdad
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Tarjeta 2: Caza de Notis
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = Color(0xFF1B1B24),
                            shape = RoundedCornerShape(20.dp)
                        )
                        .border(
                            width = 1.5.dp,
                            color = Color(0xFF2E2E3E),
                            shape = RoundedCornerShape(20.dp)
                        )
                        .padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "REFLEJOS",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF9D4EDD)
                    )
                    Text(
                        text = "Caza de Notis",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Filtrá las trampas del algoritmo.",
                        fontSize = 11.sp,
                        color = Color.Gray
                    )
                }

                // Tarjeta 3: Mito o Verdad
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = Color(0xFF1B1B24),
                            shape = RoundedCornerShape(20.dp)
                        )
                        .border(
                            width = 1.5.dp,
                            color = Color(0xFF2E2E3E),
                            shape = RoundedCornerShape(20.dp)
                        )
                        .padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "SWIPE & TRUTH",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFCCFF00)
                    )
                    Text(
                        text = "Mito o Verdad",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Desarmá fake trends rápido.",
                        fontSize = 11.sp,
                        color = Color.Gray
                    )
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
                                text = "Equilibrado",
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
                        Text(text = "🟣 Sueño 8h", fontSize = 10.sp, color = Color.Gray)
                        Text(text = "🔵 Estudio 6h", fontSize = 10.sp, color = Color.Gray)
                        Text(text = "🟢 Pantalla 3h", fontSize = 10.sp, color = Color.Gray)
                        Text(text = "⚪ Libre 7h", fontSize = 10.sp, color = Color.Gray)
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
                            text = "DATA REAL  •  UNICEF 2024",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF00F5D4) // Neon Cyan
                        )
                        Text(
                            text = "1 de cada 2 adolescentes reporta uso compulsivo de pantallas. Entrenar tus pausas recupera tu foco.",
                            fontSize = 12.sp,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun StatBarItem(
    iconRes: Int,
    percentage: String,
    barColor: Color,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = null,
            modifier = Modifier.size(16.dp)
        )
        Text(
            text = percentage,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = barColor
        )
    }
}

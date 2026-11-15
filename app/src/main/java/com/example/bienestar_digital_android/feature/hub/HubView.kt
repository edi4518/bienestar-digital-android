package com.example.bienestar_digital_android.feature.hub

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.bienestar_digital_android.R
import com.example.bienestar_digital_android.feature.onboarding.getEmojiForAvatarId
import com.example.bienestar_digital_android.feature.onboarding.presetAvatarsList

@Composable
fun HubScreen(
    uiState: HubUiState,
    onEvent: (HubUiEvent) -> Unit,
    onAbrirRankingClick: () -> Unit = {},
    onCerrarRankingClick: () -> Unit = {},
    onCambiarFiltroRanking: (String) -> Unit = {},
    onAbrirEditarPerfilClick: () -> Unit = {},
    onCerrarEditarPerfilClick: () -> Unit = {},
    onGuardarPerfilClick: (String, Int, String?) -> Unit = { _, _, _ -> },
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Elemento 1: Header (Lado Izquierdo Clickable para Editar Perfil)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Lado izquierdo del Header con Foto/Emoji e Indicador de Edición
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { onAbrirEditarPerfilClick() }
                    .padding(4.dp)
            ) {
                // Avatar o Foto del Perfil
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF262634))
                        .border(1.5.dp, Color(0xFF00F5D4), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    if (!uiState.customAvatarUri.isNullOrBlank()) {
                        AsyncImage(
                            model = uiState.customAvatarUri,
                            contentDescription = "Avatar",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Text(
                            text = getEmojiForAvatarId(uiState.avatarResId),
                            fontSize = 18.sp
                        )
                    }
                }

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Hola ${uiState.userName.uppercase()}",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "✏️", fontSize = 12.sp)
                    }
                    Text(
                        text = "Tu radar digital",
                        fontSize = 13.sp,
                        color = Color.Gray
                    )
                }
            }

            // Lado derecho del Header = Botón Ranking + Racha
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Botón Ranking Trophy
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(Color(0xFF262634))
                        .border(1.dp, Color(0xFF2E2E3E), RoundedCornerShape(50))
                        .clickable { onAbrirRankingClick() }
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "🏆", fontSize = 13.sp)
                        Text(
                            text = "RANKING",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFCCFF00)
                        )
                    }
                }

                // Racha de Fuego
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.fuego),
                        contentDescription = "Icono de fuego",
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = "${uiState.userRacha}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }

        // Barra de estado del jugador (Contenedor estilo tarjeta con el diseño exacto de la Imagen 2)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
                .background(Color(0xFF1B1B24), RoundedCornerShape(20.dp))
                .border(1.5.dp, Color(0xFF2E2E3E), RoundedCornerShape(20.dp))
                .padding(horizontal = 14.dp, vertical = 14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 1. Barra de Energía
                StatBarItem(
                    icon = R.drawable.energia,
                    name = "Energía",
                    value = uiState.stats.energia,
                    barColor = Color(0xFFCCFF00),
                    modifier = Modifier.weight(1f)
                )

                // 2. Barra de Foco
                StatBarItem(
                    icon = R.drawable.foco,
                    name = "Foco",
                    value = uiState.stats.foco,
                    barColor = Color(0xFF00F5D4),
                    modifier = Modifier.weight(1f)
                )

                // 3. Barra de Ánimo
                StatBarItem(
                    icon = R.drawable.animo,
                    name = "Ánimo",
                    value = uiState.stats.animo,
                    barColor = Color(0xFF9D4EDD),
                    modifier = Modifier.weight(1f)
                )
            }
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
            modifier = Modifier
                .fillMaxWidth()
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
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "NARRATIVA",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF00F5D4)
                    )

                    // Badge de capítulo guardado (ejemplo: "Cap. 2/7")
                    Box(
                        modifier = Modifier
                            .background(
                                color = Color(0xFF262634),
                                shape = RoundedCornerShape(9999.dp)
                            )
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = "Cap. ${uiState.misionNarrativa.capituloActual}/${uiState.misionNarrativa.totalCapitulos}",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }

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
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = Color(0xFF14141B),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .clickable { onEvent(HubUiEvent.OnContinuarNarrativaClick) }
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
                    color = Color(0xFF1B1B24),
                    shape = RoundedCornerShape(20.dp)
                )
                .border(
                    width = 1.5.dp,
                    color = Color(0xFF2E2E3E),
                    shape = RoundedCornerShape(20.dp)
                )
                .clickable { onEvent(HubUiEvent.OnOrganizadorClick) }
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
                    color = Color(0xFFCCFF00)
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

            // Simulación de la barra horaria segmentada
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .background(
                        color = Color(0xFF14141B),
                        shape = RoundedCornerShape(4.dp)
                    )
            ) {
                Box(modifier = Modifier.weight(0.33f).fillMaxHeight().background(Color(0xFF9D4EDD)))
                Box(modifier = Modifier.weight(0.25f).fillMaxHeight().background(Color(0xFF00F5D4)))
                Box(modifier = Modifier.weight(0.25f).fillMaxHeight().background(Color(0xFFCCFF00)))
                Box(modifier = Modifier.weight(0.17f).fillMaxHeight().background(Color(0xFF4A4A5A)))
            }

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
                .clickable { onEvent(HubUiEvent.OnTipClick) }
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "💡", fontSize = 24.sp)

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = uiState.tipDelDia.fuente,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF00F5D4)
                )
                Text(
                    text = uiState.tipDelDia.descripcion,
                    fontSize = 12.sp,
                    color = Color.White
                )
            }
        }
    }

    // Modal de Ranking Global en Firebase
    if (uiState.mostrarRankingDialog) {
        RankingDialog(
            uiState = uiState,
            onDismiss = onCerrarRankingClick,
            onCambiarFiltro = onCambiarFiltroRanking
        )
    }

    // Modal para Editar Perfil (Nombre y Foto)
    if (uiState.mostrarEditarPerfilDialog) {
        EditarPerfilModalDialog(
            uiState = uiState,
            onDismiss = onCerrarEditarPerfilClick,
            onGuardar = onGuardarPerfilClick
        )
    }
}

@Composable
fun EditarPerfilModalDialog(
    uiState: HubUiState,
    onDismiss: () -> Unit,
    onGuardar: (String, Int, String?) -> Unit
) {
    var inputNombre by remember { mutableStateOf(uiState.userName) }
    var selectedAvatarId by remember { mutableIntStateOf(uiState.avatarResId) }
    var customUri by remember { mutableStateOf(uiState.customAvatarUri) }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            customUri = it.toString()
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(text = "✏️", fontSize = 20.sp)
                Text(
                    text = "EDITAR PERFIL",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White
                )
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Previsualización de Foto o Avatar
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF262634))
                        .border(2.dp, Color(0xFF00F5D4), CircleShape)
                        .clickable { galleryLauncher.launch("image/*") },
                    contentAlignment = Alignment.Center
                ) {
                    if (!customUri.isNullOrBlank()) {
                        AsyncImage(
                            model = customUri,
                            contentDescription = "Foto de perfil",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Text(
                            text = getEmojiForAvatarId(selectedAvatarId),
                            fontSize = 36.sp
                        )
                    }
                }

                OutlinedButton(
                    onClick = { galleryLauncher.launch("image/*") },
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF00F5D4)),
                    border = BorderStroke(1.dp, Color(0xFF00F5D4)),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Image,
                        contentDescription = "Elegir foto",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Elegir Foto de Galería", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }

                Text(
                    text = "O elige un Avatar Preset:",
                    fontSize = 11.sp,
                    color = Color.Gray
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    presetAvatarsList.take(4).forEach { item ->
                        val esSel = customUri.isNullOrBlank() && selectedAvatarId == item.id
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(if (esSel) Color(0xFFCCFF00) else Color(0xFF262634))
                                .clickable {
                                    customUri = null
                                    selectedAvatarId = item.id
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = item.emoji, fontSize = 20.sp)
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    presetAvatarsList.takeLast(4).forEach { item ->
                        val esSel = customUri.isNullOrBlank() && selectedAvatarId == item.id
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(if (esSel) Color(0xFFCCFF00) else Color(0xFF262634))
                                .clickable {
                                    customUri = null
                                    selectedAvatarId = item.id
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = item.emoji, fontSize = 20.sp)
                        }
                    }
                }

                OutlinedTextField(
                    value = inputNombre,
                    onValueChange = { inputNombre = it },
                    label = { Text("Gamertag / Nombre") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = Color(0xFFCCFF00),
                        unfocusedBorderColor = Color(0xFF2E2E3E),
                        focusedLabelColor = Color(0xFFCCFF00)
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onGuardar(inputNombre, selectedAvatarId, customUri) },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFCCFF00)),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("GUARDAR", color = Color(0xFF0F0F14), fontWeight = FontWeight.ExtraBold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("CANCELAR", color = Color.Gray)
            }
        },
        containerColor = Color(0xFF14141B),
        titleContentColor = Color.White,
        textContentColor = Color.White
    )
}

@Composable
fun RankingDialog(
    uiState: HubUiState,
    onDismiss: () -> Unit,
    onCambiarFiltro: (String) -> Unit
) {
    val filtros = listOf("Global", "CazaNotificaciones", "Desmitificador")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "🏆", fontSize = 20.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "RANKING GLOBAL",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )
                    }
                    Text(
                        text = "LIVE 🔥",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF00F5D4)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Tabs de Filtros
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    filtros.forEach { f ->
                        val esSeleccionado = uiState.juegoFiltroRanking == f
                        val etiqueta = when (f) {
                            "CazaNotificaciones" -> "Caza Notis"
                            "Desmitificador" -> "Mitos"
                            else -> "Global"
                        }

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(32.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (esSeleccionado) Color(0xFFCCFF00) else Color(0xFF262634))
                                .clickable { onCambiarFiltro(f) },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = etiqueta,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (esSeleccionado) Color(0xFF0F0F14) else Color.White
                            )
                        }
                    }
                }
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 360.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (uiState.rankingList.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Aún no hay puntuaciones registradas. ¡Sé el primero en jugar y figurar en el ranking!",
                            color = Color.Gray,
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                } else {
                    uiState.rankingList.forEachIndexed { index, item ->
                        val posicion = index + 1
                        val esTop1 = posicion == 1
                        val esTop2 = posicion == 2
                        val esTop3 = posicion == 3
                        val esMio = item.gamertag.equals(uiState.userName, ignoreCase = true)

                        val badgeBg = when {
                            esTop1 -> Color(0xFFCCFF00)
                            esTop2 -> Color(0xFF00F5D4)
                            esTop3 -> Color(0xFF9D4EDD)
                            else -> Color(0xFF262634)
                        }

                        val badgeTextoColor = when {
                            esTop1 || esTop2 -> Color(0xFF0F0F14)
                            else -> Color.White
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (esMio) Color(0xFF1E2838) else Color(0xFF1B1B24))
                                .border(
                                    width = if (esMio || esTop1) 1.5.dp else 1.dp,
                                    color = when {
                                        esMio -> Color(0xFF00F5D4)
                                        esTop1 -> Color(0xFFCCFF00)
                                        else -> Color(0xFF2E2E3E)
                                    },
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .padding(horizontal = 12.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(28.dp)
                                        .clip(CircleShape)
                                        .background(badgeBg),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = when {
                                            esTop1 -> "🥇"
                                            esTop2 -> "🥈"
                                            esTop3 -> "🥉"
                                            else -> "#$posicion"
                                        },
                                        fontSize = if (esTop1 || esTop2 || esTop3) 14.sp else 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = badgeTextoColor
                                    )
                                }

                                Column {
                                    Text(
                                        text = item.gamertag.uppercase(),
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                    Text(
                                        text = if (item.fechaIso.isNotBlank()) item.fechaIso else "Hoy",
                                        fontSize = 10.sp,
                                        color = Color.Gray
                                    )
                                }
                            }

                            Text(
                                text = "${item.puntaje} pts",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(0xFFCCFF00)
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("CERRAR", color = Color(0xFFCCFF00), fontWeight = FontWeight.Bold)
            }
        },
        containerColor = Color(0xFF14141B),
        titleContentColor = Color.White,
        textContentColor = Color.White
    )
}

@Composable
fun StatBarItem(
    icon: Int,
    name: String,
    value: Int,
    barColor: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(3.dp)
        ) {
            Image(
                painter = painterResource(id = icon),
                contentDescription = name,
                modifier = Modifier.size(15.dp)
            )
            Text(
                text = name,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "${value.coerceIn(0, 100)}%",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFE2E8F0)
            )
        }

        // Barra con fondo gris y relleno
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .background(Color(0xFF2E2E3E), RoundedCornerShape(3.dp))
        ) {
            val progressFrac = (value.coerceIn(0, 100) / 100f).coerceAtLeast(0.04f)
            Box(
                modifier = Modifier
                    .fillMaxWidth(fraction = progressFrac)
                    .fillMaxHeight()
                    .background(barColor, RoundedCornerShape(3.dp))
            )
        }
    }
}

package com.example.bienestar_digital_android.feature.onboarding

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.bienestar_digital_android.domain.model.GenderOption

// --- Paleta de colores ---
val DarkBg = Color(0xFF0D0E11)
val CardDarkBg = Color(0xFF16181D)
val InputDarkBg = Color(0xFF1F222A)
val NeonYellow = Color(0xFFD2FF00)
val NeonGreenText = Color(0xFF00FFC2)
val BorderDark = Color(0xFF2A2E39)
val TextGray = Color(0xFF9E9E9E)

data class PresetAvatarItem(
    val id: Int,
    val emoji: String,
    val label: String
)

val presetAvatarsList = listOf(
    PresetAvatarItem(1, "🎮", "Gamer"),
    PresetAvatarItem(2, "🚀", "Explorador"),
    PresetAvatarItem(3, "⚡", "Foco Neón"),
    PresetAvatarItem(4, "🦊", "Audaz"),
    PresetAvatarItem(5, "🦉", "Sabio"),
    PresetAvatarItem(6, "🤖", "Cyber Bot"),
    PresetAvatarItem(7, "👑", "Líder"),
    PresetAvatarItem(8, "🎨", "Creador")
)

fun getEmojiForAvatarId(id: Int): String {
    return presetAvatarsList.find { it.id == id }?.emoji ?: "🎮"
}

@Composable
fun ProfileScreen(
    uiState: OnboardingUiState,
    onEvent: (OnboardingEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    var mostrarSelectorAvatar by remember { mutableStateOf(false) }

    // Launcher para abrir la galería del dispositivo y elegir una foto
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            onEvent(OnboardingEvent.OnAvatarChanged(uiState.avatarResId, it.toString()))
            mostrarSelectorAvatar = false
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBg)
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // --- BADGE SUPERIOR: CREA TU PERFIL ---
        Surface(
            color = InputDarkBg,
            shape = RoundedCornerShape(50)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .background(NeonGreenText, CircleShape)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "CREA TU PERFIL",
                    color = NeonGreenText,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Título y Subtítulo
        Text(
            text = "Configura tu radar",
            color = Color.White,
            fontSize = 32.sp,
            fontWeight = FontWeight.ExtraBold
        )
        Text(
            text = "Personaliza tu experiencia para desafiar a los algoritmos.",
            color = TextGray,
            fontSize = 14.sp,
            modifier = Modifier.padding(top = 4.dp, bottom = 20.dp)
        )

        // --- TARJETA DE AVATAR Y NIVEL ---
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, BorderDark, RoundedCornerShape(20.dp)),
            colors = CardDefaults.cardColors(containerColor = CardDarkBg),
            shape = RoundedCornerShape(20.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(contentAlignment = Alignment.BottomCenter) {
                    Box(
                        modifier = Modifier
                            .size(110.dp)
                            .border(3.dp, NeonYellow, CircleShape)
                            .padding(4.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF1E3A3A))
                            .clickable { mostrarSelectorAvatar = true },
                        contentAlignment = Alignment.Center
                    ) {
                        if (!uiState.customAvatarUri.isNullOrBlank()) {
                            AsyncImage(
                                model = uiState.customAvatarUri,
                                contentDescription = "Foto de perfil",
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Text(
                                text = getEmojiForAvatarId(uiState.avatarResId),
                                fontSize = 42.sp
                            )
                        }
                    }

                    Surface(
                        onClick = { mostrarSelectorAvatar = true },
                        color = NeonYellow,
                        shape = RoundedCornerShape(50),
                        modifier = Modifier.offset(y = 10.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Palette,
                                contentDescription = null,
                                tint = Color.Black,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "CAMBIAR",
                                color = Color.Black,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Black
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // --- ETIQUETA DE NIVEL ---
                Surface(
                    color = InputDarkBg,
                    shape = RoundedCornerShape(50),
                    border = BorderStroke(1.dp, BorderDark)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Shield,
                            contentDescription = null,
                            tint = NeonGreenText,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = uiState.levelTitle.uppercase(),
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // --- SECCIÓN TAG / APODO ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "¿CÓMO TE LLAMAS O CUÁL ES TU APODO?",
                color = TextGray,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "TAG",
                color = NeonGreenText,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, BorderDark, RoundedCornerShape(12.dp)),
            color = InputDarkBg,
            shape = RoundedCornerShape(12.dp)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "# ", color = NeonGreenText, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                BasicTextField(
                    value = uiState.gamertag,
                    onValueChange = { onEvent(OnboardingEvent.OnGamertagChanged(it)) },
                    textStyle = LocalTextStyle.current.copy(
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = if (uiState.isFormValid) NeonYellow else TextGray,
                    modifier = Modifier.size(22.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // --- SECCIÓN EDAD ---
        Text(
            text = "TU EDAD",
            color = TextGray,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, BorderDark, RoundedCornerShape(16.dp)),
            colors = CardDefaults.cardColors(containerColor = InputDarkBg),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Botón Restar (-)
                    IconButton(
                        onClick = { onEvent(OnboardingEvent.OnAgeChanged(uiState.age - 1)) },
                        modifier = Modifier
                            .size(48.dp)
                            .background(CardDarkBg, RoundedCornerShape(8.dp))
                    ) {
                        Icon(
                            imageVector = Icons.Default.Remove,
                            contentDescription = "Restar edad",
                            tint = Color.White
                        )
                    }

                    // Número de edad
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            text = "${uiState.age}",
                            color = NeonYellow,
                            fontSize = 36.sp,
                            fontWeight = FontWeight.Black
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "AÑOS",
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 6.dp)
                        )
                    }

                    // Botón Sumar (+)
                    IconButton(
                        onClick = { onEvent(OnboardingEvent.OnAgeChanged(uiState.age + 1)) },
                        modifier = Modifier
                            .size(48.dp)
                            .background(CardDarkBg, RoundedCornerShape(8.dp))
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Sumar edad",
                            tint = Color.White
                        )
                    }
                }

                if (!uiState.isTargetAge) {
                    Text(
                        text = "Recomendado para 13 a 16 años",
                        color = Color(0xFFFF583A),
                        fontSize = 11.sp,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // --- SECCIÓN GÉNERO ---
        Text(
            text = "IDENTIDAD / GÉNERO",
            color = TextGray,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            GenderChip(
                text = "FEMENINO",
                isSelected = uiState.gender == GenderOption.FEMALE,
                modifier = Modifier.weight(1f),
                onClick = { onEvent(OnboardingEvent.OnGenderSelected(GenderOption.FEMALE)) }
            )

            GenderChip(
                text = "MASCULINO",
                isSelected = uiState.gender == GenderOption.MASCULINE,
                modifier = Modifier.weight(1f),
                onClick = { onEvent(OnboardingEvent.OnGenderSelected(GenderOption.MASCULINE)) }
            )
        }

        Spacer(modifier = Modifier.height(28.dp))

        // --- BOTÓN PRINCIPAL: COMENZAR LA AVENTURA ---
        Button(
            onClick = { onEvent(OnboardingEvent.OnSubmitProfile) },
            enabled = uiState.isFormValid,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = NeonYellow,
                disabledContainerColor = CardDarkBg
            ),
            shape = RoundedCornerShape(50)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "COMENZAR LA AVENTURA",
                    color = if (uiState.isFormValid) Color.Black else TextGray,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Black
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "⚡", fontSize = 18.sp)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }

    // --- DIÁLOGO PARA SELECCIONAR FOTO O AVATAR ---
    if (mostrarSelectorAvatar) {
        AlertDialog(
            onDismissRequest = { mostrarSelectorAvatar = false },
            title = {
                Text(
                    text = "SELECCIONA TU FOTO O AVATAR",
                    fontWeight = FontWeight.Black,
                    fontSize = 16.sp,
                    color = Color.White
                )
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // 1. Botón para elegir foto desde Galería
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(NeonYellow)
                            .clickable { galleryLauncher.launch("image/*") },
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Image,
                                contentDescription = null,
                                tint = Color.Black,
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = "ELEGIR FOTO DE LA GALERÍA",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.Black
                            )
                        }
                    }

                    Text(
                        text = "O elige uno de nuestros avatares:",
                        fontSize = 12.sp,
                        color = TextGray
                    )

                    // 2. Grilla de Avatares Predefinidos
                    Column(
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        presetAvatarsList.chunked(4).forEach { rowItems ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceAround
                            ) {
                                rowItems.forEach { item ->
                                    val isSelected = uiState.customAvatarUri == null && uiState.avatarResId == item.id
                                    Box(
                                        modifier = Modifier
                                            .size(54.dp)
                                            .clip(CircleShape)
                                            .background(if (isSelected) NeonYellow.copy(alpha = 0.3f) else InputDarkBg)
                                            .border(
                                                width = if (isSelected) 2.5.dp else 1.dp,
                                                color = if (isSelected) NeonYellow else BorderDark,
                                                shape = CircleShape
                                            )
                                            .clickable {
                                                onEvent(OnboardingEvent.OnAvatarChanged(item.id, null))
                                                mostrarSelectorAvatar = false
                                            },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(text = item.emoji, fontSize = 24.sp)
                                    }
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {},
            dismissButton = {
                TextButton(onClick = { mostrarSelectorAvatar = false }) {
                    Text("CANCELAR", color = TextGray, fontWeight = FontWeight.Bold)
                }
            },
            containerColor = CardDarkBg,
            titleContentColor = Color.White
        )
    }
}

@Composable
fun GenderChip(
    text: String,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        modifier = modifier
            .border(
                width = 1.dp,
                color = if (isSelected) NeonGreenText else BorderDark,
                shape = RoundedCornerShape(50)
            ),
        color = InputDarkBg,
        shape = RoundedCornerShape(50)
    ) {
        Row(
            modifier = Modifier.padding(vertical = 12.dp, horizontal = 16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = NeonGreenText,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
            }
            Text(
                text = text,
                color = if (isSelected) NeonGreenText else Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Suppress("Unused")
@Preview(
    showBackground = true,
    showSystemUi = true,
    name = "Profile Screen Interactive Preview"
)
@Composable
fun ProfileScreenPreview() {
    var state by remember { mutableStateOf(OnboardingUiState(gamertag = "Alex_24")) }

    ProfileScreen(
        uiState = state,
        onEvent = { event ->
            when (event) {
                is OnboardingEvent.OnGamertagChanged -> {
                    state = state.copy(
                        gamertag = event.gamertag,
                        isFormValid = event.gamertag.isNotBlank()
                    )
                }
                is OnboardingEvent.OnAgeChanged -> {
                    val newAge = event.age.coerceIn(10, 99)
                    state = state.copy(
                        age = newAge,
                        isTargetAge = newAge in 13..16
                    )
                }
                is OnboardingEvent.OnGenderSelected -> {
                    state = state.copy(gender = event.gender)
                }
                is OnboardingEvent.OnAvatarChanged -> {
                    state = state.copy(
                        avatarResId = event.avatarResId,
                        customAvatarUri = event.customUri
                    )
                }
                is OnboardingEvent.OnSubmitProfile -> {
                    // Acción de prueba en preview
                }
            }
        }
    )
}

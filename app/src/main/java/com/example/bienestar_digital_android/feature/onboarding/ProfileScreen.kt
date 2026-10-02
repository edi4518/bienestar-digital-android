package com.example.bienestar_digital_android.feature.onboarding


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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// --- Paleta de colores ---
val DarkBg = Color(0xFF0D0E11)
val CardDarkBg = Color(0xFF16181D)
val InputDarkBg = Color(0xFF1F222A)
val NeonYellow = Color(0xFFD2FF00)
val NeonGreenText = Color(0xFF00FFC2)
val BorderDark = Color(0xFF2A2E39)
val TextGray = Color(0xFF9E9E9E)

@Composable
fun ProfileScreen(
    // --- ESTADOS (Datos que vienen de la Lógica) ---
    gamertag: String,
    age: Int,
    selectedGender: String,

    // --- EVENTOS (Acciones que se envían a la Lógica) ---
    onGamertagChange: (String) -> Unit,
    onAgeChange: (Int) -> Unit,
    onGenderSelect: (String) -> Unit,
    onChangeAvatarClick: () -> Unit,
    onStartAdventureClick: () -> Unit,

    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBg)
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Badge superior: CREA TU PERFIL
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

        // --- TARJETA DE AVATAR ---
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
                            .background(Color(0xFF1E3A3A)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "🎮",
                            fontSize = 35.sp
                        )
                    }

                    Surface(
                        color = NeonYellow,
                        shape = RoundedCornerShape(50),
                        modifier = Modifier
                            .offset(y = 10.dp)
                            .clickable { onChangeAvatarClick() }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
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
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // --- SECCIÓN TAG / APODO ---
        Text(
            text = "¿CÓMO TE LLAMAS O CUÁL ES TU APODO?",
            color = TextGray,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
        )

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
                    value = gamertag,
                    onValueChange = onGamertagChange,
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
                    tint = if (gamertag.isNotBlank()) NeonYellow else TextGray,
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
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Botón Restar (-)
                Surface(
                    modifier = Modifier
                        .size(48.dp)
                        .clickable { onAgeChange(age - 1) },
                    color = CardDarkBg,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(text = "—", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    }
                }

                // Número de edad centrado
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = "$age",
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
                Surface(
                    modifier = Modifier
                        .size(48.dp)
                        .clickable { onAgeChange(age + 1) },
                    color = CardDarkBg,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(text = "+", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    }
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
                isSelected = selectedGender == "FEMENINO",
                modifier = Modifier.weight(1f),
                onClick = { onGenderSelect("FEMENINO") }
            )

            GenderChip(
                text = "MASCULINO",
                isSelected = selectedGender == "MASCULINO",
                modifier = Modifier.weight(1f),
                onClick = { onGenderSelect("MASCULINO") }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        GenderChip(
            text = "PREFIERO NO DECIR / OTRO",
            isSelected = selectedGender == "OTRO",
            modifier = Modifier.fillMaxWidth(),
            onClick = { onGenderSelect("OTRO") }
        )

        Spacer(modifier = Modifier.height(32.dp))

        // --- BOTÓN PRINCIPAL DE ACCIÓN ---
        Button(
            onClick = onStartAdventureClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = NeonYellow),
            shape = RoundedCornerShape(50)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "COMENZAR LA AVENTURA",
                    color = Color.Black,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Black
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "⚡", fontSize = 18.sp)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
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
        modifier = modifier
            .border(
                width = 1.dp,
                color = if (isSelected) NeonGreenText else BorderDark,
                shape = RoundedCornerShape(50)
            )
            .clickable { onClick() },
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

// Preview con valores vacíos/iniciales para ver cómo arranca
@Preview(
    showBackground = true,
    showSystemUi = true,
    name = "Profile Screen Preview"
)
@Composable
fun ProfileScreenPreview() {
    ProfileScreen(
        gamertag = "", // Empieza vacío
        age = 16,
        selectedGender = "MASCULINO",
        onGamertagChange = {},
        onAgeChange = {},
        onGenderSelect = {},
        onChangeAvatarClick = {},
        onStartAdventureClick = {}
    )
}